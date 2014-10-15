package com.dafeng.upgrade.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dafeng.upgrade.R;
import com.dafeng.upgrade.dao.TbImgs;
import com.dafeng.upgrade.util.Sys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SystemAdapter extends BaseAdapter {

	public static final String IMG_U_BOOT = "u-boot";
	public static final String IMG_KRENEL = "kernel";
	public static final String IMG_USER_DATA = "userdata";
	public static final String IMG_RAMDISK = "ramdisk";
	public static final String IMG_SYSTEM = "system";

	private Context mContext;
	private List<TbImgs> mData = new ArrayList<TbImgs>();

	public SystemAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		generateData();
	}	

	private void generateData() {
		TbImgs img = new TbImgs();
		img.setName(IMG_U_BOOT);
		img.setCurVer(Sys.UBOOT_VERSION);
		mData.add(img);

		img = new TbImgs();
		img.setName(IMG_KRENEL);
		img.setCurVer(Sys.KERNEL_VERSION);
		mData.add(img);

		for (int i = 0; i < 3; i++) {
			img = new TbImgs();
			String name = "";
			switch (i) {
			case 0:
				name = IMG_SYSTEM;
				break;
			case 1:
				name = IMG_USER_DATA;
				break;
			case 2:
				name = IMG_RAMDISK;
				break;
			}
			img.setName(name);
			img.setCurVer(Sys.SYSTEM_VERSION);
			mData.add(img);
		}
	}

	public void setImg(String imgName, String latestVer, String downUrl) {
		imgName = imgName.split("\\.")[0];
		if (imgName.contains("ramdisk")) {
			imgName = "ramdisk";
		}
		for (int i = 0; i < mData.size(); i++) {
			TbImgs img = mData.get(i);
			if (img.getName().contains(imgName)) {
				img.setLatestVer(latestVer);
				img.setDownloadUrl(downUrl);
			}
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = newView(mContext, position, parent);
		} else {
			v = convertView;
		}
		bindView(v, mContext, position);
		return v;
	}

	private void bindView(View v, Context con, int position) {
		// TODO Auto-generated method stub
		TextView tvName = (TextView) v.findViewById(R.id.img_name);
		TextView tvCurVer = (TextView) v.findViewById(R.id.img_cur_ver);
		TextView tvLatestVer = (TextView) v.findViewById(R.id.img_last_ver);
		Button btnInstall = (Button) v.findViewById(R.id.btn_install);

		final TbImgs img = mData.get(position);
		tvName.setText(img.getName());
		tvCurVer.setText(img.getCurVer());
		if (img.getLatestVer() == null || img.getLatestVer().length() == 0) {
			return;
		}

		tvLatestVer.setText(img.getLatestVer());
		if (img.getCurVer().equals(img.getLatestVer())) {
			btnInstall.setText(R.string.install);
		} else {
			btnInstall.setText(R.string.upgrade);
		}

		btnInstall.setOnClickListener(new com.dafeng.upgrade.fragment.b.b(
				mContext, img));
	}

	private View newView(Context con, int position, ViewGroup root) {
		// TODO Auto-generated method stub
		LayoutInflater inf = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// CheckableItemLayout item= (CheckableItemLayout)
		// inf.inflate(R.layout.item_broad_filelist,root,false);
		return inf.inflate(R.layout.item_broad_filelist, root, false);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// a.b("size:" + mData.size() + ",pos:" + position);
		return 0;
	}

}
