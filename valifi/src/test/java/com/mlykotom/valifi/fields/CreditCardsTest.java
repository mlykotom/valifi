package com.mlykotom.valifi.fields;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(Parameterized.class)
public class CreditCardsTest {
	private final String inputCard;


	public CreditCardsTest(String input) {
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


	@Test
	public void checkLuhnTestForCard() {
		System.out.println("Card is: " + inputCard);
		assertThat(ValiFieldCreditCard.isLuhnTestValid(inputCard), is(true));
	}


	@Test
	public void checkLuhnTestWronString() {
		assertThat(ValiFieldCreditCard.isLuhnTestValid("424242424242424x"), is(false));
		assertThat(ValiFieldCreditCard.isLuhnTestValid("assdfg123sf"), is(false));
	}
}
