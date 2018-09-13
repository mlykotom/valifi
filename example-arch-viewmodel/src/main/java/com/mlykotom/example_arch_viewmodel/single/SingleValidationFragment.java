package com.mlykotom.example_arch_viewmodel.single;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.example_arch_viewmodel.databinding.FragmentExampleSingleBinding;


public class SingleValidationFragment extends Fragment implements SingleValidationView {
	private FragmentExampleSingleBinding mBinding;
	private SingleValidationViewModel mViewModel;


	public static SingleValidationFragment newInstance() {

		Bundle args = new Bundle();

		SingleValidationFragment fragment = new SingleValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(SingleValidationViewModel.class);
	}


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleSingleBinding.inflate(inflater);
		mBinding.setView(this);
		mBinding.setViewModel(mViewModel);
		return mBinding.getRoot();
	}


	@Override
	public void onSubmitClicked() {
		Toast.makeText(getContext(), "Submit clicked and got " + mViewModel.username.getValue(), Toast.LENGTH_LONG).show();
	}
}


