package com.mlykotom.exampleviewmodel.single;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.exampleviewmodel.MyValiFieldCaptcha;
import com.mlykotom.valifi.ValiFieldBase;
import com.mlykotom.valifi.fields.ValiFieldUsername;

import java.util.Arrays;
import java.util.List;

import eu.inloop.viewmodel.AbstractViewModel;


public class SingleValidationViewModel extends AbstractViewModel<SingleValidationView> {
	public final ValiFieldUsername username = new ValiFieldUsername();
	public final MyValiFieldCaptcha captcha = new MyValiFieldCaptcha();
	public final ValiFieldUsername async = new ValiFieldUsername();


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
		setupAsyncUsernameValidator();
	}


	@Override
	public void onDestroy() {
		username.destroy();
		captcha.destroy();
		async.destroy();
		super.onDestroy();
	}


	/**
	 * Initialization of asynchronous validation of username
	 */
	private void setupAsyncUsernameValidator() {
		async.addCustomAsyncValidator("This user already exists", new ValiFieldBase.AsyncPropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) throws InterruptedException {
				Thread.sleep(3000);
				List<String> registeredUsernames = Arrays.asList("user", "name", "mlyko", "mlykotom", "charlie");
				return value != null && !registeredUsernames.contains(value.trim().toLowerCase());
			}
		});
	}
}
