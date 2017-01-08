package com.mlykotom.validation;

import android.support.annotation.StringRes;

import java.util.regex.Pattern;


public class ValidationParams
{
	@StringRes final int[] mErrorResources;
	final Pattern mPatterns[];


	ValidationParams(Pattern[] patterns, @StringRes int[] errorResources)
	{
		mPatterns = patterns;
		mErrorResources = errorResources;
	}
}
