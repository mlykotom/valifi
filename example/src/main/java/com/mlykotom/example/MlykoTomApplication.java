package com.mlykotom.example;

import android.app.Application;

import com.mlykotom.mlyked.MlykedConfig;


public class MlykoTomApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		MlykedConfig.install(this);
	}
}