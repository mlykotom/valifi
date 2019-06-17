package com.mlykotom.exampleandroid;

import android.os.Bundle;
import android.widget.Toast;

import com.mlykotom.exampleandroid.databinding.ActivityExampleBinding;
import com.mlykotom.valifi.fields.ValiFieldEmail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ExampleActivity extends AppCompatActivity {
	public final ValiFieldEmail email = new ValiFieldEmail();

	private ActivityExampleBinding mBinding;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_example);
		mBinding.setViewModel(this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		email.setValue(savedInstanceState.getString("EMAIL"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("EMAIL", email.getValue());
	}

	@Override
	protected void onDestroy() {
		email.destroy();
		super.onDestroy();
	}

	public void onSubmitClicked() {
		Toast.makeText(this, "Submit clicked and got " + email.getValue(), Toast.LENGTH_LONG).show();
	}
}
