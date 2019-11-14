package com.mlykotom.valifi;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Class for validating credit card types.
 * See <a href="http://stackoverflow.com/questions/72768/how-do-you-detect-credit-card-type-based-on-number">StackOverflow</a> for patterns used
 *
 * @see ValiFi.Builder#setKnownCardTypes(ValiFiCardType...) for how to install more types
 */
public class ValiFiCardType {
	public static final ValiFiCardType MASTERCARD = new ValiFiCardType("Mastercard", "^5[1-5][0-9]{5,}|222[1-9][0-9]{3,}|22[3-9][0-9]{4,}|2[3-6][0-9]{5,}|27[01][0-9]{4,}|2720[0-9]{3,}$");
	public static final ValiFiCardType AMERICAN_EXPRESS = new ValiFiCardType("American Express", "^3[47][0-9]{5,}$");
	public static final ValiFiCardType VISA = new ValiFiCardType("Visa", "^4[0-9]{6,}$");
	public static final ValiFiCardType DISCOVER = new ValiFiCardType("Discover", "^6(?:011|5[0-9]{2})[0-9]{3,}$");
	public static final ValiFiCardType DINERS_CLUB = new ValiFiCardType("Diners Club", "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$");
	public static final ValiFiCardType JCB = new ValiFiCardType("JCB", "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$");

	public final String name;
	public final Pattern pattern;

	/**
	 * Constructor for more card types which may be validated
	 *
	 * @param name just for identification
	 * @param pattern to be validated (regex, Pattern will be compiled from the string)
	 */
	public ValiFiCardType(String name, String pattern) {
		this.name = name;
		this.pattern = Pattern.compile(pattern);
	}

	public static Set<ValiFiCardType> getDefaultTypes() {
		HashSet<ValiFiCardType> set = new HashSet<>();
		set.add(MASTERCARD);
		set.add(AMERICAN_EXPRESS);
		set.add(VISA);
		set.add(DISCOVER);
		set.add(DINERS_CLUB);
		set.add(JCB);
		return set;
	}
}
