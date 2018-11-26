package com.mlykotom.valifi;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;


/**
 * Interface which serves for composite design pattern.
 * This means that form and field may be used just as validable and be validated the same way
 */
public interface ValiFiValidable extends Observable {
	/**
	 * @return whether field which implements it is valid
	 */
	@Bindable
	boolean isValid();

	/**
	 * So that form or field can be destroyed the same way
	 */
	void destroy();

	/**
	 * Clears the state of the field (e.g. after submit of form).
	 * Sets value to null.
	 */
	void reset();

	/**
	 * If you want to manually show error for the field
	 */
	void validate();
}
