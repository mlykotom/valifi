package com.mlykotom.valifi;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.mlykotom.valifi.exceptions.ValiFiException;
import com.mlykotom.valifi.exceptions.ValiFiValidatorException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;

/**
 * Base class for validation field. Holds value change listener and basic rules for validation.
 *
 * @param <ValueType> of the whole field (for now it's String and beta Calendar)
 */
@SuppressWarnings("unused")
public abstract class ValiFieldBase<ValueType> extends BaseObservable implements ValiFiValidable {
	@Nullable protected ValueType mValue;
	protected boolean mIsEmptyAllowed = false;
	@Nullable protected List<ValiFieldBase> mBoundFields;
	// --- maps of validators (to be validated)
	protected LinkedHashMap<PropertyValidator<ValueType>, String> mPropertyValidators = new LinkedHashMap<>();
	@Nullable protected LinkedHashMap<AsyncPropertyValidator<ValueType>, String> mAsyncPropertyValidators;
	// --- delaying times
	protected long mErrorDelay;
	protected long mAsyncValidationDelay;
	// --- future tasks for delayed error + async validators
	@Nullable ScheduledFuture<?> mLastTask;
	@Nullable ScheduledFuture<?> mLastValidationFuture;
	boolean mIsChanged = false;
	@Nullable String mError;
	@Nullable String mLastError;
	volatile boolean mInProgress = false;
	volatile long mDueTime = -1;
	volatile boolean mLastIsError = true;
	volatile boolean mIsError = false;
	@Nullable Runnable mAsyncValidationRunnable;
	// --- others
	boolean mIsResetting = false;
	@Nullable private ValiFiForm mParentForm;
	@Nullable private ScheduledExecutorService mScheduler;
	protected OnPropertyChangedCallback mCallback = setupOnPropertyChangedCallback();
	final Runnable mNotifyErrorRunnable = setupNotifyErrorRunnable();

	public interface PropertyValidator<T> {
		/**
		 * Decides whether field will be valid based on return value.
		 * Can't be blocking, it's called on UI thread!
		 *
		 * @param value field's actual value
		 * @return validity
		 */
		boolean isValid(@Nullable T value);
	}

	/**
	 * Validator for asynchronous calls (which would take some time to finish e.g. network requests)
	 *
	 * @param <T> the same type as this field value's type
	 */
	public interface AsyncPropertyValidator<T> {
		/**
		 * Decides whether field wil be valid based on return value.
		 * It's called off the UI thread, therefore can be blocking.
		 * If custom long-term algorithm is used, it should check {@link Thread#interrupted()} and throw {@link InterruptedException}
		 * <p>
		 * Simple example:
		 * <pre>{@code
		 * for(int i = 0; i < Integer.MAX_VALUE; i++) {
		 * 	if(Thread.interrupted()) throw new InterruptedException();
		 * 	// do something
		 * }
		 * }</pre>
		 *
		 * @param value field's actual value
		 * @return validity
		 * @throws InterruptedException when thrown, means another execution is right after
		 */
		boolean isValid(@Nullable T value) throws InterruptedException;
	}

	public ValiFieldBase() {
		this(null);
	}

	/**
	 * @param defaultValue if not null, will mark that field is changed
	 */
	public ValiFieldBase(@Nullable ValueType defaultValue) {
		this(defaultValue, true);
	}

	/**
	 * @param defaultValue is set to this field in construction
	 * @param markAsChanged if default value marks this field as changed
	 * (may be useful when loading data from network, not to trigger validation, because default value will be the same)
	 */
	public ValiFieldBase(@Nullable ValueType defaultValue, boolean markAsChanged) {
		mErrorDelay = ValiFi.getErrorDelay();
		mAsyncValidationDelay = ValiFi.getAsyncValidationDelay();

		mValue = defaultValue;

		if (defaultValue != null && markAsChanged) {
			mIsChanged = true;
		}
		addOnPropertyChangedCallback(mCallback);
	}

	/**
	 * Error binding for TextInputLayout
	 *
	 * @param view TextInputLayout to be set with
	 * @param errorMessage error message to show
	 */
	@BindingAdapter("error")
	public static void setError(TextInputLayout view, String errorMessage) {
		view.setError(errorMessage);
	}

	/**
	 * Error binding for EditText
	 *
	 * @param view EditText to be set with
	 * @param errorMessage error message to show
	 */
	@BindingAdapter("error")
	public static void setError(EditText view, String errorMessage) {
		view.setError(errorMessage);
	}

	/**
	 * Helper for destroying all specified fields
	 *
	 * @param fields to be destroyed
	 * @see ValiFi#destroyFields(ValiFiValidable[])
	 */
	public static void destroyAll(ValiFiValidable... fields) {
		ValiFi.destroyFields(fields);
	}

	/**
	 * Checking for specific type if value is empty.
	 * Used for checking if empty is allowed.
	 *
	 * @param actualValue value when checking
	 * @return true when value is empty, false when values is not empty (e.g for String, use isEmpty())
	 * @see #mCallback
	 */
	protected abstract boolean whenThisFieldIsEmpty(@NonNull ValueType actualValue);

	/**
	 * Any inherited field must be able to convert to String.
	 * This is so that it's possible to show it in TextView/EditText
	 *
	 * @param value actual value to be converted
	 * @return converted string (e.g. for Date = formatted string)
	 */
	protected abstract String convertValueToString(@NonNull ValueType value);

	/**
	 * Converts string to this value. This is called from data binding so if any class is convertable, override this
	 *
	 * @param value actual value from input
	 * @return this value of type
	 */
	@Nullable
	protected abstract ValueType convertStringToValue(@Nullable String value);

	/**
	 * Might be used for checking submit buttons because isError might be true when data not changed
	 * Field is valid when:
	 * - no error is set
	 * - validation is not in progress
	 * - field was already changed OR was set that can be empty
	 *
	 * @return if property was changed, is not in progress, and is valid
	 */
	@Bindable
	@Override
	public boolean isValid() {
		return !mInProgress & !mIsError & (mIsChanged | mIsEmptyAllowed);
	}

	/**
	 * Removes property change callback and clears custom validators
	 */
	public void destroy() {
		shutdownScheduler();
		removeOnPropertyChangedCallback(mCallback);

		if (mPropertyValidators != null) {
			mPropertyValidators.clear();
			mPropertyValidators = null;
		}

		if (mAsyncPropertyValidators != null) {
			mAsyncPropertyValidators.clear();
			mAsyncPropertyValidators = null;
		}

		if (mBoundFields != null) {
			mBoundFields.clear();
			mBoundFields = null;
		}

		mParentForm = null;
		mIsChanged = false;
		mIsError = false;
		mIsEmptyAllowed = false;
	}

	/**
	 * Clears the state of the field (e.g. after submit of form).
	 */
	@Override
	public void reset() {
		mIsResetting = true;
		mIsError = false;
		mError = null;
		mInProgress = false;
		mIsChanged = false;

		setValue("");
		notifyValidationChanged();
		refreshError();
		mIsResetting = false;
	}

	/**
	 * If you want to manually show error for the field
	 */
	@Override
	public void validate() {
		notifyValueChanged(true);
		refreshError();
	}

	/**
	 * Allows empty field to be valid.
	 * Useful when some field is not necessary but needs to be in proper format if filled.
	 *
	 * @param isEmptyAllowed if true, field may be empty or null to be valid
	 * @return this, co validators can be chained
	 */
	public ValiFieldBase<ValueType> setEmptyAllowed(boolean isEmptyAllowed) {
		mIsEmptyAllowed = isEmptyAllowed;
		return this;
	}

	/**
	 * Sets how much it will take before error is shown.
	 * Does not apply in cases when validation changes (e.g invalid to valid or vice versa)
	 *
	 * @param delayMillis positive number - time in milliseconds
	 * @return this, validators can be chained
	 * @see #setErrorDelay(ValiFiErrorDelay) for immediate or manual mode
	 */
	public ValiFieldBase<ValueType> setErrorDelay(long delayMillis) {
		if (delayMillis <= 0) {
			throw new ValiFiValidatorException("Error delay must be positive");
		}

		mErrorDelay = delayMillis;
		return this;
	}

	/**
	 * Sets whether validation will be immediate or never
	 *
	 * @param delayType either never or immediate
	 * @return this, so validators can be chained
	 * @see #setErrorDelay(long) for setting exact time
	 */
	public ValiFieldBase<ValueType> setErrorDelay(ValiFiErrorDelay delayType) {
		mErrorDelay = delayType.delayMillis;
		return this;
	}

	/**
	 * Overrides default delay for asynchronous validation.
	 *
	 * @param millis can be milliseconds 0+
	 * @return this, so validators can be chained
	 * @see ValiFi.Builder#setAsyncValidationDelay(long) for default value
	 */
	public ValiFieldBase<ValueType> setAsyncValidationDelay(long millis) {
		if (millis < 0) {
			throw new ValiFiValidatorException("Asynchronous delay must be positive or immediate");
		}

		mAsyncValidationDelay = millis;
		return this;
	}

	/**
	 * @return the containing value of the field
	 */
	@Nullable
	public ValueType get() {
		return mValue;
	}

	/**
	 * Wrapper for easy setting value
	 *
	 * @param value to be set and notified about change
	 */
	public void set(@Nullable ValueType value) {
		if ((value == mValue) || (value != null && value.equals(mValue))) return;

		mValue = value;
		notifyValueChanged(false);
	}

	/**
	 * This may be shown in layout as actual value
	 *
	 * @return value in string displayable in TextInputLayout/EditText
	 */
	@Nullable
	@Bindable
	public String getValue() {
		if (mValue == null) return null;
		return convertValueToString(mValue);
	}

	/**
	 * Sets new value (from binding)
	 *
	 * @param value to be set, if the same as older, skips
	 */
	public void setValue(@Nullable String value) {
		set(convertStringToValue(value));
	}

	/**
	 * Flag for showing whether async validators are in progress.
	 * It may be used for showing/hiding progress view, etc
	 *
	 * @return whether validation in progress
	 */
	@Bindable
	public boolean isInProgress() {
		return mInProgress;
	}

	/**
	 * Sets the field that is in validating process (because of async validations)
	 * Notifies {@link #isValid()} which keeps it invalid when in progress.
	 *
	 * @param inProgress whether validates or not
	 */
	protected synchronized void setInProgress(boolean inProgress) {
		if (inProgress == mInProgress) return;

		mInProgress = inProgress;
		notifyPropertyChanged(com.mlykotom.valifi.BR.inProgress);
		notifyValidationChanged();
	}

	/**
	 * Bundles this field to form
	 *
	 * @param form which validates all bundled fields
	 */
	@Override
	public void setFormValidation(@Nullable ValiFiForm form) {
		mParentForm = form;
	}

	@Nullable
	public ValiFiForm getBoundForm() {
		return mParentForm;
	}

	@Nullable
	@Bindable
	public String getError() {
		return mError;
	}

	/**
	 * May serve for specifying temporary error from different source.
	 * This doesn't affect validation of the field, it just shows custom error message.
	 * The error will be changed for the one from validation when input changes.
	 *
	 * @param error temporary error message
	 */
	public void setError(@NonNull String error) {
		mError = error;
		refreshError();
	}

	/**
	 * @param errorResource to be shown (got from app's context)
	 * @param targetField validates with this field
	 * @return this, so validators can be chained
	 * @see #addVerifyFieldValidator(String, ValiFieldBase)
	 */
	public ValiFieldBase<ValueType> addVerifyFieldValidator(@StringRes int errorResource, final ValiFieldBase<ValueType> targetField) {
		String errorMessage = getString(errorResource);
		return addVerifyFieldValidator(errorMessage, targetField);
	}

	/**
	 * Validates equality of this value and specified field's value.
	 * If specified field changes, it notifies this field's change listener.
	 *
	 * @param errorMessage to be shown if not valid
	 * @param targetField validates with this field
	 * @return this, so validators can be chained
	 */
	public ValiFieldBase<ValueType> addVerifyFieldValidator(String errorMessage, final ValiFieldBase<ValueType> targetField) {
		addCustomValidator(errorMessage, new PropertyValidator<ValueType>() {
			@Override
			public boolean isValid(@Nullable ValueType value) {
				ValueType fieldVal = targetField.get();
				return (value == targetField.get()) || (value != null && value.equals(fieldVal));
			}
		});

		targetField.addBoundField(this);
		return this;
	}

	/**
	 * Adds validator without error message.
	 * This means no error will be shown, but field won't be valid
	 *
	 * @param validator implementation of validation
	 * @return this, so validators can be chained
	 * @see #addCustomValidator(String, PropertyValidator)
	 */
	public ValiFieldBase<ValueType> addCustomValidator(PropertyValidator<ValueType> validator) {
		return addCustomValidator(null, validator);
	}

	/**
	 * Adds custom validator with error message string resource
	 *
	 * @param errorResource string resource shown when validator not valid
	 * @param validator implementation of validation
	 * @return this, so validators can be chained
	 * @see #addCustomValidator(String, PropertyValidator)
	 */
	public ValiFieldBase<ValueType> addCustomValidator(@StringRes int errorResource, PropertyValidator<ValueType> validator) {
		String errorMessage = getString(errorResource);
		return addCustomValidator(errorMessage, validator);
	}

	/**
	 * Adds custom validator which will be validated when value property changes.
	 *
	 * @param errorMessage to be shown if field does not meet this validation
	 * @param validator implementation of validation
	 * @return this, so validators can be chained
	 */
	public ValiFieldBase<ValueType> addCustomValidator(String errorMessage, PropertyValidator<ValueType> validator) {
		mPropertyValidators.put(validator, errorMessage);
		if (mIsChanged) {
			notifyValueChanged(true);
		}
		return this;
	}

	/**
	 * Removes property validator
	 *
	 * @param validator which was set before
	 * @return true, if successfully removed, false otherwise
	 */
	public boolean removeValidator(@NonNull PropertyValidator<ValueType> validator) {
		return mPropertyValidators.remove(validator) != null;
	}

	/**
	 * Adds validator without error message which does not block main thread.
	 * This means no error will be shown, but field won't be valid
	 *
	 * @param validator implementation of validation
	 * @return this, so validators can be chained
	 * @see #addCustomAsyncValidator(String, AsyncPropertyValidator)
	 */
	public ValiFieldBase<ValueType> addCustomAsyncValidator(AsyncPropertyValidator<ValueType> validator) {
		return addCustomAsyncValidator(null, validator);
	}

	/**
	 * Adds asynchronous validation which does not block main thread.
	 *
	 * @param errorResource string resource shown when validator not valid
	 * @param validator implementation of validation
	 * @return this, so validators can be chained
	 * @see #addCustomAsyncValidator(String, AsyncPropertyValidator)
	 */
	public ValiFieldBase<ValueType> addCustomAsyncValidator(@StringRes int errorResource, AsyncPropertyValidator<ValueType> validator) {
		String errorMessage = getString(errorResource);
		return addCustomAsyncValidator(errorMessage, validator);
	}

	/**
	 * Adds asynchronous validation which does not block main thread.
	 * Validation is started after delay {@link #setAsyncValidationDelay(long)} and cancels previous async validation
	 *
	 * @param errorMessage to be shown if field does not meet this validation
	 * @param validator implementation of validation
	 * @return this, so validators can be chained
	 */
	public ValiFieldBase<ValueType> addCustomAsyncValidator(String errorMessage, AsyncPropertyValidator<ValueType> validator) {
		if (mAsyncPropertyValidators == null) {
			mAsyncPropertyValidators = new LinkedHashMap<>();
		}

		mAsyncPropertyValidators.put(validator, errorMessage);
		if (mIsChanged) {
			notifyValueChanged(true);
		}
		return this;
	}

	/**
	 * Removes async validator (if any was set)
	 *
	 * @param validator to be removed
	 * @return true if successfully removed, false otherwise
	 */
	public boolean removeAsyncValidator(@NonNull AsyncPropertyValidator<ValueType> validator) {
		if (mAsyncPropertyValidators == null) {
			throw new ValiFiException("No async validators were set!");
		}

		return mAsyncPropertyValidators.remove(validator) != null;
	}

	/**
	 * If you want to manually show error for the field
	 */
	@Override
	public void refreshError() {
		notifyPropertyChanged(com.mlykotom.valifi.BR.error);
	}

	/**
	 * Internaly fields can be binded together so that when one changes, it notifies others
	 *
	 * @param field to be notified when this field changed
	 */
	protected void addBoundField(ValiFieldBase field) {
		if (mBoundFields == null) {
			mBoundFields = new ArrayList<>();
		}
		mBoundFields.add(field);
	}

	protected int getErrorRes(@ValiFi.Builder.ValiFiErrorResource int field) {
		return ValiFi.getErrorRes(field);
	}

	protected PropertyValidator<String> getValidator(@ValiFi.Builder.ValiFiPattern int field) {
		return ValiFi.getValidator(field);
	}

	/**
	 * Serves for getting strings in fields
	 *
	 * @param stringRes R.string.*
	 * @param formatArgs the same as in context.getString()
	 * @return formatted String | in case of tests, returns "string-*"
	 */
	protected String getString(@StringRes int stringRes, Object... formatArgs) {
		return ValiFi.getString(stringRes, formatArgs);
	}

	/**
	 * Sets error state to this field + optionally to binded form
	 *
	 * @param isError whether there's error or no
	 * @param errorMessage to be shown
	 */
	protected void setIsError(boolean isError, @Nullable String errorMessage) {
		mIsChanged = true;
		mIsError = isError;
		mError = errorMessage;
		notifyValidationChanged();

		// Notifies that error message changed
		if (mErrorDelay != ValiFiErrorDelay.NEVER.delayMillis) {
			if (mErrorDelay > 0) {
				mDueTime = System.currentTimeMillis() + mErrorDelay;
			}
			mNotifyErrorRunnable.run();
		}
	}

	/**
	 * Notifies that value changed (internally)
	 *
	 * @param isImmediate if true, does not call binding notifier
	 */
	protected void notifyValueChanged(boolean isImmediate) {
		if (isImmediate) {
			// shortcut to just call the callback
			mCallback.onPropertyChanged(null, com.mlykotom.valifi.BR.value);
		} else {
			notifyPropertyChanged(com.mlykotom.valifi.BR.value);
		}
	}

	/**
	 * Notifies that field's validation flag changed
	 */
	protected void notifyValidationChanged() {
		notifyPropertyChanged(com.mlykotom.valifi.BR.valid);
		if (mParentForm != null) {
			mParentForm.notifyValidationChanged(this);
		}
	}

	/**
	 * Notifies bound fields about value change
	 */
	void notifyBoundFieldsValueChanged() {
		if (mBoundFields == null) return;

		for (ValiFieldBase field : mBoundFields) {
			if (!field.mIsChanged) continue;    // notifies only changed items
			field.notifyValueChanged(true);
		}
	}

	synchronized ScheduledExecutorService getScheduler() {
		if (mScheduler == null) {
			mScheduler = Executors.newSingleThreadScheduledExecutor();
		}

		return mScheduler;
	}

	synchronized void shutdownScheduler() {
		if (mScheduler == null) return;

		mScheduler.shutdownNow();
	}

	synchronized void cancelAndSetTask(@Nullable ScheduledFuture<?> task) {
		if (mLastTask != null) {
			mLastTask.cancel(true);
		}
		mLastTask = task;
	}

	synchronized void cancelAndSetValidationTask(@Nullable ScheduledFuture<?> task) {
		if (mLastValidationFuture != null) {
			mLastValidationFuture.cancel(true);
		}
		mLastValidationFuture = task;
	}

	/**
	 * Checks synchronous validators one by one and sets error to the field if any of them is invalid
	 *
	 * @return true if all validators are valid, false if any of them is invalid
	 */
	boolean checkBlockingValidators() {
		for (Map.Entry<PropertyValidator<ValueType>, String> entry : mPropertyValidators.entrySet()) {
			// all of setup validators must be valid, otherwise error
			if (!entry.getKey().isValid(mValue)) {
				setIsError(true, entry.getValue());
				return false;
			}
		}

		// set valid
		setIsError(false, null);
		return true;
	}

	/**
	 * Checks asynchronously all async validators and sets error to the field if any of them is invalid
	 */
	void checkAsyncValidatorsIfAny() {
		if (mAsyncPropertyValidators == null || mAsyncPropertyValidators.isEmpty()) {
			setInProgress(false);
			return;
		}

		if (mAsyncValidationRunnable == null) {
			mAsyncValidationRunnable = new Runnable() {
				@Override
				public void run() {
					setInProgress(true);
					// checking all set validators
					for (Map.Entry<AsyncPropertyValidator<ValueType>, String> entry : mAsyncPropertyValidators.entrySet()) {
						// all of setup validators must be valid, otherwise error
						try {
							if (!entry.getKey().isValid(mValue)) {
								setIsError(true, entry.getValue());
								setInProgress(false);
								return;
							}
						} catch (InterruptedException e) {
							// interruption should be called only when another task will be running
							// it will keep the progress and won't show any error
							return;
						}
					}

					// set valid
					setIsError(false, null);
					setInProgress(false);
				}
			};
		}

		// validate asynchronous validators (if any)
		cancelAndSetValidationTask(getScheduler().schedule(mAsyncValidationRunnable, mAsyncValidationDelay, TimeUnit.MILLISECONDS));
	}

	/**
	 * Runnable for showing errors.
	 * May be delayed after user stops typing
	 *
	 * @return runnable because of thread executor
	 */
	@SuppressWarnings("StringEquality")
	private Runnable setupNotifyErrorRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				// if error delay not set, don't schedule
				long remainingDelay = mErrorDelay > 0 ? mDueTime - System.currentTimeMillis() : 0;
				// errors are not the same (checking references, not content)
				boolean isErrorDifferent = (mLastError != null && mError != null && mLastError != mError);
				// validation changed from valid -> invalid or vice versa
				if (remainingDelay > 0 && mLastIsError == mIsError && !isErrorDifferent) {
					cancelAndSetTask(getScheduler().schedule(mNotifyErrorRunnable, remainingDelay, TimeUnit.MILLISECONDS));
				} else {
					mDueTime = -1;
					refreshError();
					cancelAndSetTask(null);
				}

				mLastError = mError;
				mLastIsError = mIsError;
			}
		};
	}

	/**
	 * Core of all validations.
	 * Called when value changed (from layout or by notifyPropertyChanged)
	 * 1) notifies bound fields (e.g. when verifying fields)
	 * 2) checks if value can be empty
	 * 3) validates all attached blocking validators
	 * 4) validates attached asynchronous validators
	 *
	 * @return property changed callback which should be set as field
	 */
	private OnPropertyChangedCallback setupOnPropertyChangedCallback() {
		return new OnPropertyChangedCallback() {
			@Override
			public void onPropertyChanged(Observable observable, int brId) {
				if (brId != com.mlykotom.valifi.BR.value || mIsResetting) return;

				// ad 1) notifying bound fields
				notifyBoundFieldsValueChanged();

				// ad 2) checking if value can be empty
				if (mIsEmptyAllowed && (mValue == null || whenThisFieldIsEmpty(mValue))) {
					setIsError(false, null);
					return;
				}

				cancelAndSetValidationTask(null);
				setInProgress(true);

				// ad 3) validating synchronous validators
				if (!checkBlockingValidators()) {
					setInProgress(false);
					cancelAndSetValidationTask(null);
					return;
				}

				// ad 4) validating asynchronous validators
				checkAsyncValidatorsIfAny();
			}
		};
	}
}