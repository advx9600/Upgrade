package com.dafeng.upgrade.fragment.b;

import com.dafeng.upgrade.R;
import com.dafeng.upgrade.dao.TbImgs;
import com.dafeng.upgrade.fragment.a;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

public class b extends com.dafeng.upgrade.fragment.b.aa implements
		OnClickListener {
	private TbImgs mImg;

	public b(Context con, TbImgs img) {
		mCon = con;
		mImg = img;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(mCon)
				.setMessage(getString(R.string.sure_update_img, mImg.getName()))
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								final DownloadTask downloadTask = new DownloadTask(
										mCon, mImg);
								downloadTask.setReboot(true);
								downloadTask.execute(mImg.getDownloadUrl());
							}

						}).show();
	}

}
