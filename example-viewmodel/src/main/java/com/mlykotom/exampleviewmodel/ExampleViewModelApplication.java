package com.mlykotom.exampleviewmodel;

import android.app.Application;

import com.mlykotom.mlyked.Mlyked;


public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		Mlyked.install(this, new Mlyked.Builder()
				.build());
	}
}
