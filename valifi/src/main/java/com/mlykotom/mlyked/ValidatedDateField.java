package com.mlykotom.mlyked;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.text.DateFormat;
import java.util.Calendar;


public class ValidatedDateField extends ValidatedBaseField<Calendar> {
	public ValidatedDateField() {
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
	protected String convertValueToString() {
		if(mValue == null) return null;
		return DateFormat.getDateInstance().format(mValue.getTime());
	}

	// ------------------ OLDER THAN VALIDATOR ------------------ //


	public ValidatedDateField addOlderThanYearsValidator(int amount) {
		return addOlderThanValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_YEARS_OLDER_THAN), Calendar.YEAR, amount);
	}


	public ValidatedDateField addOlderThanValidator(@StringRes int errorResource, int calendarField, int amount) {
		String errorMessage = Mlyked.getContext().getString(errorResource, amount);
		return addOlderThanValidator(errorMessage, calendarField, amount);
	}


	public ValidatedDateField addOlderThanValidator(String errorMessage, int calendarField, int amount) {
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
