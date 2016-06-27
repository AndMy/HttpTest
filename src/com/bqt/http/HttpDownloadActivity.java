package com.bqt.http;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bqt.http.utils.HttpDownloadFilesUtils;

/**实际开发涉及文件上传、下载都不会自己写这些代码，一般会使用第三方库（如xUtils）或Android提供的DownloadManager下载*/
public class HttpDownloadActivity extends ListActivity {
	private TextView tv_info;
	private LinearLayout ll_pbs;

	public static final String PATH_URL_SMALL = "http://f2.market.xiaomi.com/download/AppStore/0b6c25446ea80095219649f646b8d67361b431127/com.wqk.wqk.apk";
	public static final String PATH_URL_BIG = "http://f3.market.xiaomi.com/download/AppChannel/099d2b4f6006a4c883059f459e0025a3e1f25454e/com.pokercity.bydrqp.mi.apk";
	public static final String PATH_FILE = Environment.getExternalStorageDirectory().getPath() + File.separator + "bqt_download" + File.separator;

	/**下载完毕后安装下载的APK*/
	public static final int MSG_WHAT_DOWNLOAD_OK = 1;
	/**下载过程中更新信息*/
	public static final int MSG_WHAT_DOWNLOAD_INFO = 2;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WHAT_DOWNLOAD_OK:
				Toast.makeText(HttpDownloadActivity.this, "下载完毕，请安装", Toast.LENGTH_SHORT).show();
				tv_info.append("\n路径为：" + (String) msg.obj);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + (String) msg.obj), "application/vnd.android.package-archive");
				startActivity(intent);
				break;
			case MSG_WHAT_DOWNLOAD_INFO:
				tv_info.append((String) msg.obj);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		String[] array = { "使用HttpURLConnection单线程下载文件", "使用HttpURLConnection多线程下载文件", "使用开源框架下载文件" };

		tv_info = new TextView(this);
		tv_info.setTextColor(Color.BLUE);
		tv_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_info.setPadding(20, 10, 20, 10);
		getListView().addFooterView(tv_info);
		ll_pbs = new LinearLayout(this);
		ll_pbs.setOrientation(LinearLayout.VERTICAL);
		getListView().addFooterView(ll_pbs);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));
		File directory = new File(PATH_FILE);
		if (!directory.exists()) directory.mkdirs();//必须有这一步
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0://使用HttpURLConnection单线程下载文件
			tv_info.setText("下载过程信息：");
			new Thread() {
				@Override
				public void run() {
					HttpDownloadFilesUtils.simpleDownLoad(PATH_URL_SMALL, PATH_FILE, false, mHandler);
				}
			}.start();
			break;
		case 1://使用HttpURLConnection多线程下载文件
			tv_info.setText("下载过程信息：");
			ll_pbs.removeAllViews();//清空掉旧的进度条
			final ArrayList<ProgressBar> pbs = new ArrayList<ProgressBar>();//ProgressBar、SeekBar、ProgressDialog 这些都是可以在子线程直接更新进度的
			for (int j = 0; j < HttpDownloadFilesUtils.THREAD_COUNT; j++) {
				ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
				ll_pbs.addView(progressBar);//添加到布局中
				pbs.add(progressBar);//添加到集合中
			}

			new Thread() {
				@Override
				public void run() {
					HttpDownloadFilesUtils.mutileThreadDownload(PATH_URL_BIG, PATH_FILE, false, mHandler, pbs);
				}
			}.start();
			break;
		case 2:
			Toast.makeText(this, "请引用第三个库或jar包后自行测试", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}