package com.mlykotom.exampleviewmodel;

import com.mlykotom.validation.ValidatedEmailField;

import eu.inloop.viewmodel.AbstractViewModel;


public class ExampleViewModel extends AbstractViewModel<ExampleView> {
	public final ValidatedEmailField email = new ValidatedEmailField();
}
