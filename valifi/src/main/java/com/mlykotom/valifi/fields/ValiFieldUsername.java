package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


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

	public ValiFieldUsername(@Nullable String defaultValue, String errorMessage, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addUsernameValidator(errorMessage);
	}
}
