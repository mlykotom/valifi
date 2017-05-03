package com.mlykotom.valifi.fields;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.fields.number.ValiFieldLong;


public class ValiFieldCreditCard extends ValiFieldLong {
	protected static final int[] POSITION_WEIGHT = new int[]{2, 1};


	public ValiFieldCreditCard() {
		this(null);
	}


	public ValiFieldCreditCard(@Nullable Long defaultValue) {
		super(defaultValue);
		addSpecifiedValidator(getAppContext().getString(getErrorRes(ValiFi.Builder.ERROR_RES_CREDIT_CARD)));
	}


	public ValiFieldCreditCard(@StringRes int errorResource) {
		this(null, errorResource);
	}


	public ValiFieldCreditCard(@Nullable Long defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addNotEmptyValidator(getAppContext().getString(errorResource));
	}


	public ValiFieldCreditCard(@Nullable Long defaultValue, String errorMessage) {
		super(defaultValue);
		addSpecifiedValidator(errorMessage);
	}


	/**
	 * Checks luhn algorithm for specified credit card
	 *
	 * @param value must be number
	 * @return whether specified card if valid or not
	 */
	static boolean isLuhnTestValid(@NonNull String value) {
		int sum = 0;
		for(int i = value.length() - 1; i >= 0; i--) {
			int digit = Character.getNumericValue(value.charAt(i)) * POSITION_WEIGHT[(value.length() - i) % 2];
			sum += digit > 9 ? (digit - 9) : digit;
		}

		return (sum % 10) == 0;
	}


	/**
	 * Adds default validator for credit cards
	 *
	 * @param errorMessage to be shown when error
	 */
	protected void addSpecifiedValidator(String errorMessage) {
		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				if(value == null) return false;
				try {
					Long cardNumber = parse(value);
					return isLuhnTestValid(value);
				} catch(NumberFormatException ignored) {
					return false;
				}
			}
		});
	}
}