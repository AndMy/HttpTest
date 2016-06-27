package com.bqt.http.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import org.apache.http.Header;

import android.util.Log;

import com.bqt.http.bean.FormFileBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/** 上传文件工具类，千万不要花太多时间再这上面，实际开发中不可能用这种方式的！ */
public class HttpUploadFilesUtils {
	public static final String BOUNDARY = UUID.randomUUID().toString(); //边界标识，随机生成
	public static final String PREFIX = "--";
	public static final String LINEND = "\r\n";//是【"\r\n"】不是【"/r/n"】
	public static final String MULTIPART_FROM_DATA = "multipart/form-data";
	public static final String CHARSET = "UTF-8";
	protected static final String TAG = "HttpUploadFilesUtils";

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 */
	public static String post(String actionUrl, Map<String, String> params, Map<String, File> files) {

		try {
			URL uri = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(3000);
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

			// 首先组拼String类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX + BOUNDARY + LINEND);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			//将java基本数据类型写入【数据输出流DataOutputStream】中，并可以通过【数据输入流DataInputStream】将数据读入
			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());//创建一个将数据写入指定输出流OutputStream的数据输出流
			outStream.write(sb.toString().getBytes());//将数据写入上述构造方法中传入的输出流
			//然后组拼File类型的参数
			if (files != null) {
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX + BOUNDARY + LINEND);
					// name是post中传参的键，filename是文件的名称
					sb1.append("Content-Disposition: form-data; name=\"file1\"; filename=\"" + file.getKey() + "\"" + LINEND);
					sb1.append("Content-Type: multipart/form-data" + LINEND);
					sb1.append(LINEND);

					outStream.write(sb1.toString().getBytes());
					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}

				// 请求结束标志
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
				outStream.write(end_data);
				outStream.flush();
				// 得到响应码
				int res = conn.getResponseCode();
				Log.i("bqt", "++++++" + res);//500，服务器异常
				InputStream inputStream = conn.getInputStream();
				Log.i("bqt", "++++++" + 3);
				if (res == 200) {
					int ch;
					StringBuilder sb2 = new StringBuilder();
					while ((ch = inputStream.read()) != -1) {
						sb2.append((char) ch);
					}
					Log.i("bqt", "++++++" + sb2.toString());
					return sb2.toString();
				}
				outStream.close();
				conn.disconnect();
				return "响应失败" + res;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "MalformedURLException";
		} catch (ProtocolException e) {
			e.printStackTrace();
			return "ProtocolException";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "FileNotFoundException";
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		}
		return "失败";
	}

	/** 
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能: 
	 */
	public static boolean uploadFiles(String path, Map<String, String> params, FormFileBean[] files) throws Exception {
		final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线  
		final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志  

		int fileDataLength = 0;
		if (files != null && files.length != 0) {
			for (FormFileBean uploadFile : files) {//得到文件类型数据的总长度  
				StringBuilder fileExplain = new StringBuilder();
				fileExplain.append("--");
				fileExplain.append(BOUNDARY);
				fileExplain.append("\r\n");
				fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
				fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
				fileExplain.append("\r\n");
				fileDataLength += fileExplain.length();
				if (uploadFile.getInStream() != null) {
					fileDataLength += uploadFile.getFile().length();
				} else {
					fileDataLength += uploadFile.getData().length;
				}
			}
		}
		StringBuilder textEntity = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据  
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
		}
		//计算传输给服务器的实体数据总长度  
		int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		int port = url.getPort() == -1 ? 80 : url.getPort();
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		OutputStream outStream = socket.getOutputStream();
		//下面完成HTTP请求头的发送  
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		outStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		String alive = "Connection: Keep-Alive\r\n";
		outStream.write(alive.getBytes());
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());
		//写完HTTP请求头后根据HTTP协议再写一个回车换行  
		outStream.write("\r\n".getBytes());
		//把所有文本类型的实体数据发送出来  
		outStream.write(textEntity.toString().getBytes());
		//把所有文件类型的实体数据发送出来  
		if (files != null && files.length != 0) {
			for (FormFileBean uploadFile : files) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append("--");
				fileEntity.append(BOUNDARY);
				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
				fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
				outStream.write(fileEntity.toString().getBytes());
				if (uploadFile.getInStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
						outStream.write(buffer, 0, len);
					}
					uploadFile.getInStream().close();
				} else {
					outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
				}
				outStream.write("\r\n".getBytes());
			}
		}
		//下面发送数据结束标志，表示数据已经结束  
		outStream.write(endline.getBytes());

		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String codeString = reader.readLine();
		outStream.flush();
		outStream.close();
		reader.close();
		socket.close();
		if (codeString.indexOf("200") == -1) {//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败  
			return false;
		}
		return true;
	}

	//**************************************************************************************************************************
	//通过HttpURLConnection手动完成浏览器帮我们做的事，对传输的数据进行规范化的拼接
	public static String uploadByHttpURLConnection(String filePath, String strUrl) {
		int TIME_OUT = 5 * 1000; //超时时间
		String CHARSET = "utf-8"; //设置编码
		String result = null;//返回响应的内容
		String BOUNDARY = UUID.randomUUID().toString(); //边界标识，随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; //内容类型
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); //允许输入流
			conn.setDoOutput(true); //允许输出流
			conn.setUseCaches(false); //不允许使用缓存
			conn.setRequestMethod("POST"); //请求方式
			conn.setRequestProperty("Charset", CHARSET); //设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			//conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//当文件不为空，把文件包装并且上传
			File file = new File(filePath);
			if (file.exists() && file.length() > 0) {
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				//这里重点注意：name里面的值为服务器端需要key,只有这个key 才可以得到对应的文件,filename是文件的名字，包含后缀名的
				sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				//获取响应码  200=成功,当响应成功，获取响应的流  
				int res = conn.getResponseCode();
				Log.e(TAG, "response code:" + res);
				if (res == 200) {
					Log.e(TAG, "成功");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					Log.e(TAG, "result : " + result);
				} else Log.e(TAG, "失败");
			} else Log.e(TAG, "文件不存在" + filePath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	//**************************************************************************************************************************
	//采用第三方异步框架AsynchHttpClient上传文件
	public static void uploadByAsyncHttpClient(String filePath, String strUrl) {
		File file = new File(filePath);
		if (file.exists() && file.length() > 0) {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			RequestParams requestParams = new RequestParams();
			try {
				requestParams.put("profile_picture", file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			asyncHttpClient.post(strUrl, requestParams, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					Log.e(TAG, "成功");
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					Log.e(TAG, "失败");
				}
			});
		} else Log.e(TAG, "文件不存在" + filePath);
	}
}