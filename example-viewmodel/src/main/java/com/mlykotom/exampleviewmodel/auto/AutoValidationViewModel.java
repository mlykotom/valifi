package com.mlykotom.exampleviewmodel.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.exampleviewmodel.MyValiFieldCaptcha;
import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldEmail;
import com.mlykotom.valifi.fields.ValiFieldPassword;
import com.mlykotom.valifi.fields.ValiFieldPhone;
import com.mlykotom.valifi.fields.ValiFieldUsername;

import eu.inloop.viewmodel.AbstractViewModel;


public class AutoValidationViewModel extends AbstractViewModel<AutoValidationView> {
	public final ValiFieldEmail email = new ValiFieldEmail();
	public final ValiFieldUsername username = new ValiFieldUsername();
	public final ValiFieldPassword password = new ValiFieldPassword();
	public final ValiFieldPassword password2 = new ValiFieldPassword();
	public final ValiFieldPhone phone = new ValiFieldPhone();
	public final MyValiFieldCaptcha captcha = new MyValiFieldCaptcha();
	public final ValiFiForm form = new ValiFiForm(email, username, password, password2, phone);


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
		password2.addVerifyFieldValidator("Passwords must be the same", password);
		phone.setEmptyAllowed(true);
	}


	@Override
	public void onDestroy() {
		form.destroy();
		super.onDestroy();
	}
}
