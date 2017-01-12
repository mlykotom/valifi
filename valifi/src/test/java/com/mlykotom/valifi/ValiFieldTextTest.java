package com.mlykotom.valifi;

import android.support.annotation.Nullable;

import com.mlykotom.valifi.exceptions.ValiFiException;
import com.mlykotom.valifi.exceptions.ValiFiValidatorException;
import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ValiFieldTextTest {
	private ValiFieldText mField;


	@Before
	public void prepareField() {
		mField = new ValiFieldText();
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
	}


	@Test(expected = ValiFiValidatorException.class)
	public void checkEmptyOrNotEmpty() {
		mField.setEmptyAllowed(true);
		mField.addNotEmptyValidator("field can't be empty");
	}


	@Test(expected = ValiFiValidatorException.class)
	public void checkEmptyOrNotEmptyInverse() {
		mField.addNotEmptyValidator("field can't be empty");
		mField.setEmptyAllowed(true);
	}
}
