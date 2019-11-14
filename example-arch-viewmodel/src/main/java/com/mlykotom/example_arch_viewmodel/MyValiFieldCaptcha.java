package com.mlykotom.example_arch_viewmodel;

import com.mlykotom.valifi.fields.ValiFieldText;

import androidx.annotation.Nullable;

/**
 * Custom validator that extends text validator
 */
public class MyValiFieldCaptcha extends ValiFieldText {
	public MyValiFieldCaptcha() {
		super();
		addMyValidator();
	}

	public MyValiFieldCaptcha(String defaultValue) {
		super(defaultValue);
		addMyValidator();
	}

	private void addMyValidator() {
		addCustomValidator("Captcha must be correct", new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return "captcha".equals(value);
			}
		});
	}
}
