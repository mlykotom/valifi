package com.mlykotom.valifi;

import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ValiFiFormTest {
	private ValiFiForm mForm;


	@Before
	public void prepare() {
		mForm = new ValiFiForm();
	}


	@After
	public void clean() {
		mForm.destroy();
	}


	@Test
	public void checkFieldsValid() {
		ValiFieldText field1 = new ValiFieldText();
		field1.setEmptyAllowed(true);
		mForm.addField(field1);

		ValiFieldText field2 = new ValiFieldText("email@email.email");
		field2.addEmailValidator("email not valid");
		mForm.addField(field1);

		assertThat(mForm.getIsValid(), is(true));
	}
}
