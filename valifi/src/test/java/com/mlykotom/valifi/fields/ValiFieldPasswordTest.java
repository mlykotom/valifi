package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFiTest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ValiFieldPasswordTest {
	ValiFieldPassword field;

	@Before
	public void setup() {
		ValiFiTest.installWithoutContext();
		field = new ValiFieldPassword();
	}

	@Test
	public void initializationSucces() {
		assertThat(field.isValid(), not(true));
	}
}
