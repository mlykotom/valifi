package com.mlykotom.valifi.fields;

import android.support.annotation.StringRes;


public class ValiFieldPassword extends ValiFieldText {
	public ValiFieldPassword() {
		super();
		super.addPasswordValidator();
	}


	public ValiFieldPassword(String defaultValue) {
		super(defaultValue);
		super.addPasswordValidator();
	}


	public ValiFieldPassword(@StringRes int errorResource) {
		super();
		super.addPasswordValidator(errorResource);
	}


	public ValiFieldPassword(@StringRes int errorResource, String defaultValue) {
		super(defaultValue);
		super.addPasswordValidator(errorResource);
	}


	public ValiFieldPassword(String errorMessage, String defaultValue) {
		super(defaultValue);
		super.addPasswordValidator(errorMessage);
	}


}
