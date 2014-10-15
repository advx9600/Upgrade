package com.dafeng.upgrade.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dafeng.upgrade.R;
import com.dafeng.upgrade.util.Sys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SystemUpGradeFragment extends BaseFragment {

	public static final String URL = "http://10.1.0.111:8080/pro/ImgUpgrade/jQuery-File-Upload/server/php/?master_fill_flag=1&board="
			+ Sys.BOARD + "&git_branch=" + Sys.MACHINE_TYPE;

	private SystemAdapter mSystemAdapter;

	private Button mBtnAutoUpgrade;
	private Button mBtnEraseUpgrade;

	class UpdateImsgTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String res = Sys.getResponse(URL);
			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
			if (result.length() < 1) {
				alert(R.string.connect_server_failed);
				return;
			}

			try {
				// a.b(result);
				JSONObject obj = new JSONObject(result);
				JSONArray array = (JSONArray) obj.get("files");
				for (int i = 0; i < array.length(); i++) {
					JSONObject one = array.getJSONObject(i);
					// a.b(one.getString("filename"));
					// a.b(one.getString("version"));
					// a.b(one.getString("url"));
					mSystemAdapter.setImg(one.getString("filename"),
							one.getString("version"), one.getString("url"));
					mSystemAdapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mBtnAutoUpgrade = (Button) findViewById(R.id.btn_auto_upgrade);
			mBtnEraseUpgrade = (Button) findViewById(R.id.btn_erase_upgrade);

			mBtnAutoUpgrade
					.setOnClickListener(new com.dafeng.upgrade.fragment.b.c(
							getActivity(), mSystemAdapter, false));
			mBtnEraseUpgrade
					.setOnClickListener(new com.dafeng.upgrade.fragment.b.c(
							getActivity(), mSystemAdapter, true));
		}

	};

	public View inflaterView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_system, container, false);
	}

	public void setUpView() {
		// mLayoutImgs = findViewById(R.id.layout_imgs);
		mSystemAdapter = new SystemAdapter(getActivity());
		// int t =android.R.layout.simple_list_item_multiple_choice;
		ListView list = (ListView) findViewById(R.id.list_imgs);
		list.setOnTouchListener(new com.dafeng.upgrade.fragment.b.d(mGes));
		list.setAdapter(mSystemAdapter);
	}

	public void startRun() {
		if (!Sys.isOnline(this.getActivity())) {
			alert(R.string.network_not_connect);
			return;
		}
		if (!Sys.isSdPresent()) {
			alert(R.string.sd_not_present);
			return;
		}
		new UpdateImsgTask().execute(URL);
	}
	//
	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// this.getActivity().getMenuInflater()
	// .inflate(R.menu.system_update, menu);
	// }

}
