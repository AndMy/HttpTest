package com.bqt.http.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/** 
 * HttpURLConnection和HttpClient工具类
 * HttpURLConnection是java.net中的类，属于标准的java接口，在4.4版本的源码中被OkHttp替换掉了；HttpClient则是Apache提供的，已经被Google弃用了。
 * 一般我们实际开发中都是使用别人封装好的第三方网络请求框架，诸如：Volley，android-async-http，loopj等。
 * */
public class HttpUrlClientUtils {
	//*********************************************************************************************************************************************
	//																										         HttpURLConnection
	//*********************************************************************************************************************************************
	/** 使用java.net.HttpURLConnection类的【GET】的方式登录	 */
	public static String httpURLConGet(String strUrl, String parameters) {
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(strUrl + "?" + parameters);//"?"后面的内容都属于请求头中的内容，服务器获取到请求信息后通过"&"分离出数据
			httpURLConnection = (HttpURLConnection) url.openConnection();//打开指定URL的连接
			httpURLConnection.setRequestMethod("GET"); // get或者post必须大写
			httpURLConnection.setConnectTimeout(3000); // 连接的超时时间，非必须
			httpURLConnection.setReadTimeout(3000); // 读数据的超时时间，非必须
			httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");//非必须

			int responseCode = httpURLConnection.getResponseCode();//获取响应码
			if (responseCode == 200) {
				InputStream is = httpURLConnection.getInputStream();
				String result = getStringFromInputStream(is);
				return result;
			} else return "失败";
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "MalformedURLException";
		} catch (ProtocolException e) {
			e.printStackTrace();
			return "ProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		} finally {
			if (httpURLConnection != null) httpURLConnection.disconnect(); // 关闭连接
		}
	}

	/** 使用java.net.HttpURLConnection类的【POST】的方式登录	 */
	public static String httpURLConPost(String strUrl, String requestBody) {
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(strUrl);//区别1，URL不同，POST仅包含路径，不包含?后面的内容
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true); // 必须设置此方法，默认情况下, 系统不允许向服务器输出内容
			httpURLConnection.setConnectTimeout(3000); // 连接的超时时间，非必须
			httpURLConnection.setReadTimeout(3000); // 读数据的超时时间，非必须
			httpURLConnection.setUseCaches(false);//Post方式不能缓存，非必须

			// 添加请求头，非必须
			httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");//浏览器
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");//编码
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//数据类型
			//	httpURLConnection.setRequestProperty("Content-Length", requestBody.length() + "");//不知道为什么，设置此属性后会报ProtocolException！

			OutputStream out = httpURLConnection.getOutputStream();// 获得一个输出流， 用于向服务器写数据
			out.write(requestBody.getBytes());//采用的是平台默认的字符集编码UTF-8，请求体中的数据即为get请求URL路径?后面的内容	
			out.close();
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == 200) {
				InputStream is = httpURLConnection.getInputStream();
				String state = getStringFromInputStream(is);
				return state;
			} else return "失败";
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "MalformedURLException";
		} catch (ProtocolException e) {
			e.printStackTrace();
			return "ProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		} finally {
			if (httpURLConnection != null) httpURLConnection.disconnect();
		}
	}

	//*********************************************************************************************************************************************
	//																										         HttpClient
	//*********************************************************************************************************************************************
	/** 使用org.apache.http.client.HttpClient的【Get】的方式登录	 */
	public static String httpClientGet(String strUrl, String parameters) {
		HttpClient httpClient = null;
		try {
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(strUrl + "?" + parameters);
			HttpResponse response = httpClient.execute(httpGet); //HttpResponse对象中包含了服务器响应的全部信息
			int statusCode = response.getStatusLine().getStatusCode();// 获得服务器响应中的【响应行中的响应码】
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();//获取实体内容
				String text = getStringFromInputStream(is);
				return text;
			} else return "失败";
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "IllegalStateException";
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		} finally {
			if (httpClient != null) httpClient.getConnectionManager().shutdown(); // 关闭连接, 释放资源
		}
	}

	/** 使用org.apache.http.client.HttpClient的【Post】的方式登录	 */
	public static String httpClientPost(String strUrl, List<NameValuePair> parameters) {
		HttpClient httpClient = null;
		try {
			httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(strUrl);
			HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
			httpPost.setEntity(entity);//设置实体内容
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				InputStream is = response.getEntity().getContent();
				String text = getStringFromInputStream(is);
				return text;
			} else return "失败";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "UnsupportedEncodingException";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "ClientProtocolException";
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "IllegalStateException";
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		} finally {
			if (httpClient != null) httpClient.getConnectionManager().shutdown(); // 关闭连接和释放资源
		}
	}

	//*********************************************************************************************************************************************
	//																										          其他方法
	//*********************************************************************************************************************************************
	/**从流中读取数据，返回字节数组*/
	public static byte[] getBytesFromInputStream(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// 字节数组输出流(内存输出流)：可以捕获内存缓冲区的数据，转换成字节数组
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			is.close();
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**  将输入流转换成字符串信息 */
	public static String getStringFromInputStream(InputStream is) {
		byte[] bytes = getBytesFromInputStream(is);
		String temp = new String(bytes);//默认为UTF8编码
		if (temp.contains("utf-8")) return temp;
		else if (temp.contains("gb2312") || temp.contains("gbk")) {
			try {
				return new String(bytes, "gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	/** 打开指定URL的URLConnection对象，获取一个InputStream */
	public static InputStream getInputStreamFromUrl(String url) {
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(3 * 1000);
			conn.setReadTimeout(3 * 1000);
			return conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}