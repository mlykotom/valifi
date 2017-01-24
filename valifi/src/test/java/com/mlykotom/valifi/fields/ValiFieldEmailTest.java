package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ValiFieldEmailTest {
	public static final String EMAIL_ADDRESS_VALID = "test@email.com";
	public static final String EMAIL_ADDRESS_INVALID = "this_is_not_ok";
	public static final String EMAIL_ERROR_MSG = "email is not valid";


	@Before
	public void installWithoutContext() {
		// WARNING: installing without context will crash when getting string, only for tests!
		ValiFi.install(null);
	}


	@Test
	public void checkConstructWithDefaultValidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(EMAIL_ADDRESS_VALID, EMAIL_ERROR_MSG);
		assertThat(field.get(), is(EMAIL_ADDRESS_VALID));
		assertThat(field.getIsValid(), is(true));
	}


	@Test
	public void checkConstructWithValidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(null, EMAIL_ERROR_MSG);
		field.set(EMAIL_ADDRESS_VALID);
		assertThat(field.get(), is(EMAIL_ADDRESS_VALID));
		assertThat(field.getIsValid(), is(true));
	}


	@Test
	public void checkConstructWithDefaultInvalidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(EMAIL_ADDRESS_INVALID, EMAIL_ERROR_MSG);
		assertThat(field.get(), is(EMAIL_ADDRESS_INVALID));
		assertThat(field.getIsValid(), is(false));
		assertThat(field.getError(), is(EMAIL_ERROR_MSG));
	}


	@Test
	public void checkConstructWithInvalidEmail() {
		ValiFieldEmail field = new ValiFieldEmail(null, EMAIL_ERROR_MSG);
		field.set(EMAIL_ADDRESS_INVALID);
		assertThat(field.get(), is(EMAIL_ADDRESS_INVALID));
		assertThat(field.getIsValid(), is(false));
		assertThat(field.getError(), is(EMAIL_ERROR_MSG));
	}
}