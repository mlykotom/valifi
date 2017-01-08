package com.mlykotom.mlyked;

import android.support.annotation.StringRes;


public class ValidatedPasswordField extends ValidatedTextField {
	public ValidatedPasswordField() {
		super();
		super.addPasswordValidator();
	}


	public ValidatedPasswordField(String defaultValue) {
		super(defaultValue);
		super.addPasswordValidator();
	}


	public ValidatedPasswordField(@StringRes int errorResource) {
		super();
		super.addPasswordValidator(errorResource);
	}


	public ValidatedPasswordField(@StringRes int errorResource, String defaultValue) {
		super(defaultValue);
		super.addPasswordValidator(errorResource);
	}


	public ValidatedPasswordField(String errorMessage, String defaultValue) {
		super(defaultValue);
		super.addPasswordValidator(errorMessage);
	}


}
