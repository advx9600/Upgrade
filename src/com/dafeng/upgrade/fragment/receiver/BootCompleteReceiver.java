package com.dafeng.upgrade.fragment.receiver;


import com.dafeng.upgrade.fragment.a;
import com.dafeng.upgrade.fragment.b.DownloadTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			a.b("receive ACTION_BOOT_COMPLETED broadcast");
			DownloadTask.delXmlFiles();
		}
	}
}
