package com.mlykotom.example_arch_viewmodel.single;

import com.mlykotom.valifi.ValiFi;
import com.mlykotom.valifi.ValiFiValidable;
import com.mlykotom.valifi.ValiFieldBase;
import com.mlykotom.valifi.fields.ValiFieldCard;
import com.mlykotom.valifi.fields.ValiFieldUsername;
import com.mlykotom.valifi.fields.number.ValiFieldLong;
import com.mlykotom.valifi.fields.number.ValiFieldNumber;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

public class SingleValidationViewModel extends ViewModel {
	public final ValiFieldUsername username = new ValiFieldUsername();
	public final ValiFieldUsername async = new ValiFieldUsername();
	public final ValiFieldLong numLong = new ValiFieldLong();
	public final ValiFieldCard creditCard = new ValiFieldCard();

	public final ValiFiValidable validable = username;

	public SingleValidationViewModel() {
		setupNumberValidator();
		setupAsyncUsernameValidator();
	}

	@Override
	protected void onCleared() {
		ValiFi.destroyFields(numLong, username, async, creditCard);
		super.onCleared();
	}

	/**
	 * Example of initialization number validator.
	 */
	private void setupNumberValidator() {
		final long requiredMinNumber = 13;
		numLong.addNumberValidator("This number must be greater than 13", new ValiFieldNumber.NumberValidator<Long>() {
			@Override
			public boolean isValid(@NonNull Long value) {
				return value > requiredMinNumber;
			}
		});
	}

	/**
	 * Initialization of asynchronous validation of username
	 */
	private void setupAsyncUsernameValidator() {
		async.addCustomAsyncValidator("This user already exists", new ValiFieldBase.AsyncPropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) throws InterruptedException {
				Thread.sleep(2000);
				List<String> registeredUsernames = Arrays.asList("user", "name", "mlyko", "mlykotom", "charlie");
				return value != null && !registeredUsernames.contains(value.trim().toLowerCase(Locale.US));
			}
		});
	}
}
