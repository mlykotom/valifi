package com.mlykotom.valifi.fields;

import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;


public class ValiFieldUsername extends ValiFieldText {
	public ValiFieldUsername() {
		this(null);
	}


	public ValiFieldUsername(String defaultValue) {
		super(defaultValue);
		addUsernameValidator(getAppContext().getString(getErrorRes(ValiFi.Builder.ERROR_RES_USERNAME)));
	}


	public ValiFieldUsername(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldUsername(String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addUsernameValidator(getAppContext().getString(errorResource));
	}


	public ValiFieldUsername(String defaultValue, String errorMessage) {
		super(defaultValue);
		addUsernameValidator(errorMessage);
	}


	/**
	 * Validates username based on default pattern for username
	 *
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	protected ValiFieldText addUsernameValidator(String errorMessage) {
		addPatternValidator(errorMessage, getPattern(ValiFi.Builder.PATTERN_USERNAME));
		return this;
	}
}
