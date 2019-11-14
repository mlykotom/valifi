package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.ValiFiCardType;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValiFieldCardInstallationTest {
	@Test
	public void checkCardOnlyLuhn() {
		prepareNoDefaultTypes();
		ValiFieldCard field = new ValiFieldCard(4265773072920733L, ValiFieldCardTest.VALIDATOR_EMPTY_MESSAGE);
		assertThat(field.isValid(), is(true));
	}

	@Test
	public void checkCardOnlyLuhnFailed() {
		prepareNoDefaultTypes();
		ValiFieldCard field = new ValiFieldCard(4265773020733L, ValiFieldCardTest.VALIDATOR_EMPTY_MESSAGE);
		assertThat(field.isValid(), is(false));
	}

	@Test
	public void checkCustomCard() {
		ValiFi.install(new ValiFi.Builder()
				.setKnownCardTypes(new ValiFiCardType("Visa Custom", "^4[0-9]{6,}$")) //no default known types
				.build());

		ValiFieldCard field = new ValiFieldCard(4265773072920733L, ValiFieldCardTest.VALIDATOR_EMPTY_MESSAGE);
		assertThat(field.isValid(), is(true));
	}

	private void prepareNoDefaultTypes() {
		ValiFi.install(new ValiFi.Builder()
				.setKnownCardTypes() //no default known types
				.build());
	}
}
