package com.mlykotom.exampleviewmodel.single;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mlykotom.exampleviewmodel.MyValiFieldCaptcha;
import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.ValiFieldBase;
import com.mlykotom.valifi.fields.ValiFieldUsername;
import com.mlykotom.valifi.fields.number.ValiFieldLong;
import com.mlykotom.valifi.fields.number.ValiFieldNumber;

import java.util.Arrays;
import java.util.List;

import eu.inloop.viewmodel.AbstractViewModel;


public class SingleValidationViewModel extends AbstractViewModel<SingleValidationView> {
	public final ValiFieldUsername username = new ValiFieldUsername();
	public final MyValiFieldCaptcha captcha = new MyValiFieldCaptcha();
	public final ValiFieldUsername async = new ValiFieldUsername();
	public final ValiFieldLong numLong = new ValiFieldLong();


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
		setupNumberValidator();
		setupAsyncUsernameValidator();
	}


	@Override
	public void onDestroy() {
		ValiFi.destroyFields(numLong, username, captcha, async);
		super.onDestroy();
	}


	/**
	 * Example of initialization number validator.
	 */
	private void setupNumberValidator() {
		final long requiredMinNumber = 13;
		numLong.addNumberValidator("This number must be greater than 13", new ValiFieldNumber.NumberValidator<Long>() {
			@Override
			public boolean isValid(@NonNull Long value) {
				return value > requiredMinNumber;
			}
		});
	}


	/**
	 * Initialization of asynchronous validation of username
	 */
	private void setupAsyncUsernameValidator() {
		async.addCustomAsyncValidator("This user already exists", new ValiFieldBase.AsyncPropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) throws InterruptedException {
				Thread.sleep(2000);
				List<String> registeredUsernames = Arrays.asList("user", "name", "mlyko", "mlykotom", "charlie");
				return value != null && !registeredUsernames.contains(value.trim().toLowerCase());
			}
		});
	}
}
