package com.bqt.http;

import com.bqt.http.utils.HandlerManager;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/** 注册一个广播接收器，当下载完毕后会收到一个android.intent.action.DOWNLOAD_COMPLETE的广播，在这里取出队列里下载任务，进行安装*/
public class DownloadCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
		Handler mHandler = HandlerManager.getHandler();
		mHandler.sendMessage(Message.obtain(mHandler, DownloadManagerActivity.MSG_WHAT_DOWNLOAD_ID, id));
		if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			Toast.makeText(context, "编号 " + id + " 的下载任务已经完成！", Toast.LENGTH_SHORT).show();
		} else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
			Toast.makeText(context, "别瞎点-" + id, Toast.LENGTH_SHORT).show();
		} else if (intent.getAction().equals(DownloadManager.ACTION_VIEW_DOWNLOADS)) {
			Toast.makeText(context, "查看下载项-" + id, Toast.LENGTH_SHORT).show();
		}
		//如果下载的是APK文件，则自动安装
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		Query query = new DownloadManager.Query();
		query.setFilterById(id);
		Cursor cursor = downloadManager.query(query);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
				String name = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME));
				String uri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
				String end = name.substring(name.lastIndexOf("."));
				if (end != null && end.equals(".apk") && status == DownloadManager.STATUS_SUCCESSFUL) {
					Intent mIntent = new Intent(Intent.ACTION_VIEW);
					mIntent.setDataAndType(Uri.parse(uri), "application/vnd.android.package-archive");
					mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(mIntent);
				}
			}
			cursor.close();
		}
	}
}