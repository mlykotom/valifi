package com.mlykotom.valifi;

import android.support.annotation.Nullable;

import com.mlykotom.valifi.exceptions.ValiFiException;
import com.mlykotom.valifi.exceptions.ValiFiValidatorException;
import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ValiFieldTextTest {
	private static final String VALIDATOR_EMPTY_MESSAGE = "field can't be empty";
	private ValiFieldText mField;


	@Before
	public void prepareField() {
		mField = new ValiFieldText();
	}


	@After
	public void cleanField() {
		mField.destroy();
	}


	@Test(expected = ValiFiException.class)
	public void checkLibraryInstalled() {
		mField.getAppContext();
	}


	@Test(expected = ValiFiException.class)
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
	public void setGetIsCorrect() {
		mField.set("test_value");
		assertEquals("test_value", mField.get());
		assertTrue(mField.getIsValid());
	}


	@Test(expected = ValiFiValidatorException.class)
	public void checkEmptyOrNotEmpty() {
		mField.setEmptyAllowed(true);
		mField.addNotEmptyValidator(VALIDATOR_EMPTY_MESSAGE);
	}


	@Test(expected = ValiFiValidatorException.class)
	public void checkEmptyOrNotEmptyInverse() {
		mField.addNotEmptyValidator(VALIDATOR_EMPTY_MESSAGE);
		mField.setEmptyAllowed(true);
	}


	@Test
	public void checkNotEmptyWhenNull() {
		mField.addNotEmptyValidator(VALIDATOR_EMPTY_MESSAGE);
		mField.set(null);
		assertFalse(mField.getIsValid());
	}


	@Test
	public void checkNotEmpty() {
		mField.addNotEmptyValidator(VALIDATOR_EMPTY_MESSAGE);
		mField.set("");
		assertFalse(mField.getIsValid());
	}

}
