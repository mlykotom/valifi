package com.mlykotom.valifi.fields;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;


public class ValiFieldUsername extends ValiFieldText {
	public ValiFieldUsername() {
		this(null);
	}


	public ValiFieldUsername(@Nullable String defaultValue) {
		super(defaultValue);
		addUsernameValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_USERNAME)));
	}


	public ValiFieldUsername(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldUsername(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addUsernameValidator(getString(errorResource));
	}


	public ValiFieldUsername(@Nullable String defaultValue, String errorMessage) {
		super(defaultValue);
		addUsernameValidator(errorMessage);
	}


	/**
	 * Validates username based on default pattern for username
	 *
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	@NonNull
	protected ValiFieldText addUsernameValidator(String errorMessage) {
		addPatternValidator(errorMessage, getPattern(ValiFi.Builder.PATTERN_USERNAME));
		return this;
	}
}
