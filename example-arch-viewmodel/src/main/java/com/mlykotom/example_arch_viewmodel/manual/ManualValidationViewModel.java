package com.mlykotom.example_arch_viewmodel.manual;

import com.mlykotom.example_arch_viewmodel.MyValiFieldCaptcha;
import com.mlykotom.valifi.ValiFiErrorDelay;
import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldEmail;

import androidx.lifecycle.ViewModel;

public class ManualValidationViewModel extends ViewModel {
	public final ValiFieldEmail email = new ValiFieldEmail();
	public final MyValiFieldCaptcha captcha = new MyValiFieldCaptcha();
	public final ValiFiForm form = new ValiFiForm(email, captcha);

	public ManualValidationViewModel() {
		email.setErrorDelay(ValiFiErrorDelay.NEVER);
	}

	@Override
	protected void onCleared() {
		form.destroy();
		super.onCleared();
	}

	public boolean onManualSubmit() {
		form.refreshError();

		if (form.isValid()) {
			// do what you want after validation
			return true;
		}

		return false;
	}
}
