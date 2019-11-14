package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFiTest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValiFieldEmailTest {
	public static final String EMAIL_ADDRESS_VALID = "test@email.com";
	public static final String EMAIL_ADDRESS_INVALID = "this_is_not_ok";

	@Before
	public void installWithoutContext() {
		ValiFiTest.installWithoutContext();
	}

	@Test
	public void checkConstructWithDefaultValidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(EMAIL_ADDRESS_VALID, ValiFiTest.FIELD_ERROR_MSG);
		assertThat(field.get(), is(EMAIL_ADDRESS_VALID));
		assertThat(field.isValid(), is(true));
	}

	@Test
	public void checkConstructWithValidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(null, ValiFiTest.FIELD_ERROR_MSG);
		field.set(EMAIL_ADDRESS_VALID);
		assertThat(field.get(), is(EMAIL_ADDRESS_VALID));
		assertThat(field.isValid(), is(true));
	}

	@Test
	public void checkConstructWithDefaultInvalidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(EMAIL_ADDRESS_INVALID, ValiFiTest.FIELD_ERROR_MSG);
		assertThat(field.get(), is(EMAIL_ADDRESS_INVALID));
		assertThat(field.isValid(), is(false));
		assertThat(field.getError(), is(ValiFiTest.FIELD_ERROR_MSG));
	}

	@Test
	public void checkConstructWithInvalidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(null, ValiFiTest.FIELD_ERROR_MSG);
		field.set(EMAIL_ADDRESS_INVALID);
		assertThat(field.get(), is(EMAIL_ADDRESS_INVALID));
		assertThat(field.isValid(), is(false));
		assertThat(field.getError(), is(ValiFiTest.FIELD_ERROR_MSG));
	}
}