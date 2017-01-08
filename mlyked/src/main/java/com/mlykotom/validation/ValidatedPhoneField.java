package com.mlykotom.validation;

import android.support.annotation.StringRes;


public class ValidatedPhoneField extends ValidatedField
{
	public ValidatedPhoneField()
	{
		super();
		super.addPhoneValidator();
	}


	public ValidatedPhoneField(String defaultValue)
	{
		super(defaultValue);
		super.addPhoneValidator();
	}


	public ValidatedPhoneField(@StringRes int errorResource)
	{
		super();
		super.addPhoneValidator(errorResource);
	}


	public ValidatedPhoneField(@StringRes int errorResource, String defaultValue)
	{
		super(defaultValue);
		super.addPhoneValidator(errorResource);
	}


	public ValidatedPhoneField(String errorMessage, String defaultValue)
	{
		super(defaultValue);
		super.addPhoneValidator(errorMessage);
	}
}
