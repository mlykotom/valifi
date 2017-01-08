package com.mlykotom.exampleviewmodel;

import android.app.Application;

import com.mlykotom.mlyked.MlykedConfig;


public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		MlykedConfig.install(this);
	}
}
