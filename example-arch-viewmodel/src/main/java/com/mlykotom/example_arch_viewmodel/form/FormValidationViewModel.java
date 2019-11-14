package com.mlykotom.example_arch_viewmodel.form;

import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldEmail;
import com.mlykotom.valifi.fields.ValiFieldPassword;
import com.mlykotom.valifi.fields.ValiFieldPhone;

import androidx.lifecycle.ViewModel;

public class FormValidationViewModel extends ViewModel {
	public final ValiFieldEmail email = new ValiFieldEmail();
	public final ValiFieldPassword password = new ValiFieldPassword();
	public final ValiFieldPassword password2 = new ValiFieldPassword();
	public final ValiFieldPhone phone = new ValiFieldPhone();

	public final ValiFiForm passwordForm = new ValiFiForm(password, password2);
	public final ValiFiForm form = new ValiFiForm(email, passwordForm, phone);

	public FormValidationViewModel() {
		phone.setEmptyAllowed(true);
		password2.addVerifyFieldValidator("Passwords must be the same", password);
	}

	@Override
	protected void onCleared() {
		form.destroy();
		super.onCleared();
	}
}
