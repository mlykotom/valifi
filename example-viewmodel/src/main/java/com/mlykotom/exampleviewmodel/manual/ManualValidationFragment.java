package com.mlykotom.exampleviewmodel.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.exampleviewmodel.databinding.FragmentExampleManualBinding;

import eu.inloop.viewmodel.base.ViewModelBaseFragment;


public class ManualValidationFragment extends ViewModelBaseFragment<ManualValidationView, ManualValidationViewModel> implements ManualValidationView {
	private FragmentExampleManualBinding mBinding;


	public static ManualValidationFragment newInstance() {

		Bundle args = new Bundle();

		ManualValidationFragment fragment = new ManualValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleManualBinding.inflate(inflater);
		mBinding.setView(this);
		mBinding.setViewModel(getViewModel());
		return mBinding.getRoot();
	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setModelView(this);
	}


	@Nullable
	@Override
	public Class<ManualValidationViewModel> getViewModelClass() {
		return ManualValidationViewModel.class;
	}


	@Override
	public void onManualSubmitClicked() {
		if(getViewModel().onManualSubmit()) {
			Toast.makeText(getContext(), "Form is valid!", Toast.LENGTH_LONG).show();
		}
	}
}
