package com.mlykotom.exampleviewmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.validation.ValidatedEmailField;

import eu.inloop.viewmodel.AbstractViewModel;


public class ExampleViewModel extends AbstractViewModel<ExampleView> {
	public final ValidatedEmailField email = new ValidatedEmailField();


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
	}
}
