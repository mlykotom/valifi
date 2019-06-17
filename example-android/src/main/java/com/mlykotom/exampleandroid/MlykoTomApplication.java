package com.mlykotom.exampleandroid;

import android.app.Application;

import com.mlykotom.valifi.ValiFi;

public class MlykoTomApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ValiFi.install(this);
	}
}