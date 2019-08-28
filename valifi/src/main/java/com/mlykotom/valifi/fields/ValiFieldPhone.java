package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class ValiFieldPhone extends ValiFieldText {
	public ValiFieldPhone() {
		super();
		addPhoneValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PHONE)));
	}

	public ValiFieldPhone(@Nullable String defaultValue) {
		super(defaultValue);
		addPhoneValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PHONE)));
	}

	public ValiFieldPhone(@Nullable String defaultValue, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addPhoneValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_PHONE)));
	}

	public ValiFieldPhone(@StringRes int errorResource) {
		super();
		addPhoneValidator(getString(errorResource));
	}

	public ValiFieldPhone(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addPhoneValidator(getString(errorResource));
	}

	public ValiFieldPhone(@Nullable String defaultValue, @StringRes int errorResource, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addPhoneValidator(getString(errorResource));
	}

	public ValiFieldPhone(@Nullable String defaultValue, String errorMessage) {
		super(defaultValue);
		addPhoneValidator(errorMessage);
	}

	public ValiFieldPhone(@Nullable String defaultValue, String errorMessage, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addPhoneValidator(errorMessage);
	}
}
