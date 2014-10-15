package com.dafeng.upgrade.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dafeng.upgrade.fragment.a;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;
import android.support.v4.app.FragmentActivity;

public class Sys {
	public static String SYSTEM_VERSION = SystemProperties.get(
			"ro.system.version", "");

	public static String BOARD = SystemProperties.get("ro.product.board", "");

	public static String UBOOT_VERSION = getVer("boot");

	public static String KERNEL_VERSION = getVer("kernel").split(" ")[1];

	public static String MACHINE_TYPE = getVer("kernel").split(" ")[0];

	private static String getVer(String type) {
		String str = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/android_kernel"),
					256);
			str = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		type = type.toLowerCase(Locale.ENGLISH);
		String[] splitStr = str.split(",");
		if (splitStr[0].toLowerCase(Locale.ENGLISH).contains(type)) {
			return splitStr[0].split("=")[1];
		}

		return splitStr[1].split("=")[1];
	}

	public static String getResponse(String url) {
		String responseStr = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseStr = out.toString();
				// ..more logic
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseStr;
	}

	public static boolean isOnline(Activity activity) {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean isSdPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
}
