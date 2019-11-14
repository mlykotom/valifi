package com.mlykotom.valifi.fields;

import com.mlykotom.valifi.ValiFiTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class ValiFieldCardTest {
	public static final String VALIDATOR_EMPTY_MESSAGE = "Credit card is not valid";
	private final String inputCard;
	private ValiFieldCard mField;

	public ValiFieldCardTest(String input) {
		this.inputCard = input;
	}

	@Parameterized.Parameters
	public static Collection creditCards() {
		return Arrays.asList(
				"4242424242424242",
				"4012888888881881",
				"4000056655665556",
				"5555555555554444",
				"5200828282828210",
				"5105105105105100",
				"378282246310005",
				"371449635398431",
				"6011111111111117",
				"6011000990139424",
				"30569309025904",
				"38520000023237",
				"3530111333300000",
				"3566002020360505"
		);
	}

	@Before
	public void prepare() {
		ValiFiTest.installWithoutContext();
		mField = new ValiFieldCard(0L, VALIDATOR_EMPTY_MESSAGE);
	}

	@Test
	public void checkLuhnTestForCard() {
		assertThat(ValiFieldCard.isLuhnTestValid(inputCard), is(true));
	}

	@Test
	public void checkLuhnTestWronString() {
		assertThat(ValiFieldCard.isLuhnTestValid("424242424242424x"), is(false));
		assertThat(ValiFieldCard.isLuhnTestValid("assdfg123sf"), is(false));
	}

	@Test
	public void checkDefaultCardTypes() {
		mField.set(inputCard);
		assertThat(mField.isValid(), is(true));
	}
}
