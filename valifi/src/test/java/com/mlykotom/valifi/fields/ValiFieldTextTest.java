package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.exceptions.ValiFiValidatorException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class ValiFieldTextTest {
	private static final String VALIDATOR_EMPTY_MESSAGE = "field can't be empty";
	private ValiFieldText mField;


	@Before
	public void prepareField() {
		mField = new ValiFieldText();
	}


	@After
	public void destroyField() {
		mField.destroy();
		// check if field is still valid
		assertThat(mField.getIsValid(), is(false));
		// check bound values
//		assertThat(mField.mBoundFields, is(nullValue()));	// TODO we'd like to handle that
		// check if has form
		assertThat(mField.getBoundForm(), is(nullValue()));

		// TODO check if we can do something with the field (we should not)
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


	@Test
	public void checkMinLengthInvalid() {
		mField.set("123");
		mField.addMinLengthValidator("must be longer than 4 characters", 4);
		assertThat(mField.getIsValid(), is(false));
	}


	@Test
	public void checkMinLengthValid() {
		mField.set("1234");
		mField.addMinLengthValidator("must be longer than 4 characters", 4);
		assertThat(mField.getIsValid(), is(true));
	}


	@Test
	public void checkRangeLengthMin4Max6Invalid7() {
		mField.set("1234567");
		mField.addRangeLengthValidator("must be between 4 and 6", 4, 6);
		assertThat(mField.getIsValid(), is(false));
	}


	@Test
	public void checkRangeLengthMin4Max6Valid5() {
		mField.set("12345");
		mField.addRangeLengthValidator("must be between 4 and 6", 4, 6);
		assertThat(mField.getIsValid(), is(true));
	}
}
