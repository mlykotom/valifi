package com.mlykotom.valifi;

import android.support.annotation.StringRes;


public class ValidatedUsernameField extends ValidatedTextField
{
	public ValidatedUsernameField()
	{
		super();
		super.addUsernameValidator();
	}


	public ValidatedUsernameField(String defaultValue)
	{
		super(defaultValue);
		super.addUsernameValidator();
	}


	public ValidatedUsernameField(@StringRes int errorResource)
	{
		super();
		super.addUsernameValidator(errorResource);
	}


	public ValidatedUsernameField(@StringRes int errorResource, String defaultValue)
	{
		super(defaultValue);
		super.addUsernameValidator(errorResource);
	}


	public ValidatedUsernameField(String errorMessage, String defaultValue)
	{
		super(defaultValue);
		super.addUsernameValidator(errorMessage);
	}
}
