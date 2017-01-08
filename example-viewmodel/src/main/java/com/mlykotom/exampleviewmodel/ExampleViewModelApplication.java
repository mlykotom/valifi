package com.mlykotom.exampleviewmodel;

import android.app.Application;

import com.mlykotom.mlyked.ValidationConfig;


public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ValidationConfig.install(this);
	}
}
