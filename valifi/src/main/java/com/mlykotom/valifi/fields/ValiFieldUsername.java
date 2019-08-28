package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class ValiFieldUsername extends ValiFieldText {
	public ValiFieldUsername() {
		super();
		addUsernameValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_USERNAME)));
	}

	public ValiFieldUsername(@Nullable String defaultValue) {
		super(defaultValue);
		addUsernameValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_USERNAME)));
	}

	public ValiFieldUsername(@Nullable String defaultValue, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addUsernameValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_USERNAME)));
	}

	public ValiFieldUsername(@StringRes int errorResource) {
		super();
		addUsernameValidator(getString(errorResource));
	}

	public ValiFieldUsername(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addUsernameValidator(getString(errorResource));
	}

	public ValiFieldUsername(@Nullable String defaultValue, @StringRes int errorResource, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
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
