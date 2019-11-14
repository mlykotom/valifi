package com.mlykotom.example_arch_viewmodel.form;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.example_arch_viewmodel.databinding.FragmentExampleFormBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class FormValidationFragment extends Fragment implements FormValidationView {
	private FragmentExampleFormBinding mBinding;
	private FormValidationViewModel mViewModel;

	public static FormValidationFragment newInstance() {
		Bundle args = new Bundle();
		FormValidationFragment fragment = new FormValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(FormValidationViewModel.class);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleFormBinding.inflate(inflater);
		mBinding.setView(this);
		mBinding.setViewModel(mViewModel);
		return mBinding.getRoot();
	}

	@Override
	public void onSubmitClicked() {
		Toast.makeText(getContext(), "Submit clicked and form is valid!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onResetClicked() {
		mViewModel.email.reset();
	}
}
