package com.mlykotom.valifi;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mlykotom.valifi.exceptions.ValiFiException;
import com.mlykotom.valifi.exceptions.ValiFiValidatorException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;

import static androidx.annotation.RestrictTo.Scope.TESTS;

@SuppressWarnings("unused")
public class ValiFi {
	static String TAG = ValiFi.class.getSimpleName();
	@SuppressLint("StaticFieldLeak")
	private static ValiFi ourInstance;
	final ValiFiConfig mParameters;
	private final Context mAppContext;

	/**
	 * @param appContext nullable for tests
	 * @param config configuration of valifi (patterns, errors)
	 */
	private ValiFi(@Nullable Context appContext, @NonNull ValiFiConfig config) {
		mAppContext = appContext;
		mParameters = config;
	}

	/**
	 * Installs validation with specified settings
	 * This should be called in Application's onCreate
	 *
	 * @param appContext for requesting resources, etc.
	 * @param config overriden parameters, built by {@link Builder}
	 */
	public static void install(@NonNull Context appContext, @NonNull ValiFiConfig config) {
		ourInstance = new ValiFi(appContext.getApplicationContext(), config);
	}

	/**
	 * Installs validation with default settings
	 * This should be called in Application's onCreate
	 *
	 * @param appContext for requesting resources, etc.
	 */
	public static void install(@NonNull Context appContext) {
		install(appContext.getApplicationContext(), new Builder().build());
	}

	/**
	 * Installer for tests without context
	 */
	@RestrictTo(TESTS)
	public static void install() {
		ourInstance = new ValiFi(null, new Builder().build());
	}

	/**
	 * Installer for tests without context
	 *
	 * @param config overriden parameters, built by {@link Builder}
	 */
	@RestrictTo(TESTS)
	public static void install(@NonNull ValiFiConfig config) {
		ourInstance = new ValiFi(null, config);
	}

	/**
	 * Helper for destroying all specified fields
	 *
	 * @param fields to be destroyed
	 */
	public static void destroyFields(ValiFiValidable... fields) {
		for (ValiFiValidable field : fields) {
			field.destroy();
		}
	}

	/**
	 * @return installed known card types (MASTERCARD, VISA and AMERICAN_EXPRESS as default)
	 */
	@NonNull
	public static Set<ValiFiCardType> getCreditCardTypes() {
		return getInstance().mParameters.mKnownCardTypes;
	}

	static int getErrorRes(@Builder.ValiFiErrorResource int field) {
		return getInstance().mParameters.mErrorResources[field];
	}

	@NonNull
	static ValiFieldBase.PropertyValidator<String> getValidator(@Builder.ValiFiPattern int field) {
		return getInstance().mParameters.mValidators[field];
	}

	static long getErrorDelay() {
		return getInstance().mParameters.mErrorDelay;
	}

	static long getAsyncValidationDelay() {
		return getInstance().mParameters.mAsyncValidationDelay;
	}

	static ValiFi getInstance() {
		if (ourInstance == null) {
			throw new ValiFiException("ValiFi must be installed in Application.onCreate()!");
		}

		return ourInstance;
	}

	static Context getContext() {
		if (getInstance().mAppContext == null) {
			throw new ValiFiException("ValiFi was installed without Context!");
		}
		return getInstance().mAppContext;
	}

	static String getString(@StringRes int stringRes, Object... formatArgs) {
		@Nullable Context context = getInstance().mAppContext;
		if (context == null) {
			// tests may be initialized without context, so will use placeholder string
			return "string-" + stringRes;
		}
		return context.getString(stringRes, formatArgs);
	}

	/**
	 * Configuration for validation library.
	 * Should be built by {@link Builder}
	 */
	public static class ValiFiConfig {
		@StringRes final int[] mErrorResources;
		final ValiFieldBase.PropertyValidator<String>[] mValidators;
		final long mErrorDelay;
		final long mAsyncValidationDelay;
		final Set<ValiFiCardType> mKnownCardTypes;

		ValiFiConfig(ValiFieldBase.PropertyValidator<String>[] validators, @StringRes int[] errorResources, long errorDelay, long asyncValidationDelay, @NonNull Set<ValiFiCardType> knownCardTypes) {
			mValidators = validators;
			mErrorResources = errorResources;
			mErrorDelay = errorDelay;
			mAsyncValidationDelay = asyncValidationDelay;
			mKnownCardTypes = knownCardTypes;
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
		public static final int ERROR_RES_CREDIT_CARD = 10;
		// ------ COUNT OF PARAMETERS
		public static final int ERROR_RES_COUNT = ERROR_RES_CREDIT_CARD + 1;
		// ----- Patterns
		public static final int PATTERN_EMAIL = 0;
		public static final int PATTERN_PHONE = 1;
		public static final int PATTERN_PASSWORD = 2;
		public static final int PATTERN_USERNAME = 3;
		// ------ COUNT OF PARAMETERS
		public static final int PATTERN_COUNT = PATTERN_USERNAME + 1;
		// ----- other
		private static final long DEFAULT_ERROR_DELAY_MILLIS = 500;
		private static final long DEFAULT_ASYNC_VALIDATION_DELAY_MILLIS = 300;
		private ValiFieldBase.PropertyValidator<String>[] mValidators;
		private int[] mErrorResources;
		private Set<ValiFiCardType> mKnownCardTypes;
		private long mErrorDelay = DEFAULT_ERROR_DELAY_MILLIS;
		private long mAsyncValidationDelay = DEFAULT_ASYNC_VALIDATION_DELAY_MILLIS;

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
				ERROR_RES_CREDIT_CARD
		})
		@Retention(RetentionPolicy.SOURCE)
		public @interface ValiFiErrorResource {}

		@IntDef({
				PATTERN_EMAIL,
				PATTERN_PHONE,
				PATTERN_PASSWORD,
				PATTERN_USERNAME
		})
		@Retention(RetentionPolicy.SOURCE)
		public @interface ValiFiPattern {}

		public Builder() {
			//noinspection unchecked
			mValidators = new ValiFieldBase.PropertyValidator[PATTERN_COUNT];
			mErrorResources = new int[ERROR_RES_COUNT];
			mKnownCardTypes = ValiFiCardType.getDefaultTypes();

			setupResources();
			setupPatterns();
		}

		/**
		 * You may override any resource when specifying string resource for it
		 *
		 * @param field one of error resources in library {@link ValiFiErrorResource}
		 * @param value string resource used as default. Some errors may require PARAMETERS
		 * @return builder for chaining
		 */
		public Builder setErrorResource(@ValiFiErrorResource int field, @StringRes int value) {
			mErrorResources[field] = value;
			return this;
		}

		/**
		 * You may override any pattern with specific validator
		 *
		 * @param field one of patterns in library {@link ValiFiPattern}
		 * @param validator Generic validation (global) in your app. If you need to specify custom validation,
		 * check {@link ValiFieldBase#addCustomValidator(ValiFieldBase.PropertyValidator)}
		 * @return builder for chaining
		 */
		public Builder setValidator(@ValiFiPattern int field, ValiFieldBase.PropertyValidator<String> validator) {
			mValidators[field] = validator;
			return this;
		}

		/**
		 * You may override any pattern when specifying pattern for it
		 *
		 * @param field one of patterns in library {@link ValiFiPattern}
		 * @param pattern compiled pattern used as default
		 * @return builder for chaining
		 */
		public Builder setPattern(@ValiFiPattern int field, final Pattern pattern) {
			return setValidator(field, new ValiFieldBase.PropertyValidator<String>() {
				@Override
				public boolean isValid(@Nullable String value) {
					return value != null && pattern.matcher(value).matches();
				}
			});
		}

		/**
		 * Setups error delay for either never or immediate
		 * When set, it will be used in all fields by default (if some field does not override it)
		 *
		 * @param delayType either never or immediate
		 * @return builder for chaining
		 * @see #setErrorDelay(long)  if you want to set exact time
		 */
		public Builder setErrorDelay(ValiFiErrorDelay delayType) {
			mErrorDelay = delayType.delayMillis;
			return this;
		}

		/**
		 * Setups error delay (default is {@link #DEFAULT_ERROR_DELAY_MILLIS}).
		 * When set, it will be used in all fields by default (if some field does not override it)
		 *
		 * @param millis how long till error will be shown.
		 * @return builder for chaining
		 * @see #setErrorDelay(ValiFiErrorDelay) for immediate or manual mode
		 */
		public Builder setErrorDelay(long millis) {
			if (millis <= 0) {
				throw new ValiFiValidatorException("Error delay must be positive");
			}
			mErrorDelay = millis;
			return this;
		}

		/**
		 * Asynchronous validation for all fields will start after the delay specified here.
		 * Default value is {@link #DEFAULT_ASYNC_VALIDATION_DELAY_MILLIS}
		 *
		 * @param millis can be milliseconds 0+
		 * @return builder for chaining
		 * @see ValiFieldBase#setAsyncValidationDelay(long) for overriding for specified field
		 */
		public Builder setAsyncValidationDelay(long millis) {
			if (millis < 0) {
				throw new ValiFiValidatorException("Asynchronous delay must be positive or immediate");
			}

			mAsyncValidationDelay = millis;
			return this;
		}

		/**
		 * Clears known card types and sets new types
		 *
		 * @param types to be set (will clear previously set). If @null, only clears the types
		 * @return builder for chaining
		 */
		public Builder setKnownCardTypes(@Nullable ValiFiCardType... types) {
			mKnownCardTypes = new HashSet<>();
			if (types != null) {
				Collections.addAll(mKnownCardTypes, types);
			}
			return this;
		}

		public ValiFiConfig build() {
			return new ValiFiConfig(mValidators, mErrorResources, mErrorDelay, mAsyncValidationDelay, mKnownCardTypes);
		}

		private void setupPatterns() {
			// NOTE: same as Patterns.EMAIL_ADDRESS but unit tests return null
			setPattern(PATTERN_EMAIL, Pattern.compile("[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"));
			setPattern(PATTERN_PHONE, Pattern.compile("^\\+420 ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$" + "|" + "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$"));            // phone czech | phone en-US
			setPattern(PATTERN_USERNAME, Pattern.compile(".{4,}"));
			setPattern(PATTERN_PASSWORD, Pattern.compile(".{8,}"));
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
			mErrorResources[ERROR_RES_CREDIT_CARD] = R.string.validation_error_credit_card;
		}
	}
}
