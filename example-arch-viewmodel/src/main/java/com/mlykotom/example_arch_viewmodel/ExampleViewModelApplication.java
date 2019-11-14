package com.mlykotom.example_arch_viewmodel;

import android.app.Application;

import com.mlykotom.valifi.ValiFi;
import com.squareup.leakcanary.LeakCanary;

public class ExampleViewModelApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);

		ValiFi.install(this);
	}
}
