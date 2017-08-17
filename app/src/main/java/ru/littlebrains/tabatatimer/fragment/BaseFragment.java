package ru.littlebrains.tabatatimer.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import ru.littlebrains.tabatatimer.R;


public class BaseFragment extends Fragment {

	public ActionBar actionBar;
	public AppCompatActivity mActivity;
	public Toolbar toolbar;
	public FrameLayout rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);

		actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		mActivity = (AppCompatActivity)getActivity();
		setHasOptionsMenu(true);
		setRetainInstance(true);

		return null;
	}

	protected void setTitle(int idTitle){
		if(actionBar != null) {
			actionBar.setTitle(idTitle);
		}
	}

	protected void setTitle(String title){
		if(actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	protected void showToast(int idText){
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(mActivity, idText, duration);
		toast.show();
	}
}
