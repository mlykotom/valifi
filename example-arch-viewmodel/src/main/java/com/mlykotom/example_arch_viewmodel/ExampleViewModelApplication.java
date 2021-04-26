package com.mlykotom.example_arch_viewmodel;

import android.app.Application;

import com.mlykotom.valifi.ValiFi;

public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ValiFi.install(this);
	}
}
