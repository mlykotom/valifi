package com.mlykotom.valifi;

/**
 * Interface which serves for composite design pattern.
 * This means that form and field may be used just as validable and be validated the same way
 */
public interface IValidable {
	/**
	 * @return whether field which implements it is valid
	 */
	boolean isValid();
}
