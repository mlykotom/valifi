package com.mlykotom.exampleviewmodel.single;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.exampleviewmodel.databinding.FragmentExampleSingleBinding;

import eu.inloop.viewmodel.base.ViewModelBaseFragment;


public class SingleValidationFragment extends ViewModelBaseFragment<SingleValidationView, SingleValidationViewModel> implements SingleValidationView {
	private FragmentExampleSingleBinding mBinding;


	public static SingleValidationFragment newInstance() {

		Bundle args = new Bundle();

		SingleValidationFragment fragment = new SingleValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Nullable
	@Override
	public Class<SingleValidationViewModel> getViewModelClass() {
		return SingleValidationViewModel.class;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleSingleBinding.inflate(inflater);
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
		Toast.makeText(getContext(), "Submit clicked and got " + getViewModel().username.getValue(), Toast.LENGTH_LONG).show();
	}
}


