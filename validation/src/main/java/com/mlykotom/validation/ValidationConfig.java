package com.mlykotom.validation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;


public class ValidationConfig {
	// ----- Error Resources
	public static final int ERROR_RES_NOT_EMPTY = 0;
	public static final int ERROR_RES_LENGTH_MIN = 1;
	public static final int ERROR_RES_LENGTH_MAX = 2;
	public static final int ERROR_RES_LENGTH_RANGE = 3;
	public static final int ERROR_RES_LENGTH_EXACT = 4;
	public static final int ERROR_RES_EMAIL = 5;
	public static final int ERROR_RES_PHONE = 6;
	public static final int ERROR_RES_USERNAME = 7;
	public static final int ERROR_RES_PASSWORD = 8;
	public static final int ERROR_RES_YEARS_OLDER_THAN = 9;
	// ----- Patterns
	public static final int PATTERN_EMAIL = 0;
	public static final int PATTERN_PHONE = 1;
	public static final int PATTERN_PASSWORD = 2;
	public static final int PATTERN_USERNAME = 3;
	// ------ COUNT OF PARAMETERS
	public static final int ERROR_RES_COUNT = ERROR_RES_YEARS_OLDER_THAN + 1;
	public static final int PATTERN_COUNT = PATTERN_USERNAME + 1;

	@SuppressLint("StaticFieldLeak")
	private static ValidationConfig ourInstance;
	private final Context mAppContext;
	private final ValidationParams mParameters;


	@IntDef({
			ERROR_RES_NOT_EMPTY,
			ERROR_RES_LENGTH_MIN,
			ERROR_RES_LENGTH_MAX,
			ERROR_RES_LENGTH_RANGE,
			ERROR_RES_LENGTH_EXACT,
			ERROR_RES_EMAIL,
			ERROR_RES_PHONE,
			ERROR_RES_USERNAME,
			ERROR_RES_PASSWORD,
			ERROR_RES_YEARS_OLDER_THAN,
	})
	@Retention(RetentionPolicy.SOURCE)
	@interface ValidationErrorResource {}


	@IntDef({
			PATTERN_EMAIL,
			PATTERN_PHONE,
			PATTERN_PASSWORD,
			PATTERN_USERNAME
	})
	@Retention(RetentionPolicy.SOURCE)
	@interface ValidationPattern {}


	private ValidationConfig(Context appContext, @NonNull ValidationParams parameters) {
		mAppContext = appContext;
		mParameters = parameters;
	}


	/**
	 * Installs validation with specified settings
	 * This should be called in Application's onCreate
	 *
	 * @param appContext for requesting resources, etc.
	 * @param parameters overriden settings for validation
	 */
	public static void install(Application appContext, ValidationParams parameters) {
		ourInstance = new ValidationConfig(appContext, parameters);
	}


	/**
	 * Installs validation with default settings
	 * This should be called in Application's onCreate
	 *
	 * @param appContext for requesting resources, etc.
	 */
	public static void install(Application appContext) {
		install(appContext, newConfigBuilder().build());
	}


	/**
	 * Creates new builder so that it's possible to override settings
	 *
	 * @return builder with fluent api to override settings
	 */
	@NonNull
	public static ValidationConfigBuilder newConfigBuilder() {
		return new ValidationConfigBuilder();
	}


	// ------ Might be used for ValidatedField


	static int getErrorRes(@ValidationErrorResource int field) {
		return getInstance().mParameters.mErrorResources[field];
	}


	static Pattern getPattern(@ValidationPattern int field) {
		return getInstance().mParameters.mPatterns[field];
	}


	static Context getContext() {
		return getInstance().mAppContext;
	}


	private static ValidationConfig getInstance() {
		if(ourInstance == null) {
			throw new RuntimeException("ValidationConfig must be installed in Application.onCreate()!");
		}

		return ourInstance;
	}
}
