package com.bqt.http;

/**第一步，定义一个接口，在接口中定义需要回调的方法*/
public interface LoginInterface {
	/**登录成功或失败后回调*/
	public void loginComplete(boolean isSuccess, String result);
}