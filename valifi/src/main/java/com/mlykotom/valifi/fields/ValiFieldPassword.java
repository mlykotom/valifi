package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


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

	public ValiFieldPassword(@Nullable String defaultValue, String errorMessage, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addPasswordValidator(errorMessage);
	}
}