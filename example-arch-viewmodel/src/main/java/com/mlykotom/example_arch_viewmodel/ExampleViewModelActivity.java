package com.mlykotom.example_arch_viewmodel;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.mlykotom.example_arch_viewmodel.form.FormValidationFragment;
import com.mlykotom.example_arch_viewmodel.manual.ManualValidationFragment;
import com.mlykotom.example_arch_viewmodel.single.SingleValidationFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ExampleViewModelActivity extends AppCompatActivity {
	private PagesAdapter mAdapter;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);
		setupActionbar();
		setupAdapter();
	}

	private void setupActionbar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("ValiFi example");
		setSupportActionBar(toolbar);
	}

	private void setupAdapter() {
		mViewPager = findViewById(R.id.pager);
		mTabLayout = findViewById(R.id.tabs);

		mAdapter = new PagesAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
	}

	private static class PagesAdapter extends FragmentStatePagerAdapter {
		private static String[] sTitles = new String[]{"Single", "Form", "Manual"};

		public PagesAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
				default:
					return SingleValidationFragment.newInstance();

				case 1:
					return FormValidationFragment.newInstance();

				case 2:
					return ManualValidationFragment.newInstance();
			}
		}

		@Override
		public int getCount() {
			return sTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return sTitles[position];
		}
	}
}
