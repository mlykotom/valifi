package com.mlykotom.valifi;

import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.Before;
import org.junit.Test;

import androidx.annotation.Nullable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValiFiTest {
	public static final String FIELD_ERROR_MSG = "field is not valid";
	private ValiFieldBase<String> mField;

	/**
	 * WARNING: installing without context will crash when getting string, only for tests!
	 */
	public static void installWithoutContext() {
		ValiFi.install();
	}

	@Before
	public void prepareField() {
		installWithoutContext();
		mField = new ValiFieldText();
	}

	@Test
	public void checkLibraryInstalled() {
		assertThat(ValiFi.getInstance(), notNullValue());
	}

	@Test
	public void checkLibraryInstalledByAddingValidator() {
		mField.addCustomValidator(R.string.validation_error_email, new ValiFieldBase.PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				// it doesn't matter if it's valid, checking installation
				return false;
			}
		});
	}

	@Test
	public void checkErrorResourcesMatchLength() {
		int errorResArrCount = ValiFi.getInstance().mParameters.mErrorResources.length;
		assertThat(errorResArrCount, is(ValiFi.Builder.ERROR_RES_COUNT));
	}

	@Test
	public void checkPatternsMatchLength() {
		int patternsArrCount = ValiFi.getInstance().mParameters.mValidators.length;
		assertThat(patternsArrCount, is(ValiFi.Builder.PATTERN_COUNT));
	}

	@Test
	public void checkErrorResourcesMathStrings() {
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_NOT_EMPTY), is(R.string.validation_error_empty));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_MIN), is(R.string.validation_error_min_length));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_MAX), is(R.string.validation_error_max_length));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_RANGE), is(R.string.validation_error_range_length));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_LENGTH_EXACT), is(R.string.validation_error_exact_length));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_EMAIL), is(R.string.validation_error_email));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_PHONE), is(R.string.validation_error_phone));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_USERNAME), is(R.string.validation_error_username));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_PASSWORD), is(R.string.validation_error_password));
		assertThat(mField.getErrorRes(ValiFi.Builder.ERROR_RES_YEARS_OLDER_THAN), is(R.string.validation_error_older_than_years));
	}
}
