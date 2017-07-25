package com.mlykotom.exampleviewmodel.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.exampleviewmodel.databinding.FragmentExampleFormBinding;

import eu.inloop.viewmodel.base.ViewModelBaseFragment;


public class FormValidationFragment extends ViewModelBaseFragment<FormValidationView, FormValidationViewModel> implements FormValidationView {
	private FragmentExampleFormBinding mBinding;


	public static FormValidationFragment newInstance() {
		Bundle args = new Bundle();
		FormValidationFragment fragment = new FormValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Nullable
	@Override
	public Class<FormValidationViewModel> getViewModelClass() {
		return FormValidationViewModel.class;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleFormBinding.inflate(inflater);
		mBinding.setView(this);
		mBinding.setViewModel(getViewModel());
		return mBinding.getRoot();
	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setModelView(this);
	}


	@Override
	public void onSubmitClicked() {
		Toast.makeText(getContext(), "Submit clicked and form is valid!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onResetClicked() {
		getViewModel().email.reset();
	}
}
