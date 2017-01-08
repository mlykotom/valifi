package com.mlykotom.mlykotom;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mlykotom.mlykotom.databinding.ActivityExampleBinding;
import com.mlykotom.validation.ValidatedEmailField;


public class ExampleActivity extends AppCompatActivity {
	public final ValidatedEmailField email = new ValidatedEmailField();
	private ActivityExampleBinding mBinding;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_example);
		mBinding.setViewModel(this);
	}


	@Override
	protected void onDestroy() {
		email.destroy();
		super.onDestroy();
	}


	public void onSubmitClicked() {
		Toast.makeText(this, "Submit enabled and clicked", Toast.LENGTH_LONG).show();
	}
}
