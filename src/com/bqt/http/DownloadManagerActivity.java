package com.bqt.http;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.bqt.http.utils.HandlerManager;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadManagerActivity extends ListActivity {
	public static final String URL_SMALL_FILE = "http://f2.market.xiaomi.com/download/AppStore/0b6c25446ea80095219649f646b8d67361b431127/com.wqk.wqk.apk";
	public static final String URL_BIG_FILE = "http://f3.market.xiaomi.com/download/AppChannel/099d2b4f6006a4c883059f459e0025a3e1f25454e/com.pokercity.bydrqp.mi.apk";

	private DownloadCompleteReceiver receiver;
	private TextView tv_info;
	private int status = 1;
	public static final int MSG_WHAT_DOWNLOAD_ID = 1;
	private boolean isQueryDownTaskById = false;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WHAT_DOWNLOAD_ID:
				if (isQueryDownTaskById) {
					long id = (long) msg.obj;
					tv_info.append("查询结果\n");
					Map<String, String> map = queryDownTaskById(DownloadManagerActivity.this, id);
					for (Map.Entry<String, String> k_v : map.entrySet()) {//增强for遍历键值对
						tv_info.append(k_v + "\n");
					}
				}
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = { "注册接收DownloadManager三个广播", "取消注册广播接收者", //
				"下载文件到SD卡下的Download目录", "下载文件到SD卡指定目录", "下载文件到/data/data/包名/files/目录", //
				"通过状态查询下载任务", "通过id查询下载任务", };
		for (int i = 0; i < array.length; i++) {
			array[i] = i + "、" + array[i];
		}
		tv_info = new TextView(this);// 将内容显示在TextView中
		tv_info.setTextColor(Color.BLUE);
		tv_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_info.setPadding(20, 10, 20, 10);
		getListView().addFooterView(tv_info);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));
		//加入到ThreadLocal中
		HandlerManager.setHandler(mHandler);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			if (receiver == null) receiver = new DownloadCompleteReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);//下载完成的动作
			intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);//当用户单击notification中下载管理的某项时触发
			intentFilter.addAction(DownloadManager.ACTION_VIEW_DOWNLOADS);//查看下载项
			registerReceiver(receiver, intentFilter);
			break;
		case 1:
			if (receiver != null) {
				unregisterReceiver(receiver);
				receiver = null;
			}
			break;
		case 2:
			simpleDownLoadFileToSdNoUI(this, URL_SMALL_FILE);
			break;
		case 3:
			downLoadFile(this, URL_BIG_FILE, "包青天", new Random().nextInt(1000) + ".apk", DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED, //
					DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE, true);
			break;
		case 4:
			simpleDownLoadFileToDataNoUI(this, URL_SMALL_FILE);
			break;
		case 5:
			tv_info.setText("\n" + getStatusString(status) + "    的任务有：");
			List<Map<String, String>> runningList = queryDownTaskByStatus(this, status);//共有五种状态
			// 遍历
			for (Map<String, String> hm : runningList) {
				tv_info.append("\n**********************************");
				Set<String> set = hm.keySet();
				for (String key : set) {
					String value = hm.get(key);
					tv_info.append("\n" + key + "-" + value);
				}
			}
			//下一个状态
			status = status << 1;
			if (status > (1 << 4)) status = 1;
			break;
		case 6:
			isQueryDownTaskById = true;
			break;
		}
	}

	public static String getStatusString(int status) {
		switch (status) {
		case DownloadManager.STATUS_FAILED:
			return "失败";
		case DownloadManager.STATUS_PAUSED:
			return "暂停";
		case DownloadManager.STATUS_PENDING:
			return "等待";
		case DownloadManager.STATUS_RUNNING:
			return "正在下载";
		case DownloadManager.STATUS_SUCCESSFUL:
			return "下载成功";
		default:
			return "没有这种状态";
		}
	}

	public static void simpleDownLoadFileToSdNoUI(Context mContext, String url) {
		downLoadFile(mContext, url, null, null, DownloadManager.Request.VISIBILITY_HIDDEN, DownloadManager.Request.NETWORK_WIFI, true);
	}

	public static void simpleDownLoadFileToDataNoUI(Context mContext, String url) {
		downLoadFile(mContext, url, null, null, DownloadManager.Request.VISIBILITY_HIDDEN, DownloadManager.Request.NETWORK_WIFI, false);
	}

	/**
	 * 使用DownloadManager下载文件
	 * @param mContext	上下文
	 * @param url				文件路径，请自行确保路径正确
	 * @param filePath		子目录名，设为null则不创建子目录
	 * @param fileName	文件名，设为null则使用服务器路径中的文件名
	 * @param visibility		通知显示的类型，请使用DownloadManager.Request中定义的常量，设置为不显示需要权限DOWNLOAD_WITHOUT_NOTIFICATION
	 * @param networkType	只允许在指定的网络类型下下周，请使用DownloadManager.Request中定义的常量，可以使用"|"运算
	 * @param isToSdCard	是否保存到SD卡上，设为false则保存到【/data/data/包名/files/】目录下
	 */
	public static void downLoadFile(Context mContext, String url, String filePath, String fileName, int visibility, int networkType, boolean isToSdCard) {
		if (url == null || url == "") url = "http://www.sinaimg.cn/dy/slidenews/3_img/2016_22/77542_379697_224394.jpg";
		if (filePath == null || filePath == "") filePath = Environment.DIRECTORY_DOWNLOADS;
		if (fileName == null || fileName == "") fileName = url.substring(url.lastIndexOf("/") + 1);//截取文件名及后缀名
		if (fileName == null || fileName == "") fileName = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss", Locale.CHINA).format(new Date());//截取失败时自动命名
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url)).setNotificationVisibility(visibility).setAllowedNetworkTypes(networkType);
		if (isToSdCard) request.setDestinationInExternalPublicDir(filePath, fileName);//以SD卡路径为根路径
		else request.setDestinationInExternalFilesDir(mContext, filePath, fileName);//以【/data/data/包名/files/】为根路径
		((DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);//将下载请求放入队列
	}

	public static List<Map<String, String>> queryDownTaskByStatus(Context mContext, int status) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterByStatus(status);
		Cursor cursor = ((DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE)).query(query);
		if (cursor != null) {
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
				String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
				String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
				String name = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME));
				//String mStatu = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
				String sizeNow = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));//已下载
				String sizeTotal = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("title", title);
				map.put("name", name);
				map.put("uri", uri);
				map.put("status", sizeTotal + ":" + sizeNow);
				data.add(map);
			}
			cursor.close();
			return data;
		}
		return null;
	}

	public static Map<String, String> queryDownTaskById(Context mContext, long id) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(id);
		Cursor cursor = ((DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE)).query(query);
		if (cursor != null) {
			Map<String, String> data = new HashMap<String, String>();
			if (cursor.moveToNext()) {
				String sizeNow = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
				String sizeTotal = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				data.put("sizeNow", sizeNow);
				data.put("sizeTotal", sizeTotal);
			}
			cursor.close();
			return data;
		}
		return null;
	}
}