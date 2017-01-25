package com.mlykotom.valifi;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import com.mlykotom.valifi.exceptions.ValiFiException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;


@SuppressWarnings("unused")
public class ValiFi {
	static String TAG = ValiFi.class.getSimpleName();
	@SuppressLint("StaticFieldLeak")
	private static ValiFi ourInstance;
	private final Context mAppContext;
	private final ValiFiConfig mParameters;


	private ValiFi(Context appContext, ValiFiConfig config) {
		mAppContext = appContext;
		mParameters = config;
	}


	/**
	 * Installs validation with specified settings
	 * This should be called in Application's onCreate
	 *
	 * @param appContext for requesting resources, etc.
	 * @param config     overriden parameters, built by {@link Builder}
	 */
	public static void install(Application appContext, ValiFiConfig config) {
		ourInstance = new ValiFi(appContext, config);
	}


	/**
	 * Installs validation with default settings
	 * This should be called in Application's onCreate
	 *
	 * @param appContext for requesting resources, etc.
	 */
	public static void install(Application appContext) {
		install(appContext, new Builder().build());
	}


	static int getErrorRes(@Builder.ValiFiErrorResource int field) {
		return getInstance().mParameters.mErrorResources[field];
	}


	static Pattern getPattern(@Builder.ValiFiPattern int field) {
		return getInstance().mParameters.mPatterns[field];
	}


	static long getErrorDelay() {
		return getInstance().mParameters.mErrorDelay;
	}


	static Context getContext() {
		if(getInstance().mAppContext == null) {
			throw new ValiFiException("ValiFi was installed without Context!");
		}
		return getInstance().mAppContext;
	}


	private static ValiFi getInstance() {
		if(ourInstance == null) {
			throw new ValiFiException("ValiFi must be installed in Application.onCreate()!");
		}

		return ourInstance;
	}


	/**
	 * Configuration for validation library.
	 * Should be built by {@link Builder}
	 */
	public static class ValiFiConfig {
		@StringRes final int[] mErrorResources;
		final Pattern mPatterns[];
		final long mErrorDelay;


		ValiFiConfig(Pattern[] patterns, @StringRes int[] errorResources, long errorDelay) {
			mPatterns = patterns;
			mErrorResources = errorResources;
			mErrorDelay = errorDelay;
		}
	}


	/**
	 * Builder for overriding error resources or patterns for default validation
	 *
	 * @see #setErrorResource(int, int)
	 * @see #setPattern(int, Pattern)
	 */
	public static class Builder {
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
		// ------ COUNT OF PARAMETERS
		public static final int ERROR_RES_COUNT = ERROR_RES_YEARS_OLDER_THAN + 1;
		// ----- Patterns
		public static final int PATTERN_EMAIL = 0;
		public static final int PATTERN_PHONE = 1;
		public static final int PATTERN_PASSWORD = 2;
		public static final int PATTERN_USERNAME = 3;
		public static final int PATTERN_COUNT = PATTERN_USERNAME + 1;
		// ----- other
		private static final long DEFAULT_ERROR_DELAY_MILLIS = 500;

		private Pattern[] mPatterns;
		private int[] mErrorResources;
		private long mErrorDelay = DEFAULT_ERROR_DELAY_MILLIS;


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
		@interface ValiFiErrorResource {}


		@IntDef({
				PATTERN_EMAIL,
				PATTERN_PHONE,
				PATTERN_PASSWORD,
				PATTERN_USERNAME
		})
		@Retention(RetentionPolicy.SOURCE)
		@interface ValiFiPattern {}


		public Builder() {
			mPatterns = new Pattern[PATTERN_COUNT];
			mErrorResources = new int[ERROR_RES_COUNT];

			setupResources();
			setupPatterns();
		}


		/**
		 * You may override any resource when specifying string resource for it
		 *
		 * @param field one of error resources in library {@link ValiFiErrorResource}
		 * @param value string resource used as default.
		 *              Some errors may require parameters in string)
		 * @return this
		 */
		public Builder setErrorResource(@ValiFiErrorResource int field, @StringRes int value) {
			mErrorResources[field] = value;
			return this;
		}


		/**
		 * You may override any pattern when specifying pattern for it
		 *
		 * @param field one of patterns in library {@link ValiFiPattern}
		 * @param value compiled pattern used as default
		 * @return this
		 */
		public Builder setPattern(@ValiFiPattern int field, Pattern value) {
			mPatterns[field] = value;
			return this;
		}


		public Builder setErrorDelay(long millis) {
			mErrorDelay = millis;
			return this;
		}


		public ValiFiConfig build() {
			return new ValiFiConfig(mPatterns, mErrorResources, mErrorDelay);
		}


		private void setupPatterns() {
			// TODO maybe use Patterns#EMAIL_ADDRESS ?
			mPatterns[PATTERN_EMAIL] = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			mPatterns[PATTERN_PHONE] = Pattern.compile("^\\+420 ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$" + "|" + "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$");            // phone czech | phone en-US
			mPatterns[PATTERN_USERNAME] = Pattern.compile(".{4,}");
			mPatterns[PATTERN_PASSWORD] = Pattern.compile(".{8,}");
		}


		private void setupResources() {
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
}
