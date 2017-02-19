package com.mlykotom.exampleviewmodel.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.valifi.ValiFiErrorDelay;
import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldEmail;

import eu.inloop.viewmodel.AbstractViewModel;


public class ManualValidationViewModel extends AbstractViewModel<ManualValidationView> {
	public final ValiFieldEmail email = new ValiFieldEmail();
	public final ValiFiForm form = new ValiFiForm(email);


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);

		email.setErrorDelay(ValiFiErrorDelay.NEVER);
	}


	@Override
	public void onDestroy() {
		form.destroy();
		super.onDestroy();
	}


	public boolean onManualSubmit() {
		form.refreshError();

		if(form.isValid()) {
			// do what you want after validation
			return true;
		}

		return false;
	}
}
