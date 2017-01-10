package com.mlykotom.exampleviewmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mlykotom.valifi.ValiFieldEmail;

import eu.inloop.viewmodel.AbstractViewModel;


public class ExampleViewModel extends AbstractViewModel<ExampleView> {
	public final ValiFieldEmail email = new ValiFieldEmail();


	@Override
	public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
		super.onCreate(arguments, savedInstanceState);
	}
}
