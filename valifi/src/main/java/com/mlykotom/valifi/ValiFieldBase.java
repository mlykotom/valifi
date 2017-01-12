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

import com.mlykotom.valifi.fields.ValiFieldText;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * TODO weak reference for callback
 * TODO validators through annotations
 *
 * @param <ValueType>
 */
@SuppressWarnings("unused")
public abstract class ValiFieldBase<ValueType> extends BaseObservable {
	protected ValueType mValue;
	protected LinkedHashMap<PropertyValidator<ValueType>, String> mPropertyValidators = new LinkedHashMap<>();
	protected boolean mIsEmptyAllowed = false;
	@Nullable protected List<ValiFieldBase> mBoundFields;
	boolean mIsChanged = false;
	private boolean mIsError = false;
	private String mError;
	@Nullable private ValiFiForm mParentForm;
	/**
	 * Callback for handling all validators in one place
	 */
	protected OnPropertyChangedCallback mCallback = new OnPropertyChangedCallback() {
		@Override
		public void onPropertyChanged(Observable observable, int brId) {
			if(brId != com.mlykotom.valifi.BR.value) return;

			if(mBoundFields != null) {
				for(ValiFieldBase field : mBoundFields) {
					if(!field.mIsChanged) continue;    // notifies only changed items
					field.notifyPropertyChanged(com.mlykotom.valifi.BR.value);
				}
			}

			ValueType actualValue = mValue;
			if(mIsEmptyAllowed && (actualValue == null || whenThisFieldIsEmpty(actualValue))) {
				setIsError(false, null);
				return;
			}

			for(Map.Entry<PropertyValidator<ValueType>, String> entry : mPropertyValidators.entrySet()) {
				// all of setup validators must be valid, otherwise error
				if(!entry.getKey().isValid(actualValue)) {
					setIsError(true, entry.getValue());
					return;
				}
			}

			setIsError(false, null);
		}
	};


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
		addOnPropertyChangedCallback(mCallback);
	}


	public ValiFieldBase(ValueType defaultValue) {
		this();
		set(defaultValue);
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
	public static void destroyAll(ValiFieldText... fields) {
		for(ValiFieldText field : fields) {
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
	 * @return converted string (e.g. if Date -> formatted string)
	 */
	protected abstract String convertValueToString();


	/**
	 * Allows empty field to be valid.
	 * Useful when some field is not necessary but needs to be in proper format if filled.
	 *
	 * @return this, co validators can be chained
	 */
	public ValiFieldBase<ValueType> setEmptyAllowed(boolean isEmptyAllowed) {
		mIsEmptyAllowed = isEmptyAllowed;
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
		notifyPropertyChanged(com.mlykotom.valifi.BR.value);
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
		set((ValueType) value); // TODO convert value (is it possible?)
	}


	/**
	 * Removes property change callback and clears custom validators
	 */
	public void destroy() {
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
	 * <p>Gets actual validation flag which might be used for error messages.</p>
	 * !!THIS IS FALSE WHEN DATA WERE NOT SET YET!!
	 *
	 * @return valid flag -> false is default state
	 */
	@Bindable
	public boolean getIsError() {
		return mIsError;
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


	// ------------------ VERIFY OTHER FIELD VALIDATOR ------------------ //


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

	// ------------------ CUSTOM VALIDATOR ------------------ //


	public ValiFieldBase<ValueType> addCustomValidator(String errorMessage, PropertyValidator<ValueType> validator) {
		mPropertyValidators.put(validator, errorMessage);
		return this;
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
		notifyPropertyChanged(com.mlykotom.valifi.BR.isError);
		notifyPropertyChanged(com.mlykotom.valifi.BR.isValid);
		notifyPropertyChanged(com.mlykotom.valifi.BR.error);
		if(mParentForm != null) {
			mParentForm.fieldValidationChanged(this);
		}
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
}