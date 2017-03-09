package com.mlykotom.valifi;

import android.databinding.Observable;
import android.support.annotation.Nullable;

import com.mlykotom.valifi.fields.ValiFieldText;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ValiFieldBaseTest {

	@Before
	public void prepare() {
		ValiFiTest.installWithoutContext();
	}


	@Test
	public void setGetIsCorrect() {
		ValiFieldBase<String> field = new ValiFieldText();
		field.set("test_value");
		assertThat("test_value", is(field.get()));
		assertThat(field.getIsValid(), is(true));
	}


	@Test
	public void checkBoundFieldsInvalid() {
		ValiFieldBase<String> field = new ValiFieldText();
		ValiFieldBase<String> boundField = new ValiFieldText();
		field.addVerifyFieldValidator("fields must be same", boundField);

		boundField.setValue("val_1");
		field.setValue("val_2");

		assertThat(field.getIsValid(), is(false));
	}

	// ---- destroy ---- //


	@Test
	public void checkMoreDestroyCalls() {
		ValiFieldBase<String> field = new ValiFieldText();

		field.destroy();
		field.destroy();
	}


	@Test
	public void checkDestroyingFormAndTheSameField() {
		ValiFieldBase<String> field = new ValiFieldText();
		ValiFiForm form = new ValiFiForm(field);

		form.destroy();
		form.destroy();
		field.destroy();
	}


	// ---- add custom validators ---- //


	@Test
	public void checkAddCustomValidator1Invalid() {
		ValiFieldBase<String> field = new ValiFieldText();
		field.addCustomValidator(new ValiFieldBase.PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return false;
			}
		});

		assertThat(field.getIsValid(), is(false));
	}


	@Test
	public void checkBoundFieldsValid() {
		ValiFieldBase<String> field = new ValiFieldText();
		ValiFieldBase<String> boundField = new ValiFieldText();
		field.addVerifyFieldValidator("fields must be same", boundField);

		boundField.setValue("val_1");
		field.setValue("val_1");

		assertThat(field.getIsValid(), is(true));
	}


	@Test
	public void checkDefaultErrorDelayInConstructor() {
		ValiFieldBase<String> field = new ValiFieldText();
		assertThat(field.mErrorDelay, is(ValiFi.getErrorDelay()));
	}


	@Test
	public void checkErrorDelaySetByMethod() {
		ValiFieldBase<String> field = new ValiFieldText();
		field.setErrorDelay(9999);
		assertThat(field.mErrorDelay, is(9999L));
	}


	@Test
	public void checkDelayedError() throws InterruptedException {
		final long delay_time = 700;

		final ValiFieldBase<String> field = new ValiFieldText("invalid_length_12345");
		field.setErrorDelay(delay_time);
		assertThat(field.mErrorDelay, is(delay_time));

		final boolean[] testIsOk = {false};
		field.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
			@Override
			public void onPropertyChanged(Observable observable, int i) {
				if(i != com.mlykotom.valifi.BR.error) return;

				testIsOk[0] = true;
			}
		});

		field.addCustomValidator(ValiFiTest.FIELD_ERROR_MSG, new ValiFieldBase.PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return false;
			}
		});

		assertThat(field.getIsValid(), is(false));

		Thread.sleep(delay_time + 100);

		assertThat(field.getError(), is(ValiFiTest.FIELD_ERROR_MSG));
		assertThat(field.mDueTime, is(-1L));
		assertThat(testIsOk[0], is(true));
	}


//	@Test
//	public void checkNoErrorShown() {
//		ValiFieldBase<String> field = new ValiFieldText("invalid");
//		field.setErrorDelay(ValiFi.Builder.NO_ERROR_SHOWING);
//
//		// TODO check several delayed times that there is no error
////		field.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
////			@Override
////			public void onPropertyChanged(Observable sender, int propertyId) {
////				if(i !=)
////			}
////		});
//	}
}
