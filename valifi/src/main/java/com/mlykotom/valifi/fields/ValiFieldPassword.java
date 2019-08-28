package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class ValiFieldPassword extends ValiFieldText {
	public ValiFieldPassword() {
		super();
		addPasswordValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PASSWORD)));
	}

	public ValiFieldPassword(@Nullable String defaultValue) {
		super(defaultValue);
		addPasswordValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PASSWORD)));
	}

	public ValiFieldPassword(@Nullable String defaultValue, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addPasswordValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PASSWORD)));
	}

	public ValiFieldPassword(@StringRes int errorResource) {
		super();
		addPasswordValidator(getString(errorResource));
	}

	public ValiFieldPassword(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addPasswordValidator(getString(errorResource));
	}

	public ValiFieldPassword(@Nullable String defaultValue, @StringRes int errorResource, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
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