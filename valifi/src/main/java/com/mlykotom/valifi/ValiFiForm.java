package com.mlykotom.valifi;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;


/**
 * Bundles more fields together and provides validation for all of them + destroying
 */
public class ValiFiForm extends BaseObservable implements ValiFiValidable {
	private List<ValiFieldBase> mFields = new ArrayList<>();


	public ValiFiForm(ValiFieldBase... fields) {
		for(ValiFieldBase field : fields) {
			addField(field);
		}
	}


	/**
	 * Checks whether all bundled fields are valid
	 *
	 * @return If any field is not valid = false
	 */
	@Bindable
	@Override
	public boolean isValid() {
		for(ValiFieldBase field : mFields) {
			if(!field.isValid()) return false;
		}

		return true;
	}


	/**
	 * Clears used resources by this form + clears validated field's resources.
	 * Should be called after done working with form and fields
	 */
	public void destroy() {
		for(ValiFieldBase field : mFields) {
			field.destroy();
		}

		mFields.clear();
	}


	@Override
	public void reset() {
		for(ValiFieldBase field : mFields) {
			field.reset();
		}
	}


	/**
	 * Adds field to this form so can be validated with others
	 *
	 * @param field to be validated through this form
	 */
	public void addField(ValiFieldBase field) {
		field.setFormValidation(this);
		mFields.add(field);
	}


	/**
	 * Notifies about error all fields in the form
	 *
	 * @see ValiFieldBase#refreshError()
	 */
	public void refreshError() {
		for(ValiFieldBase field : mFields) {
			field.refreshError();
		}
	}


	/**
	 * Field validation was changed and informs this form about it
	 *
	 * @param field which was changed (ignored and handled by observable callback)
	 */
	void notifyValidationChanged(ValiFieldBase field) {
		notifyPropertyChanged(com.mlykotom.valifi.BR.valid);
	}
}