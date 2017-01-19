package com.mlykotom.valifi.fields;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.ValiFieldBase;
import com.mlykotom.valifi.exceptions.ValiFiValidatorException;

import java.util.regex.Pattern;


/**
 * Handles validating on simple string fields
 */
@SuppressWarnings("unused")
public class ValiFieldText extends ValiFieldBase<String> {
	private boolean mHasNotEmptyValidator = false;


	public ValiFieldText() {
		super();
	}


	public ValiFieldText(String defaultValue) {
		super(defaultValue);
	}


	@Override
	public String toString() {
		return mValue;
	}


	/**
	 * Checking for specific type if value is empty.
	 * Used for checking if empty is allowed.
	 *
	 * @param actualValue value when checking
	 * @return true when value is empty, false when values is not empty (e.g for String, use isEmpty())
	 * @see #mCallback
	 */
	@Override
	protected boolean whenThisFieldIsEmpty(@NonNull String actualValue) {
		return actualValue.isEmpty();
	}


	@Override
	protected String convertValueToString() {
		return mValue;
	}


	@Override
	public ValiFieldBase<String> setEmptyAllowed(boolean isEmptyAllowed) {
		if(mHasNotEmptyValidator) {
			throw new ValiFiValidatorException("Field can't be empty and not empty at the same time");
		}
		return super.setEmptyAllowed(isEmptyAllowed);
	}

	// ------------------ PATTERN VALIDATOR ------------------ //


	/**
	 * @see #addPatternValidator(String, Pattern)
	 */
	public ValiFieldText addPatternValidator(@StringRes int errorResource, final Pattern pattern) {
		String errorMessage = getAppContext().getString(errorResource);
		return addPatternValidator(errorMessage, pattern);
	}


	/**
	 * Validates any pattern specified with parameter
	 *
	 * @param errorMessage specifies error message to be shown
	 * @param pattern      validates this pattern
	 * @return this, so validators can be chained
	 */
	public ValiFieldText addPatternValidator(String errorMessage, final Pattern pattern) {
		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return value != null && pattern.matcher(value).matches();
			}
		});
		return this;
	}


	// ------------------ NOT EMPTY VALIDATOR ------------------ //


	/**
	 * Validates emptiness of the value
	 *
	 * @return this, so validators can be chained
	 */
	public ValiFieldText addNotEmptyValidator() {
		return addNotEmptyValidator(getErrorRes(ValiFi.Builder.ERROR_RES_NOT_EMPTY));
	}


	public ValiFieldText addNotEmptyValidator(@StringRes int errorResource) {
		String errorMessage = getAppContext().getString(errorResource);
		return addNotEmptyValidator(errorMessage);
	}


	/**
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	public ValiFieldText addNotEmptyValidator(String errorMessage) {
		return addMinLengthValidator(errorMessage, 1);
	}


	// ------------------ MIN LENGTH VALIDATOR ------------------ //


	/**
	 * Validates min length of string
	 *
	 * @param minLength must be larger or equal
	 * @return this, so validators can be chained
	 */
	public ValiFieldText addMinLengthValidator(int minLength) {
		return addMinLengthValidator(getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_MIN), minLength);
	}


	public ValiFieldText addMinLengthValidator(@StringRes int errorResource, int minLength) {
		String errorMessage = getAppContext().getString(errorResource, minLength);
		return addMinLengthValidator(errorMessage, minLength);
	}


	/**
	 * @param errorMessage specifies error message to be shown
	 * @param minLength    must be larger or equal
	 * @return this, so validators can be chained
	 */
	public ValiFieldText addMinLengthValidator(String errorMessage, final int minLength) {
		return addRangeLengthValidator(errorMessage, minLength, -1);
	}


	// ------------------ EXACT LENGTH VALIDATOR ------------------ //


	public ValiFieldText addExactLengthValidator(int exactLength) {
		return addExactLengthValidator(getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_EXACT), exactLength);
	}


	public ValiFieldText addExactLengthValidator(@StringRes int errorResource, int exactLength) {
		String errorMessage = getAppContext().getString(errorResource, exactLength);
		return addExactLengthValidator(errorMessage, exactLength);
	}


	// ------------------ MAX LENGTH VALIDATOR ------------------ //


	public ValiFieldText addExactLengthValidator(String errorMessage, final int exactLength) {
		return addRangeLengthValidator(errorMessage, exactLength, exactLength);
	}


	public ValiFieldText addMaxLengthValidator(int maxLength) {
		return addMaxLengthValidator(getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_MAX), maxLength);
	}


	public ValiFieldText addMaxLengthValidator(@StringRes int errorResource, int maxLength) {
		String errorMessage = getAppContext().getString(errorResource, maxLength);
		return addMaxLengthValidator(errorMessage, maxLength);
	}


	public ValiFieldText addMaxLengthValidator(String errorMessage, final int maxLength) {
		return addRangeLengthValidator(errorMessage, 0, maxLength);
	}


	// ------------------ RANGE VALIDATOR ------------------ //


	public ValiFieldText addRangeLengthValidator(int minLength, int maxLength) {
		return addRangeLengthValidator(getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_RANGE), minLength, maxLength);
	}


	public ValiFieldText addRangeLengthValidator(@StringRes int errorResource, int minLength, int maxLength) {
		String errorMessage = getAppContext().getString(errorResource, minLength, maxLength);
		return addRangeLengthValidator(errorMessage, minLength, maxLength);
	}


	/**
	 * Adds validator which checks length of value.
	 *
	 * @param errorMessage shown when not valid
	 * @param minLength    value's length must be greater or equal
	 * @param maxLength    value's length must be lower or equal
	 * @return this, so validators can be chained
	 */
	public ValiFieldText addRangeLengthValidator(String errorMessage, final int minLength, final int maxLength) {
		if(minLength > 0) {
			// checking empty or not empty
			if(mIsEmptyAllowed) {
				throw new ValiFiValidatorException("Field can't be empty and not empty at the same time");
			}
			mHasNotEmptyValidator = true;
		}

		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				int length = value != null ? value.trim().length() : 0;

				if(maxLength == -1) {
					return length >= minLength;
				} else {
					return length >= minLength && length <= maxLength;
				}
			}
		});
		return this;
	}
}