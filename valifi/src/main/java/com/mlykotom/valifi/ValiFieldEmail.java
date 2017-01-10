package com.mlykotom.valifi;

import android.support.annotation.StringRes;


public class ValiFieldEmail extends ValiFieldText
{
	public ValiFieldEmail()
	{
		super();
		super.addEmailValidator();
	}


	public ValiFieldEmail(String defaultValue)
	{
		super(defaultValue);
		super.addEmailValidator();
	}


	public ValiFieldEmail(@StringRes int errorResource)
	{
		super();
		super.addEmailValidator(errorResource);
	}


	public ValiFieldEmail(@StringRes int errorResource, String defaultValue)
	{
		super(defaultValue);
		super.addEmailValidator(errorResource);
	}


	public ValiFieldEmail(String errorMessage, String defaultValue)
	{
		super(defaultValue);
		super.addEmailValidator(errorMessage);
	}
}
