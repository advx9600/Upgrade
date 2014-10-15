package com.dafeng.upgrade.fragment.b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dafeng.upgrade.dao.TbImgs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<String, Integer, String> {

	private Context context;
	private PowerManager.WakeLock mWakeLock;
	ProgressDialog mProgressDialog;
	public static final String SAVE_PATH = "/sdcard/sdfuse";
	private TbImgs mImg;
	private boolean mNeedReboot = false;
	private boolean mExeSuccess = false;	

	public DownloadTask(Context context, TbImgs img) {
		this.context = context;
		this.mImg = img;
	}

	public void setReboot(boolean b) {
		mNeedReboot = b;
	}

	public boolean isExeSuccess() {
		return mExeSuccess;
	}

	@Override
	protected String doInBackground(String... sUrl) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			// URL url = new URL(sUrl[0]);
			URL url = new URL(mImg.getDownloadUrl());
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return "Server returned HTTP " + connection.getResponseCode()
						+ " " + connection.getResponseMessage();
			}

			// this will be useful to display download percentage
			// might be -1: server did not report the length
			int fileLength = connection.getContentLength();

			// download the file
			input = connection.getInputStream();

			output = new FileOutputStream(SAVE_PATH
					+ mImg.getDownloadUrl().substring(
							mImg.getDownloadUrl().lastIndexOf("/")));

			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				// allow canceling with back button
				if (isCancelled()) {
					input.close();
					return null;
				}
				total += count;
				// publishing the progress....
				if (fileLength > 0) // only if total length is known
					publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}
		} catch (Exception e) {
			return e.toString();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			if (connection != null)
				connection.disconnect();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass()
				.getName());
		mWakeLock.acquire();

		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(mImg.getName());
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						cancel(true);
					}
				});
		mProgressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		// if we get here, length is known, now set indeterminate to false
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		mWakeLock.release();
		mProgressDialog.dismiss();
		if (result != null)
			Toast.makeText(context, "Download error: " + result,
					Toast.LENGTH_LONG).show();
		else {
			// Toast.makeText(context, "File downloaded",
			// Toast.LENGTH_SHORT)
			// .show();
			File tempFile = new File(mImg.getDownloadUrl());
			File file = new File(SAVE_PATH + "/"
					+ tempFile.getName().split("\\.")[0] + ".xml");
			try {
				file.createNewFile();
				mExeSuccess = true;
				if (mNeedReboot)
					reboot(context);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void reboot(Context con) {
		Intent intent = new Intent("com.system.action.reboot");
		con.sendBroadcast(intent);
	}

	public static void delXmlFiles() {
		String[] files = { "nand_erase.xml", "u-boot.xml", "kernel.xml",
				"ramdisk-yaffs.xml", "userdata.xml", "system.xml" };
		for (int i = 0; i < files.length; i++) {
			File file = new File(DownloadTask.SAVE_PATH + "/" + files[i]);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public static void setEraseAllCondition() {
		File file = new File(SAVE_PATH + "/nand_erase.xml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}