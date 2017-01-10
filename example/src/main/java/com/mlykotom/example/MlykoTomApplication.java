package com.mlykotom.example;

import android.app.Application;

import com.mlykotom.valifi.Mlyked;


public class MlykoTomApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Mlyked.install(this);
	}
}