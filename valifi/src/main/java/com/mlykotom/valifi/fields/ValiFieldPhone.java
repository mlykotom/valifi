package com.mlykotom.valifi.fields;

import android.support.annotation.StringRes;


public class ValiFieldPhone extends ValiFieldText
{
	public ValiFieldPhone()
	{
		super();
		super.addPhoneValidator();
	}


	public ValiFieldPhone(String defaultValue)
	{
		super(defaultValue);
		super.addPhoneValidator();
	}


	public ValiFieldPhone(@StringRes int errorResource)
	{
		super();
		super.addPhoneValidator(errorResource);
	}


	public ValiFieldPhone(@StringRes int errorResource, String defaultValue)
	{
		super(defaultValue);
		super.addPhoneValidator(errorResource);
	}


	public ValiFieldPhone(String errorMessage, String defaultValue)
	{
		super(defaultValue);
		super.addPhoneValidator(errorMessage);
	}
}
