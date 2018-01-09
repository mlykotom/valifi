package com.mlykotom.valifi.fields;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;


public class ValiFieldPassword extends ValiFieldText {
	public ValiFieldPassword() {
		this(null);
	}


	public ValiFieldPassword(@Nullable String defaultValue) {
		super(defaultValue);
		addPasswordValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PASSWORD)));
	}


	public ValiFieldPassword(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldPassword(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addPasswordValidator(getString(errorResource));
	}


	public ValiFieldPassword(@Nullable String defaultValue, String errorMessage) {
		super(defaultValue);
		addPasswordValidator(errorMessage);
	}


	/**
	 * Validates password based on default pattern for password
	 *
	 * @param errorMessage to show when field not valid
	 * @return this, so validators can be chained
	 */
	@NonNull
	protected ValiFieldPassword addPasswordValidator(String errorMessage) {
		addPatternValidator(errorMessage, getPattern(ValiFi.Builder.PATTERN_PASSWORD));
		return this;
	}
}