package com.dafeng.upgrade.fragment;

import com.dafeng.upgrade.R;

import android.app.AlertDialog;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BaseFragment extends Fragment implements BaseFragmentInt {

	protected View mRootView;
	protected GestureDetector mGes;

	public BaseFragment setGes(GestureDetector ges) {
		mGes = ges;
		return this;
	}

	protected void toast(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void toast(int id) {
		toast(this.getString(id));
	}

	protected void alert(int id) {
		alert(getString(id));
	}

	protected void alert(String msg) {
		new AlertDialog.Builder(this.getActivity()).setMessage(msg)
				.setNegativeButton(getString(android.R.string.ok), null).show();
	}

	protected View findViewById(int id) {
		return mRootView.findViewById(id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		mRootView = inflaterView(inflater, container);
		setUpView();
		startRun();
		return mRootView;
	}

	@Override
	public View inflaterView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startRun() {
		// TODO Auto-generated method stub
	}

}
