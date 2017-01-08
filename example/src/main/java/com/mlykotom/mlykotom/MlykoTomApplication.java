package com.mlykotom.mlykotom;

import android.app.Application;

import com.mlykotom.validation.ValidationConfig;


public class MlykoTomApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ValidationConfig.install(this);
	}
}