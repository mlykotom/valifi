package com.mlykotom.exampleviewmodel.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.exampleviewmodel.MyValiFieldCaptcha;
import com.mlykotom.valifi.fields.ValiFieldUsername;

import eu.inloop.viewmodel.AbstractViewModel;


public class AutoValidationViewModel extends AbstractViewModel<AutoValidationView> {
	public final ValiFieldUsername username = new ValiFieldUsername();
	public final MyValiFieldCaptcha captcha = new MyValiFieldCaptcha();


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
	}


	@Override
	public void onDestroy() {
		username.destroy();
		captcha.destroy();
		super.onDestroy();
	}
}
