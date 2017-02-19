package com.mlykotom.valifi;

import android.support.annotation.Nullable;

import com.mlykotom.valifi.exceptions.ValiFiException;
import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.Before;
import org.junit.Test;


public class ValiFiTest {
	public static final String FIELD_ERROR_MSG = "field is not valid";
	private ValiFieldBase<String> mField;


	/**
	 * WARNING: installing without context will crash when getting string, only for tests!
	 */
	public static void installWithoutContext() {
		ValiFi.install(null);
	}


	@Before
	public void prepareField() {
		installWithoutContext();
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
}
