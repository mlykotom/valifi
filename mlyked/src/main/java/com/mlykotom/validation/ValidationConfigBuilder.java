package com.mlykotom.validation;

import android.support.annotation.StringRes;

import java.util.regex.Pattern;

import static com.mlykotom.validation.ValidationConfig.ERROR_RES_COUNT;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_EMAIL;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_LENGTH_EXACT;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_LENGTH_MAX;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_LENGTH_MIN;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_LENGTH_RANGE;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_NOT_EMPTY;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_PASSWORD;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_PHONE;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_USERNAME;
import static com.mlykotom.validation.ValidationConfig.ERROR_RES_YEARS_OLDER_THAN;
import static com.mlykotom.validation.ValidationConfig.PATTERN_COUNT;
import static com.mlykotom.validation.ValidationConfig.PATTERN_EMAIL;
import static com.mlykotom.validation.ValidationConfig.PATTERN_PASSWORD;
import static com.mlykotom.validation.ValidationConfig.PATTERN_PHONE;
import static com.mlykotom.validation.ValidationConfig.PATTERN_USERNAME;


public class ValidationConfigBuilder
{
	private Pattern[] mPatterns;
	private int[] mErrorResources;


	ValidationConfigBuilder()
	{
		mPatterns = new Pattern[PATTERN_COUNT];
		mErrorResources = new int[ERROR_RES_COUNT];

		setupResources();
		setupPatterns();
	}


	/**
	 * TODO DOC
	 *
	 * @param field
	 * @param value
	 * @return
	 */
	public ValidationConfigBuilder setErrorResource(@ValidationConfig.ValidationErrorResource int field, @StringRes int value)
	{
		mErrorResources[field] = value;
		return this;
	}


	/**
	 * TODO DOC
	 *
	 * @param field
	 * @param value
	 * @return
	 */
	public ValidationConfigBuilder setPattern(@ValidationConfig.ValidationPattern int field, Pattern value)
	{
		mPatterns[field] = value;
		return this;
	}


	public ValidationParams build()
	{
		return new ValidationParams(mPatterns, mErrorResources);
	}


	private void setupPatterns()
	{
		mPatterns[PATTERN_EMAIL] = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		mPatterns[PATTERN_PHONE] = Pattern.compile("^\\+420 ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$" + "|" + "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$");            // phone czech | phon en-US
		mPatterns[PATTERN_USERNAME] = Pattern.compile(".{4,}");
		mPatterns[PATTERN_PASSWORD] = Pattern.compile(".{8,}");
	}


	private void setupResources()
	{
		mErrorResources[ERROR_RES_NOT_EMPTY] = R.string.validation_error_empty;
		mErrorResources[ERROR_RES_LENGTH_MIN] = R.string.validation_error_min_length;
		mErrorResources[ERROR_RES_LENGTH_MAX] = R.string.validation_error_max_length;
		mErrorResources[ERROR_RES_LENGTH_RANGE] = R.string.validation_error_range_length;
		mErrorResources[ERROR_RES_LENGTH_EXACT] = R.string.validation_error_exact_length;
		mErrorResources[ERROR_RES_EMAIL] = R.string.validation_error_email;
		mErrorResources[ERROR_RES_PHONE] = R.string.validation_error_phone;
		mErrorResources[ERROR_RES_USERNAME] = R.string.validation_error_username;
		mErrorResources[ERROR_RES_PASSWORD] = R.string.validation_error_password;
		mErrorResources[ERROR_RES_YEARS_OLDER_THAN] = R.string.validation_error_older_than_years;
	}
}