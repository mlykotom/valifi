package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFieldBaseTest;
import com.mlykotom.valifi.exceptions.ValiFiValidatorException;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class ValiFieldTextTest extends ValiFieldBaseTest {
	private static final String VALIDATOR_EMPTY_MESSAGE = "field can't be empty";


	@Override
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
		assertThat(mField.getIsValid(), is(false));
	}


	@Test
	public void checkBoundFields() {
		ValiFieldText boundField = new ValiFieldText();
		mField.addVerifyFieldValidator("fields must be same", boundField);

		boundField.setValue("val_1");
		mField.setValue("val_2");

		assertThat(mField.getIsValid(), is(false));
	}
}
