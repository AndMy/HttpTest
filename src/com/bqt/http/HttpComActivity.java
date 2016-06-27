package com.bqt.http;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bqt.http.bean.UrlOfServer;
import com.bqt.http.bean.UrlOfPics;
import com.bqt.http.utils.AsyncHttpHelper.OnHttpListener;
import com.bqt.http.utils.AsyncXiuHttpHelper;
import com.bqt.http.utils.HttpUrlClientUtils;
import com.bqt.http.utils.JsonFormatTool;
import com.bqt.http.utils.ProgressDialogUtil;
import com.loopj.android.http.RequestParams;

/**
 * 测试使用三种方式（AsyncHttpClient、httpURLCon、httpClient）分别以get和post方式访问服务器
 * @author 白乾涛
 */
public class HttpComActivity extends ListActivity {
	private TextView tv_result;
	private ImageView imageView;

	private String session_id;//每次登录都不一样
	private int uid;//10415362
	private String picUrl;//每次登录都不一样

	/**拼接的URL地址*/
	private static String URL_HEAD = "http://" + AsyncXiuHttpHelper.SERVER_URL;
	public static final int GET_ASYNC_HTTP = 0;
	public static final int POST_ASYNC_HTTP = 1;
	public static final int GET_HTTP_URL_CON = 2;
	public static final int POST_HTTP_URL_CON = 3;
	public static final int GET_HTTP_CLIENT = 4;
	public static final int POST_HTTP_CLIENT = 5;
	public static final int LOAD_PIC_BY_HANDLER = 6;
	public static final int LOAD_PIC_BY_ASYNCTASK = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		String[] array = { "使用AsyncHttpClient的get方式", "使用AsyncHttpClient的post方式", //
				"使用httpURLCon的get方式", "使用httpURLCon的post方式", //
				"使用httpClient的get方式", "使用httpClient的post方式",//
				"使用httpURLCon+handler加载图片", "使用httpURLCon+AsyncTask加载图片" };
		for (int i = 0; i < array.length; i++) {
			array[i] = i + "、" + array[i];
		}
		imageView = new ImageView(this);
		getListView().addFooterView(imageView);
		tv_result = new TextView(this);// 将内容显示在TextView中
		tv_result.setTextColor(Color.BLUE);
		tv_result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_result.setPadding(20, 10, 20, 10);
		getListView().addFooterView(tv_result);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));

		//初始化
		if (MyApplication.getApplication().getUser() != null) {
			session_id = MyApplication.getApplication().getUser().getuSessionId();//设置完后就可以获取了
			uid = MyApplication.getApplication().getUser().getuId();
			tv_result.setText("session_id=" + session_id + "\n" + "uid=" + uid);
		} else tv_result.setText("请先登录再操作");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		picUrl = UrlOfPics.urls[new Random().nextInt(UrlOfPics.urls.length)];
		switch (position) {
		case GET_ASYNC_HTTP:
			asyncGetNotifaction();
			break;
		case POST_ASYNC_HTTP:
			asyncModifySignature();
			break;
		case GET_HTTP_URL_CON:
			httpURLConGet();
			break;
		case POST_HTTP_URL_CON:
			httpURLConPost();
			break;
		case GET_HTTP_CLIENT:
			httpClientGet();
			break;
		case POST_HTTP_CLIENT:
			httpClientPost();
			break;
		case LOAD_PIC_BY_HANDLER:
			loadPicByHandler();
			break;
		case LOAD_PIC_BY_ASYNCTASK:
			new MyImageLoadTask().execute(picUrl);
			break;
		}
	}

	//**************************************************************************************************************************
	//                                                                 		使用异步框架AsyncHttpClient访问服务器
	//**************************************************************************************************************************

	/**
	 * 获取活动通知数据，get
	 */
	private void asyncGetNotifaction() {
		RequestParams params = new RequestParams();
		params.put("session_id", session_id);
		params.put("uid", uid);

		ProgressDialogUtil.showProgressDialog(HttpComActivity.this, "", "申请中...");
		AsyncXiuHttpHelper.get(UrlOfServer.RQ_NOTIFICATION, params, new OnHttpListener<JSONObject>() {
			@Override
			public void onHttpListener(boolean httpSuccessed, final JSONObject obj) {//运行UI线程中
				ProgressDialogUtil.dismissProgressDialog();
				tv_result.setText(JsonFormatTool.formatJson(obj.toString()));
				new Thread() {//子线程中请求网络
					@Override
					public void run() {
						//final String img = obj.getJSONArray("news").getJSONObject(0).getString("img");
						InputStream stream = HttpUrlClientUtils.getInputStreamFromUrl(picUrl);//这里使用另一张图片演示
						final Bitmap bitmap = BitmapFactory.decodeStream(stream);
						runOnUiThread(new Runnable() {//主线程总更新UI。如果数据比较复杂，应该用handler处理
							@Override
							public void run() {
								imageView.setImageBitmap(bitmap);
							}
						});
					}
				}.start();
			}
		});
	}

	/**
	 * 提交用户个性签名，post
	 */
	private void asyncModifySignature() {
		RequestParams params = new RequestParams();
		params.put("session_id", session_id);
		params.put("uid", uid);
		params.put("sig_data", "签名内容：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

		ProgressDialogUtil.showProgressDialog(HttpComActivity.this, "", "申请中...");
		AsyncXiuHttpHelper.post(UrlOfServer.RQ_USER_SIGNATURE, params, new OnHttpListener<JSONObject>() {
			@Override
			public void onHttpListener(boolean httpSuccessed, JSONObject obj) {
				ProgressDialogUtil.dismissProgressDialog();
				tv_result.setText(JsonFormatTool.formatJson(obj.toString()));
				imageView.setImageResource(R.drawable.ic_launcher);
			}
		});
	}

	//**************************************************************************************************************************
	//                                                                         	使用HttpURLConnection访问服务器
	//**************************************************************************************************************************
	/**
	 * 使用java.net.HttpURLConnection类的【GET】的方式，获取活动通知数据
	 */
	private void httpURLConGet() {
		ProgressDialogUtil.showProgressDialog(HttpComActivity.this, "", "申请中...");
		new Thread(new Runnable() {//子线程中请求网络
					@Override
					public void run() {
						String parameters = "session_id=" + session_id + "&uid=" + uid;//请求参数
						final String strJson = HttpUrlClientUtils.httpURLConGet(URL_HEAD + UrlOfServer.RQ_NOTIFICATION, parameters);

						//final String img = new JSONObject(strJson).getJSONArray("news").getJSONObject(0).getString("img");
						InputStream stream = HttpUrlClientUtils.getInputStreamFromUrl(picUrl);//这里使用另一张图片演示
						final Bitmap bitmap = BitmapFactory.decodeStream(stream);

						runOnUiThread(new Runnable() {//主线程中更新UI
							@Override
							public void run() {
								ProgressDialogUtil.dismissProgressDialog();
								tv_result.setText(JsonFormatTool.formatJson(strJson));
								imageView.setImageBitmap(bitmap);
							}
						});
					}
				}).start();
	}

	/**
	 * 使用java.net.HttpURLConnection类的【POST】的方式，提交用户个性签名
	 */
	private void httpURLConPost() {
		ProgressDialogUtil.showProgressDialog(HttpComActivity.this, "", "申请中...");
		new Thread(new Runnable() {
			@Override
			public void run() {

				String sig_data = "签名内容：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
				String requestBody = "session_id=" + session_id + "&uid=" + uid + "&sig_data=" + sig_data;//请求体中的数据即为get请求URL路径?后面的内容
				final String str = HttpUrlClientUtils.httpURLConPost(URL_HEAD + UrlOfServer.RQ_USER_SIGNATURE, requestBody);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ProgressDialogUtil.dismissProgressDialog();
						imageView.setImageResource(R.drawable.ic_launcher);
						try {
							tv_result.setText(JsonFormatTool.formatJson(new JSONObject(str).toString()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();

	}

	//**************************************************************************************************************************
	//                                                                                  	使用HttpClient访问服务器
	//**************************************************************************************************************************
	/**
	 * 使用org.apache.http.client.HttpClient的【Get】的方式登录
	 */
	private void httpClientGet() {
		ProgressDialogUtil.showProgressDialog(HttpComActivity.this, "", "申请中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				String parameters = "session_id=" + session_id + "&uid=" + uid;//请求参数
				final String strJson = HttpUrlClientUtils.httpClientGet(URL_HEAD + UrlOfServer.RQ_NOTIFICATION, parameters);

				//final String img = new JSONObject(strJson).getJSONArray("news").getJSONObject(0).getString("img");
				InputStream stream = HttpUrlClientUtils.getInputStreamFromUrl(picUrl);//这里使用另一张图片演示
				final Bitmap bitmap = BitmapFactory.decodeStream(stream);

				runOnUiThread(new Runnable() {//主线程中更新UI
					@Override
					public void run() {
						ProgressDialogUtil.dismissProgressDialog();
						tv_result.setText(JsonFormatTool.formatJson(strJson));
						imageView.setImageBitmap(bitmap);
					}
				});
			}
		}).start();
	}

	/**
	 * 使用org.apache.http.client.HttpClient的【Post】的方式登录
	 */
	private void httpClientPost() {
		ProgressDialogUtil.showProgressDialog(HttpComActivity.this, "", "申请中...");
		new Thread(new Runnable() {
			@Override
			public void run() {

				String sig_data = "签名内容：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
				// 设置post请求的参数，代表发送给服务器的实体中的内容
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("session_id", session_id));
				parameters.add(new BasicNameValuePair("uid", uid + ""));
				parameters.add(new BasicNameValuePair("sig_data", sig_data));

				//参数的类型为【HttpEntity】接口，其实现类UrlEncodedFormEntity中的元素为键值对形式的【BasicNameValuePair】对象
				final String str = HttpUrlClientUtils.httpClientPost(URL_HEAD + UrlOfServer.RQ_USER_SIGNATURE, parameters);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ProgressDialogUtil.dismissProgressDialog();
						imageView.setImageResource(R.drawable.ic_launcher);
						try {
							tv_result.setText(JsonFormatTool.formatJson(new JSONObject(str).toString()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	public static final int MSG_WHAT_BITMAP = 1;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WHAT_BITMAP:
				imageView.setImageBitmap((Bitmap) (msg.obj));
				break;
			}
		}
	};

	private void loadPicByHandler() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				InputStream inputStream = HttpUrlClientUtils.getInputStreamFromUrl(picUrl);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				mHandler.sendMessage(Message.obtain(mHandler, MSG_WHAT_BITMAP, bitmap));
			}
		}).start();
	}

	class MyImageLoadTask extends AsyncTask<String, Bitmap, Bitmap> {
		protected Bitmap doInBackground(String... params) {
			InputStream inputStream = HttpUrlClientUtils.getInputStreamFromUrl(params[0]);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			imageView.setImageBitmap(result);

		}
	}
}