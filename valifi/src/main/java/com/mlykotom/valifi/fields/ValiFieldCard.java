package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.ValiFiCardType;
import com.mlykotom.valifi.fields.number.ValiFieldLong;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class ValiFieldCard extends ValiFieldLong {
	protected static final int[] POSITION_WEIGHT = new int[]{2, 1};

	public ValiFieldCard() {
		this(null);
	}

	public ValiFieldCard(@Nullable Long defaultValue) {
		super(defaultValue);
		addSpecifiedValidator(getString(getErrorRes(ValiFi.Builder.ERROR_RES_CREDIT_CARD)));
	}

	public ValiFieldCard(@StringRes int errorResource) {
		this(null, errorResource);
	}

	public ValiFieldCard(@Nullable Long defaultValue, @StringRes int errorResource) {
		super(defaultValue);
		addNotEmptyValidator(getString(errorResource));
	}

	public ValiFieldCard(@Nullable Long defaultValue, String errorMessage) {
		super(defaultValue);
		addSpecifiedValidator(errorMessage);
	}

	/**
	 * Checks luhn algorithm for specified credit card
	 *
	 * @param value must be number
	 * @return whether specified card if valid or not
	 */
	protected static boolean isLuhnTestValid(@NonNull String value) {
		int sum = 0;
		for (int i = value.length() - 1; i >= 0; i--) {
			int digit = Character.getNumericValue(value.charAt(i)) * POSITION_WEIGHT[(value.length() - i) % 2];
			sum += digit > 9 ? (digit - 9) : digit;
		}

		return (sum % 10) == 0;
	}

	/**
	 * Goes through known credit card types and tries to match input to their pattern
	 *
	 * @param value actual input
	 * @param knownTypes set of known types
	 * @return true if type found or set is empty
	 * @see ValiFi.Builder#setKnownCardTypes(ValiFiCardType...) for setting more types
	 */
	protected static boolean isCreditCardKnown(String value, @NonNull Set<ValiFiCardType> knownTypes) {
		// if there is no card installed, just skip and say it's valid
		if (knownTypes.isEmpty()) return true;

		// first found, valid
		for (ValiFiCardType valiFiCardType : knownTypes) {
			if (valiFiCardType.pattern.matcher(value).matches()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds default validator for credit cards:
	 * 1) checks if its a number
	 * 2) check Luhn test
	 * 3) check known card type
	 *
	 * @param errorMessage to be shown when error
	 */
	protected void addSpecifiedValidator(String errorMessage) {
		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				if (value == null) return false;

				try {
					// tries to parse a number
					Long cardNumber = parse(value);
					// tries luhn rule
					if (!isLuhnTestValid(value)) return false;
					// validates known credit card types
					return isCreditCardKnown(value, ValiFi.getCreditCardTypes());
				} catch (NumberFormatException ignored) {
					return false;
				}
			}
		});
	}
}