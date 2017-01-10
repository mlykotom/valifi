package com.mlykotom.exampleviewmodel;

import android.app.Application;

import com.mlykotom.valifi.Mlyked;


public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Mlyked.install(this);
	}
}
