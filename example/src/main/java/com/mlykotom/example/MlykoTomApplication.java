package com.mlykotom.example;

import android.app.Application;

import com.mlykotom.mlyked.ValidationConfig;


public class MlykoTomApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ValidationConfig.install(this);
	}
}