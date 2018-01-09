package com.mlykotom.valifi.fields;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;


public class ValiFieldPhone extends ValiFieldText {
	public ValiFieldPhone() {
		this(null);
	}


	public ValiFieldPhone(@Nullable String defaultValue) {
		super(defaultValue);
		addPhoneValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PHONE)));
	}


	public ValiFieldPhone(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldPhone(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addPhoneValidator(getString(errorResource));
	}


	public ValiFieldPhone(@Nullable String defaultValue, String errorMessage) {
		super(defaultValue);
		addPhoneValidator(errorMessage);
	}


	/**
	 * Validates US or Czech phone numbers
	 *
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	protected ValiFieldText addPhoneValidator(String errorMessage) {
		addPatternValidator(errorMessage, getPattern(ValiFi.Builder.PATTERN_PHONE));
		return this;
	}
}
