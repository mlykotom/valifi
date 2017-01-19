package com.mlykotom.exampleviewmodel;

import android.support.annotation.Nullable;

import com.mlykotom.valifi.fields.ValiFieldText;


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
