package com.mlykotom.valifi;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;


/**
 * Bundles more fields together and provides validation for all of them + destroying
 */
public class ValiFiForm extends BaseObservable {
	private List<ValiFieldBase> mFields = new ArrayList<>();


	public ValiFiForm(ValiFieldBase... fields) {
		for(ValiFieldBase field : fields) {
			addField(field);
		}
	}


	/**
	 * Checks whether all bundled fields are valid
	 *
	 * @return If any field is not valid -> false
	 */
	@Bindable
	public boolean getIsValid() {
		for(ValiFieldBase field : mFields) {
			if(!field.getIsValid()) return false;
		}

		return true;
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
	 * Clears used resources by this form + clears validated field's resources.
	 * Should be called after done working with form and fields
	 */
	public void destroy() {
		for(ValiFieldBase field : mFields) {
			field.destroy();
		}

		mFields.clear();
	}


	/**
	 * Field validation was changed and informs this form about it
	 *
	 * @param field which was changed
	 */
	void fieldValidationChanged(ValiFieldBase field) {
		notifyPropertyChanged(com.mlykotom.valifi.BR.isValid);
	}
}