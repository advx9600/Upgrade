package com.dafeng.upgrade.fragment.b;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class d extends com.dafeng.upgrade.fragment.b.aa implements
		OnTouchListener {
	private GestureDetector mGes;

	public d(GestureDetector ges) {
		// TODO Auto-generated constructor stub
		mGes = ges;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mGes.onTouchEvent(event);
		return false;
	}

}
