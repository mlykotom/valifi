package com.mlykotom.exampleviewmodel;

import android.app.Application;

import com.mlykotom.valifi.ValiFi;


public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ValiFi.install(this);
	}
}
