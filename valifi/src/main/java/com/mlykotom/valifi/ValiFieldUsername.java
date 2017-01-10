package com.mlykotom.valifi;

import android.support.annotation.StringRes;


public class ValiFieldUsername extends ValiFieldText
{
	public ValiFieldUsername()
	{
		super();
		super.addUsernameValidator();
	}


	public ValiFieldUsername(String defaultValue)
	{
		super(defaultValue);
		super.addUsernameValidator();
	}


	public ValiFieldUsername(@StringRes int errorResource)
	{
		super();
		super.addUsernameValidator(errorResource);
	}


	public ValiFieldUsername(@StringRes int errorResource, String defaultValue)
	{
		super(defaultValue);
		super.addUsernameValidator(errorResource);
	}


	public ValiFieldUsername(String errorMessage, String defaultValue)
	{
		super(defaultValue);
		super.addUsernameValidator(errorMessage);
	}
}
