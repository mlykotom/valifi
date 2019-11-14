package com.mlykotom.valifi;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Bundles more fields together and provides validation for all of them + destroying
 */
public class ValiFiForm extends BaseObservable implements ValiFiValidable {
	private List<ValiFiValidable> mFields = new ArrayList<>();
	@Nullable private ValiFiForm mParentForm;

	public ValiFiForm(ValiFiValidable... fields) {
		for (ValiFiValidable field : fields) {
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
		for (ValiFiValidable field : mFields) {
			if (!field.isValid()) return false;
		}

		return true;
	}

	/**
	 * Clears used resources by this form + clears validated field's resources.
	 * Should be called after done working with form and fields
	 */
	public void destroy() {
		for (ValiFiValidable field : mFields) {
			field.destroy();
		}

		mFields.clear();
		mParentForm = null;
	}

	@Override
	public void reset() {
		for (ValiFiValidable field : mFields) {
			field.reset();
		}
	}

	/**
	 * Notifies about error all fields in the form
	 *
	 * @see ValiFieldBase#validate()
	 */
	@Override
	public void validate() {
		for (ValiFiValidable field : mFields) {
			field.validate();
		}
	}

	/**
	 * Adds field to this form so can be validated with others
	 *
	 * @param field to be validated through this form
	 */
	public void addField(ValiFiValidable field) {
		field.setFormValidation(this);
		mFields.add(field);
	}

	/**
	 * Notifies about error all fields in the form
	 *
	 * @see ValiFieldBase#refreshError()
	 */
	@Override
	public void refreshError() {
		for (ValiFiValidable field : mFields) {
			field.refreshError();
		}
	}

	/**
	 * Field validation was changed and informs this form about it
	 *
	 * @param field which was changed (ignored and handled by observable callback)
	 */
	@SuppressWarnings("unused")
	void notifyValidationChanged(ValiFiValidable field) {
		notifyPropertyChanged(com.mlykotom.valifi.BR.valid);
		if (mParentForm != null) {
			mParentForm.notifyValidationChanged(this);
		}
	}

	/**
	 * Bundles this form to another form
	 *
	 * @param form which validates all bundled fields
	 */
	@Override
	public void setFormValidation(@Nullable ValiFiForm form) {
		mParentForm = form;
	}

	@Nullable
	public ValiFiForm getBoundForm() {
		return mParentForm;
	}
}
