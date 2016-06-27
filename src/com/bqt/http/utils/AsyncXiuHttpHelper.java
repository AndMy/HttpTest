package com.bqt.http.utils;

import org.json.JSONObject;

import com.bqt.http.utils.AsyncHttpHelper.OnHttpListener;
import com.loopj.android.http.RequestParams;

/**AsyncHttp再封装后的简易工具类*/
public class AsyncXiuHttpHelper {

	//	public static final String SERVER_URL = "api.95xiu.com";
	//	public static final String WEB_SERVER_URL = "chat.95xiu.com";
	//	public static final int LIVE_WEB_PORT = 3016;

	public static final String SERVER_URL = "tapi.95xiu.com";
	public static final String WEB_SERVER_URL = "tapi.95xiu.com";
	public static final int LIVE_WEB_PORT = 3014;

	/**
	 * get 请求
	 * @param relativeUrl  相对地址，如"/user/loginv2.php"
	 * @param params   请求参数
	 * @param onHttpListner   成功或失败时的监听，在回调方法中获取返回的数据
	 */
	public static void get(final String relativeUrl, RequestParams params, final OnHttpListener<JSONObject> onHttpListner) {
		params = FormatRequestParams(params);
		AsyncHttpHelper.get(SERVER_URL, relativeUrl, params, onHttpListner);
	}

	/**
	 *  post 请求
	 * @param relativeUrl  相对地址，如"/user/loginv2.php"
	 * @param params   请求参数
	 * @param onHttpListner   成功或失败时的监听，在回调方法中获取返回的数据
	 */
	public static void post(final String relativeUrl, RequestParams params, final OnHttpListener<JSONObject> onHttpListner) {
		params = FormatRequestParams(params);
		AsyncHttpHelper.post(SERVER_URL, relativeUrl, params, onHttpListner);
	}

	/**
	 * 返回添加了基础信息的RequestParams
	 */
	private static RequestParams FormatRequestParams(RequestParams params) {
		if (params == null) params = new RequestParams();
		//params.put("imei", Properties.IMEI);
		//params.put("channel", Properties.CN);
		//params.put("session_id", AppUser.getInstance().getUser().getuSessionId());
		//params.put("version_code", Properties.VERSION_CODE);
		return params;
	}
}