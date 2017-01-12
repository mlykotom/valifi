package com.mlykotom.valifi;

import android.support.annotation.Nullable;

import com.mlykotom.valifi.exceptions.ValiFiException;
import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;


public abstract class ValiFieldBaseTest {
	protected ValiFieldText mField;


	@Test
	public abstract void setGetIsCorrect();


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
		assertThat(mField.mBoundFields, is(nullValue()));
		// check if has form
		assertThat(mField.getBoundForm(), is(nullValue()));

		// TODO check if we can do something with the field (we should not)
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
}
