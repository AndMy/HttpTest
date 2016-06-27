package com.bqt.http;

import android.app.Application;

import com.bqt.http.bean.UserBean;

/**用于设置全局变量*/
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
	}

	private static MyApplication mApplication = null;

	public static MyApplication getApplication() {
		return mApplication;
	}

	private UserBean mUser;

	public UserBean getUser() {
		return mUser;
	}

	public void setUser(UserBean mUser) {
		this.mUser = mUser;
	}

	@Override
	public void onTerminate() {//当终止应用程序对象时调用，不保证一定被调用
		super.onTerminate();
		System.gc();
	}
}