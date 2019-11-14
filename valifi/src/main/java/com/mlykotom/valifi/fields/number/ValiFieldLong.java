package com.mlykotom.valifi.fields.number;

import androidx.annotation.Nullable;

public class ValiFieldLong extends ValiFieldNumber<Long> {
	public ValiFieldLong() {
		super();
	}

	public ValiFieldLong(@Nullable Long defaultValue) {
		super(defaultValue);
	}

	@Override
	protected Long parse(@Nullable String value) throws NumberFormatException {
		return Long.parseLong(value);
	}
}
