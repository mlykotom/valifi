package com.mlykotom.mlyked;

import android.support.annotation.StringRes;


public class ValidatedEmailField extends ValidatedTextField
{
	public ValidatedEmailField()
	{
		super();
		super.addEmailValidator();
	}


	public ValidatedEmailField(String defaultValue)
	{
		super(defaultValue);
		super.addEmailValidator();
	}


	public ValidatedEmailField(@StringRes int errorResource)
	{
		super();
		super.addEmailValidator(errorResource);
	}


	public ValidatedEmailField(@StringRes int errorResource, String defaultValue)
	{
		super(defaultValue);
		super.addEmailValidator(errorResource);
	}


	public ValidatedEmailField(String errorMessage, String defaultValue)
	{
		super(defaultValue);
		super.addEmailValidator(errorMessage);
	}
}
