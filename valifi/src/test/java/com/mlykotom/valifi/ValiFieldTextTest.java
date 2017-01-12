package com.mlykotom.valifi;

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


	@Test(expected = RuntimeException.class)
	public void t01_checkLibraryInstalledThrown() {
		mField.getAppContext();
	}


	@Test
	public void t02_setGetIsCorrect() {
		mField.set("test_value");
		assertEquals("test_value", mField.get());
	}


	@Test(expected = ValiFiValidatorException.class)
	public void t03_checkEmptyOrNotEmpty() {
		mField.setEmptyAllowed(true);
		mField.addNotEmptyValidator("field can't be empty");
	}

	@Test(expected = ValiFiValidatorException.class)
	public void t04_checkEmptyOrNotEmptyInverse() {
		mField.addNotEmptyValidator("field can't be empty");
		mField.setEmptyAllowed(true);
	}
}
