package com.mlykotom.exampleviewmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldDate;
import com.mlykotom.valifi.fields.ValiFieldEmail;
import com.mlykotom.valifi.fields.ValiFieldPassword;

import eu.inloop.viewmodel.AbstractViewModel;


public class ExampleViewModel extends AbstractViewModel<ExampleView> {
	public final ValiFieldEmail email = new ValiFieldEmail();
	public final ValiFieldPassword password = new ValiFieldPassword();
	public final ValiFieldDate dateOfBirth = new ValiFieldDate();
	public final ValiFiForm form = new ValiFiForm(email, password);


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
	}


	@Override
	public void onDestroy() {
		form.destroy();
		super.onDestroy();
	}
}
