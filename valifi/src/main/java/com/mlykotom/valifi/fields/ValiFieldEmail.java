package com.mlykotom.valifi.fields;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Patterns;

import com.mlykotom.valifi.ValiFi;


public class ValiFieldEmail extends ValiFieldText {
	public ValiFieldEmail() {
		this(null);
	}


	public ValiFieldEmail(String defaultValue) {
		super(defaultValue);
		addEmailValidator(getAppContext().getString(getErrorRes(ValiFi.Builder.ERROR_RES_EMAIL)));
	}


	public ValiFieldEmail(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldEmail(String defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addEmailValidator(getAppContext().getString(errorResource));
	}


	public ValiFieldEmail(String defaultValue, String errorMessage) {
		super(defaultValue);
		addEmailValidator(errorMessage);
	}


	/**
	 * Validates email addresses based on Android's {@link Patterns#EMAIL_ADDRESS}
	 *
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	protected ValiFieldText addEmailValidator(String errorMessage) {
		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return value != null && getPattern(ValiFi.Builder.PATTERN_EMAIL).matcher(value).matches();
			}
		});
		return this;
	}
}
