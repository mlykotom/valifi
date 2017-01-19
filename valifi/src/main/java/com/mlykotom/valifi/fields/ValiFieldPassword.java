package com.mlykotom.valifi.fields;

import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;


public class ValiFieldPassword extends ValiFieldText {
	public ValiFieldPassword() {
		this(null);
	}


	public ValiFieldPassword(String defaultValue) {
		super(defaultValue);
		addPasswordValidator(getAppContext().getString(getErrorRes(ValiFi.Builder.ERROR_RES_PASSWORD)));
	}


	public ValiFieldPassword(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldPassword(String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addPasswordValidator(getAppContext().getString(errorResource));
	}


	public ValiFieldPassword(String defaultValue, String errorMessage) {
		super(defaultValue);
		addPasswordValidator(errorMessage);
	}


	/**
	 * Validates password based on default pattern for password
	 *
	 * @param errorMessage to show when field not valid
	 * @return this, so validators can be chained
	 */
	protected ValiFieldPassword addPasswordValidator(String errorMessage) {
		addPatternValidator(errorMessage, getPattern(ValiFi.Builder.PATTERN_PASSWORD));
		return this;
	}
}