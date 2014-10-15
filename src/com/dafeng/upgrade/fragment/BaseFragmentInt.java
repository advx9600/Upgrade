package com.dafeng.upgrade.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface BaseFragmentInt {
	public View inflaterView(LayoutInflater inflater, ViewGroup container);

	public void setUpView();

	public void startRun();

}
