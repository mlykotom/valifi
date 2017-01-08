package com.mlykotom.example;

import android.app.Application;

import com.mlykotom.mlyked.Mlyked;


public class MlykoTomApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Mlyked.install(this);
	}
}