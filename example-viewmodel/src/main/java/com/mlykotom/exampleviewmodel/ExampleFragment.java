package com.mlykotom.exampleviewmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.exampleviewmodel.databinding.FragmentExampleBinding;

import eu.inloop.viewmodel.base.ViewModelBaseFragment;


public class ExampleFragment extends ViewModelBaseFragment<ExampleView, ExampleViewModel> implements ExampleView {
	private FragmentExampleBinding mBinding;


	@Nullable
	@Override
	public Class<ExampleViewModel> getViewModelClass() {
		return ExampleViewModel.class;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleBinding.inflate(inflater);
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


