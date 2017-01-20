package com.mlykotom.exampleviewmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import eu.inloop.viewmodel.base.ViewModelBaseEmptyActivity;


public class ExampleViewModelActivity extends ViewModelBaseEmptyActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);
	}
}
