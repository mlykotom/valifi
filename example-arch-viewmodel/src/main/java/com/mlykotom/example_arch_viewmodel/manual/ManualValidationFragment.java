package com.mlykotom.example_arch_viewmodel.manual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mlykotom.example_arch_viewmodel.databinding.FragmentExampleManualBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ManualValidationFragment extends Fragment implements ManualValidationView {
	private FragmentExampleManualBinding mBinding;
	private ManualValidationViewModel mViewModel;

	public static ManualValidationFragment newInstance() {

		Bundle args = new Bundle();

		ManualValidationFragment fragment = new ManualValidationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(ManualValidationViewModel.class);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentExampleManualBinding.inflate(inflater);
		mBinding.setView(this);
		mBinding.setViewModel(mViewModel);
		return mBinding.getRoot();
	}

	@Override
	public void onManualSubmitClicked() {
		if (mViewModel.onManualSubmit()) {
			Toast.makeText(getContext(), "Form is valid!", Toast.LENGTH_LONG).show();
		}
	}
}
