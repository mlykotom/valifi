package com.mlykotom.exampleviewmodel.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.exampleviewmodel.databinding.FragmentExampleAutoBinding;

import eu.inloop.viewmodel.base.ViewModelBaseFragment;


public class AutoValidationFragment extends ViewModelBaseFragment<AutoValidationView, AutoValidationViewModel> implements AutoValidationView {
	private FragmentExampleAutoBinding mBinding;


	public static AutoValidationFragment newInstance() {

		Bundle args = new Bundle();

		AutoValidationFragment fragment = new AutoValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Nullable
	@Override
	public Class<AutoValidationViewModel> getViewModelClass() {
		return AutoValidationViewModel.class;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleAutoBinding.inflate(inflater);
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
		Toast.makeText(getContext(), "Submit clicked and got " + getViewModel().email.getValue(), Toast.LENGTH_LONG).show();
	}
}


