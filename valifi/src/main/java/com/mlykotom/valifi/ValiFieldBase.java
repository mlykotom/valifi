package com.mlykotom.valifi;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.mlykotom.valifi.exceptions.ValiFiValidatorException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**
 * Base class for validation field. Holds value change listener and basic rules for validation.
 *
 * @param <ValueType> of the whole field (for now it's String and beta Calendar)
 */
@SuppressWarnings("unused")
public abstract class ValiFieldBase<ValueType> extends BaseObservable {
	protected ValueType mValue;
	protected LinkedHashMap<PropertyValidator<ValueType>, String> mPropertyValidators = new LinkedHashMap<>();
	protected boolean mIsEmptyAllowed = false;
	@Nullable protected List<ValiFieldBase> mBoundFields;
	protected long mErrorDelay;
	boolean mIsChanged = false;
	@Nullable ScheduledFuture<?> mLastTask;
	volatile long mDueTime = -1;
	volatile boolean mLastIsError = true;
	volatile boolean mIsError = false;
	@Nullable String mError;
	@Nullable String mLastError;
	@Nullable private ValiFiForm mParentForm;
	protected OnPropertyChangedCallback mCallback = setupOnPropertyChangedCallback();
	@Nullable private ScheduledExecutorService mScheduler;
	final Runnable mNotifyErrorRunnable = setupNotifyErrorRunnable();


	public interface PropertyValidator<T> {
		/**
		 * Decides whether field will be valid based on return value
		 *
		 * @param value field's actual value
		 * @return validity
		 */
		boolean isValid(@Nullable T value);
	}


	public ValiFieldBase() {
		mErrorDelay = ValiFi.getErrorDelay();
		addOnPropertyChangedCallback(mCallback);
	}


	/**
	 * @param defaultValue if not null, will mark that field is changed
	 */
	public ValiFieldBase(@Nullable ValueType defaultValue) {
		this();
		mValue = defaultValue;
		if(defaultValue != null) {
			mIsChanged = true;
		}
	}


	/**
	 * Error binding for TextInputLayout
	 *
	 * @param view         TextInputLayout to be set with
	 * @param errorMessage error message to show
	 */
	@BindingAdapter("error")
	public static void setError(TextInputLayout view, String errorMessage) {
		view.setError(errorMessage);
	}


	/**
	 * Error binding for EditText
	 *
	 * @param view         EditText to be set with
	 * @param errorMessage error message to show
	 */
	@BindingAdapter("error")
	public static void setError(EditText view, String errorMessage) {
		view.setError(errorMessage);
	}


	/**
	 * Helper for clearing all specified validated fields
	 *
	 * @param fields to be cleansed
	 */
	public static void destroyAll(ValiFieldBase... fields) {
		for(ValiFieldBase field : fields) {
			field.destroy();
		}
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
	 * @return converted string (e.g. for Date = formatted string)
	 */
	protected abstract String convertValueToString();


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
	 * Does not apply in cases when validation changes (e.g invalid -> valid or vice versa)
	 *
	 * @param delayMillis positive or zero time in milliseconds
	 * @return this, validators can be chained
	 */
	public ValiFieldBase<ValueType> setErrorDelay(long delayMillis) {
		if(delayMillis < 0) {
			throw new ValiFiValidatorException("Error delay can't be negative");
		}
		mErrorDelay = delayMillis;
		return this;
	}


	/**
	 * @return the containing value of the field
	 */
	public ValueType get() {
		return mValue;
	}


	/**
	 * Wrapper for easy setting value
	 *
	 * @param value to be set and notified about change
	 */
	public void set(@Nullable ValueType value) {
		if((value == mValue) || (value != null && value.equals(mValue))) return;

		mValue = value;
		notifyValueChanged(false);
	}


	/**
	 * This may be shown in layout as actual value
	 *
	 * @return value in string displayable in TextInputLayout/EditText
	 */
	@Bindable
	public String getValue() {
		return convertValueToString();
	}


	/**
	 * Sets new value (from binding)
	 *
	 * @param value to be set, if the same as older, skips
	 */
	public void setValue(@Nullable String value) {
		set((ValueType) value); // TODO have not abstract method unimplemented (with exception throwing)
		// TODO convert value (is it possible?)
	}


	/**
	 * Removes property change callback and clears custom validators
	 */
	public void destroy() {
		shutdownScheduler();
		removeOnPropertyChangedCallback(mCallback);
		mPropertyValidators.clear();
		mPropertyValidators = null;
		if(mBoundFields != null) {
			mBoundFields.clear();
			mBoundFields = null;
		}
		mParentForm = null;
		mIsChanged = false;
		mIsError = false;
		mIsEmptyAllowed = false;
	}


	/**
	 * Bundles this field to form
	 *
	 * @param form which validates all bundled fields
	 */
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
	 * Might be used for checking submit buttons because isError might be true when data not changed
	 *
	 * @return if property was changed and is valid
	 */
	@Bindable
	public boolean getIsValid() {
		return !mIsError & (mIsChanged | mIsEmptyAllowed);
	}


	/**
	 * @param errorResource to be shown (got from app's context)
	 * @param targetField   validates with this field
	 * @return this, so validators can be chained
	 * @see #addVerifyFieldValidator(String, ValiFieldBase)
	 */
	public ValiFieldBase<ValueType> addVerifyFieldValidator(@StringRes int errorResource, final ValiFieldBase<ValueType> targetField) {
		String errorMessage = getAppContext().getString(errorResource);
		return addVerifyFieldValidator(errorMessage, targetField);
	}


	/**
	 * Validates equality of this value and specified field's value.
	 * If specified field changes, it notifies this field's change listener.
	 *
	 * @param errorMessage to be shown if not valid
	 * @param targetField  validates with this field
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
	 * Ability to add custom validators
	 *
	 * @param validator which has value inside
	 * @return this, so validators can be chained
	 */
	public ValiFieldBase<ValueType> addCustomValidator(PropertyValidator<ValueType> validator) {
		mPropertyValidators.put(validator, null);
		return this;
	}


	public ValiFieldBase<ValueType> addCustomValidator(@StringRes int errorResource, PropertyValidator<ValueType> validator) {
		String errorMessage = getAppContext().getString(errorResource);
		return addCustomValidator(errorMessage, validator);
	}


	public ValiFieldBase<ValueType> addCustomValidator(String errorMessage, PropertyValidator<ValueType> validator) {
		mPropertyValidators.put(validator, errorMessage);
		if(mIsChanged) {
			notifyValueChanged(true);
		}
		return this;
	}


	/**
	 * Internaly fields can be binded together so that when one changes, it notifies others
	 *
	 * @param field to be notified when this field changed
	 */
	protected void addBoundField(ValiFieldBase field) {
		if(mBoundFields == null) {
			mBoundFields = new ArrayList<>();
		}
		mBoundFields.add(field);
	}


	protected int getErrorRes(@ValiFi.Builder.ValiFiErrorResource int field) {
		return ValiFi.getErrorRes(field);
	}


	protected Pattern getPattern(@ValiFi.Builder.ValiFiPattern int field) {
		return ValiFi.getPattern(field);
	}


	protected Context getAppContext() {
		return ValiFi.getContext();
	}


	/**
	 * Sets error state to this field + optionally to binded form
	 *
	 * @param isError      whether there's error or no
	 * @param errorMessage to be shown
	 */
	protected void setIsError(boolean isError, @Nullable String errorMessage) {
		mIsChanged = true;
		mIsError = isError;
		mError = errorMessage;
		notifyValidationChanged();
		if(mParentForm != null) {
			mParentForm.notifyValidationChanged(this);
		}
	}


	/**
	 * Notifies that value changed (internally)
	 *
	 * @param isImmediate if true, does not call binding notifier
	 */
	protected void notifyValueChanged(boolean isImmediate) {
		if(isImmediate) {
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
		notifyPropertyChanged(com.mlykotom.valifi.BR.isValid);
	}


	/**
	 * Notifies that error message changed
	 */
	protected void notifyErrorChanged() {
		if(mErrorDelay > 0) {
			mDueTime = System.currentTimeMillis() + mErrorDelay;
		}

		mNotifyErrorRunnable.run();
	}


	synchronized ScheduledExecutorService getScheduler() {
		if(mScheduler == null) {
			mScheduler = Executors.newSingleThreadScheduledExecutor();
		}

		return mScheduler;
	}


	synchronized void shutdownScheduler() {
		if(mScheduler == null) return;

		mScheduler.shutdownNow();
	}


	synchronized void cancelAndSetTask(@Nullable ScheduledFuture<?> task) {
		if(mLastTask != null) {
			mLastTask.cancel(true);
		}
		mLastTask = task;
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
				if(remainingDelay > 0 && mLastIsError == mIsError && !isErrorDifferent) {
					cancelAndSetTask(getScheduler().schedule(mNotifyErrorRunnable, remainingDelay, TimeUnit.MILLISECONDS));
				} else {
					mDueTime = -1;
					notifyPropertyChanged(com.mlykotom.valifi.BR.error);
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
	 * 3) validates all attached validators
	 *
	 * @return property changed callback which should be set as field
	 */
	private OnPropertyChangedCallback setupOnPropertyChangedCallback() {
		return new OnPropertyChangedCallback() {
			@Override
			public void onPropertyChanged(Observable observable, int brId) {
				if(brId != com.mlykotom.valifi.BR.value) return;

				// notifying bound fields about change
				if(mBoundFields != null) {
					for(ValiFieldBase field : mBoundFields) {
						if(!field.mIsChanged) continue;    // notifies only changed items
						field.notifyValueChanged(true);
					}
				}

				// checking if value can be empty
				ValueType actualValue = mValue;
				if(mIsEmptyAllowed && (actualValue == null || whenThisFieldIsEmpty(actualValue))) {
					setIsError(false, null);
					notifyErrorChanged();
					return;
				}

				// checking all set validators
				for(Map.Entry<PropertyValidator<ValueType>, String> entry : mPropertyValidators.entrySet()) {
					// all of setup validators must be valid, otherwise error
					if(!entry.getKey().isValid(actualValue)) {
						setIsError(true, entry.getValue());
						notifyErrorChanged();
						return;
					}
				}

				// set valid
				setIsError(false, null);
				notifyErrorChanged();
			}
		};
	}
}