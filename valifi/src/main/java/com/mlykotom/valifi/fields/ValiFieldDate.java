package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.ValiFieldBase;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * // TODO BETA!!
 */
public class ValiFieldDate extends ValiFieldBase<Calendar> {
	public ValiFieldDate() {
		super();
	}

	/**
	 * Checking for specific type if value is empty.
	 * Used for checking if empty is allowed.
	 *
	 * @param actualValue value when checking
	 * @return true when value is empty, false when values is not empty (e.g for String, use isEmpty())
	 * @see #mCallback
	 */
	@Override
	protected boolean whenThisFieldIsEmpty(@NonNull Calendar actualValue) {
		return !actualValue.isSet(Calendar.YEAR);    // TODO maybe check other values
	}

	@Override
	protected String convertValueToString(@NonNull Calendar value) {
		return DateFormat.getDateInstance().format(value.getTime());
	}

	@Override
	@Nullable
	protected Calendar convertStringToValue(@Nullable String value) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(DateFormat.getDateInstance().parse(value));
		} catch (ParseException e) {
			// TODO solve
			e.printStackTrace();
		}

		return calendar;
	}

	// ------------------ OLDER THAN VALIDATOR ------------------ //

	public ValiFieldDate addOlderThanYearsValidator(int amount) {
		return addOlderThanValidator(getErrorRes(ValiFi.Builder.ERROR_RES_YEARS_OLDER_THAN), Calendar.YEAR, amount);
	}

	public ValiFieldDate addOlderThanValidator(@StringRes int errorResource, int calendarField, int amount) {
		String errorMessage = getString(errorResource, amount);
		return addOlderThanValidator(errorMessage, calendarField, amount);
	}

	public ValiFieldDate addOlderThanValidator(String errorMessage, int calendarField, int amount) {
		final Calendar wantedDate = Calendar.getInstance();
		wantedDate.add(calendarField, -amount);

		addCustomValidator(errorMessage, new PropertyValidator<Calendar>() {
			@Override
			public boolean isValid(@Nullable Calendar value) {
				return value != null && value.compareTo(wantedDate) < 0;
			}
		});

		return this;
	}
}
