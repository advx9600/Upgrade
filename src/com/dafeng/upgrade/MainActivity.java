package com.dafeng.upgrade;

import com.dafeng.upgrade.fragment.AppUpgradeFragment;
import com.dafeng.upgrade.fragment.SystemUpGradeFragment;
import com.dafeng.upgrade.fragment.a;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class MainActivity extends ActionBarActivity {

	protected GestureDetector mGes;

	private static int FirstAppNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setGes();

		if (savedInstanceState == null) {
			resetFragment();
		}

	}	

	private void resetFragment() {		
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.container,
						FirstAppNum == 0 ? new SystemUpGradeFragment()
								.setGes(mGes) : new AppUpgradeFragment()
								.setGes(mGes)).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setGes() {
		mGes = new GestureDetector(this, new Gesture());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGes.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	public class Gesture extends GestureDetector.SimpleOnGestureListener {
		public boolean onSingleTapUp(MotionEvent ev) {
			return true;
		}

		public void onLongPress(MotionEvent ev) {
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int leng = (int) Math.sqrt((e1.getX() - e2.getX())
					* (e1.getX() - e2.getX()) + (e1.getY() - e2.getY())
					* (e1.getY() - e2.getY()));
			int velocity = (int) Math.sqrt(velocityX * velocityX + velocityY
					* velocityY);
			if (leng > 200 && velocity > 1000) {
				FirstAppNum = (FirstAppNum + 1) % 2;
				resetFragment();
			}
			return false;
		}
	}
}
