package com.mlykotom.valifi.fields;

import android.util.Patterns;

import com.mlykotom.valifi.ValiFi;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


public class ValiFieldEmail extends ValiFieldText {
	public ValiFieldEmail() {
		this(null);
	}


	public ValiFieldEmail(@Nullable String defaultValue) {
		super(defaultValue);
		addEmailValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_EMAIL)));
	}


	public ValiFieldEmail(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldEmail(@Nullable String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addEmailValidator(getString(errorResource));
	}


	public ValiFieldEmail(@Nullable String defaultValue, String errorMessage) {
		super(defaultValue);
		addEmailValidator(errorMessage);
	}

	public ValiFieldEmail(@Nullable String defaultValue, String errorMessage, boolean markAsChanged) {
		super(defaultValue, markAsChanged);
		addEmailValidator(errorMessage);
	}
}