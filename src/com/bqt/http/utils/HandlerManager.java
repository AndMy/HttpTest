package com.bqt.http.utils;

import android.os.Handler;

/**在UI线程中均可以使用此Handler，如Activity、Service、BroadcastReceiver，他们用的都是同一个对象，可以方便在相互通讯
 * ThreadLocal会为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
 */
public class HandlerManager {
	private static ThreadLocal<Handler> threadLocal = new ThreadLocal<Handler>();

	public static Handler getHandler() {
		return threadLocal.get();//返回当前线程所对应的线程局部变量
	}

	public static void setHandler(Handler mHandler) {
		threadLocal.set(mHandler);//设置当前线程的线程局部变量的值
	}
}