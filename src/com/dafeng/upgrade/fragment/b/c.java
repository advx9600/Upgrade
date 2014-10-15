package com.dafeng.upgrade.fragment.b;

import java.util.ArrayList;
import java.util.List;

import com.dafeng.upgrade.R;
import com.dafeng.upgrade.dao.TbImgs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

public class c extends com.dafeng.upgrade.fragment.b.aa implements
		OnClickListener {

	private BaseAdapter mAdapter;
	private Handler mHandler;
	private DownloadTask mDownload;

	private boolean mIsEraseAll = false;

	public void setEraseAll(boolean erase) {
		mIsEraseAll = erase;
	}

	@SuppressLint("HandlerLeak")
	public c(Context con, BaseAdapter base, boolean eraseAll) {
		mCon = con;
		mAdapter = base;

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mDownload.execute("");
					break;
				case 2:
					DownloadTask.delXmlFiles();
					new AlertDialog.Builder(mCon).setMessage(
							R.string.upgrade_faled).show();
					break;
				case 3:
					DownloadTask.reboot(mCon);
					break;
				case 4:
					DownloadTask.setEraseAllCondition();
					break;
				}
			}
		};
		setEraseAll(eraseAll);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(mCon)
				.setMessage(
						getString(mIsEraseAll ? R.string.sure_erase_all_upgrade
								: R.string.sure_auto_upgrade))
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								TaskManager taskMan = new TaskManager();
								int count = mAdapter.getCount();
								for (int i = 0; i < count; i++) {
									TbImgs img = (TbImgs) mAdapter.getItem(i);
									if (!img.getCurVer().equals(
											img.getLatestVer())) {
										taskMan.addImg(img);
									} else if (mIsEraseAll) {
										taskMan.addImg(img);
									}
								}
								taskMan.start();
							}

						}).show();
	}

	class TaskManager extends Thread {
		List<TbImgs> mListImgs = new ArrayList<TbImgs>();

		public void addImg(TbImgs img) {
			mListImgs.add(img);
		}

		@Override
		public void run() {
			boolean isSuccess = true;
			for (int i = 0; i < mListImgs.size(); i++) {
				mDownload = new DownloadTask(mCon, mListImgs.get(i));
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
				while (mDownload.getStatus() != AsyncTask.Status.FINISHED) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!mDownload.isExeSuccess()) {
					msg.what = 2;
					mHandler.sendMessage(msg);
					isSuccess = false;
					break;
				}
			}
			if (isSuccess && mIsEraseAll) {
				Message msg = new Message();
				msg.what = 4;
				mHandler.sendMessage(msg);
			}

			if (isSuccess) {
				Message msg = new Message();
				msg.what = 3;
				mHandler.sendMessage(msg);
			}
		}
	}
}
