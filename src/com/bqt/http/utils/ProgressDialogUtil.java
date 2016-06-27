package com.bqt.http.utils;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

import com.bqt.http.view.ProgressDialogView;

/**显示、隐藏进度对话框的工具类*/
public class ProgressDialogUtil {

	private static ProgressDialogView progressDialog;

	/**显示自定义的进度对话框，不可取消*/
	public static void showProgressDialog(Context context, String title, String message) {
		dismissProgressDialog();
		progressDialog = new ProgressDialogView(context);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	/**显示自定义的进度对话框*/
	public static void showProgressDialog(Context context, String title, String message, boolean cancelable, OnCancelListener cancelListener) {
		dismissProgressDialog();
		progressDialog = new ProgressDialogView(context);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(cancelable);
		progressDialog.setOnCancelListener(cancelListener);
		progressDialog.show();
	}

	/** 取消显示带进度条的对话框 */
	public static void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			try {
				progressDialog.dismiss();
			} catch (IllegalArgumentException e) {
			}
		}
		progressDialog = null;
	}
}