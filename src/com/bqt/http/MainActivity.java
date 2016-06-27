package com.bqt.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/** 主界面 */
public class MainActivity extends ListActivity {
	private List<String> mData;//数据
	private ListAdapter mAdapter;//适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = new ArrayList<String>(Arrays.asList("使用账号密码登录", "传入账户密码后自动登录", "通过http的get和post方式与服务器通讯",//
				"从相机或图例获取、裁剪图片，并上传服务器", "使用HttpURLConnection（多线程）下载文件", "使用DownloadManager下载文件"));
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
		setListAdapter(mAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case 1:
			Bundle loginBundle = new Bundle();
			loginBundle.putString("account", "103468");
			loginBundle.putString("password", "103468");
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtras(loginBundle);
			startActivity(intent);
			break;
		case 2:
			startActivity(new Intent(this, HttpComActivity.class));
			break;
		case 3:
			startActivity(new Intent(this, UploadFileActivity.class));
			break;
		case 4:
			startActivity(new Intent(this, HttpDownloadActivity.class));
			break;
		case 5:
			startActivity(new Intent(this, DownloadManagerActivity.class));
			break;
		}
	}
}