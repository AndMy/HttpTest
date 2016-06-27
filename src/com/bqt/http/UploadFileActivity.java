package com.bqt.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bqt.http.bean.UrlOfServer;
import com.bqt.http.utils.AsyncHttpHelper.OnHttpListener;
import com.bqt.http.utils.AsyncXiuHttpHelper;
import com.bqt.http.utils.HttpUploadFilesUtils;
import com.bqt.http.utils.HttpUrlClientUtils;
import com.bqt.http.utils.JsonFormatTool;
import com.bqt.http.utils.ProgressDialogUtil;
import com.loopj.android.http.RequestParams;

/**实际开发涉及文件上传、下载都不会自己写这些代码，一般会集成第三方库来完成，比如android-async-http，okhttp，xUtils等*/
public class UploadFileActivity extends ListActivity {
	private ImageView imageView;
	private TextView tv_info;

	public static final int uid = 10415362;
	private String session_id = "若看到此内容，说明你还没登录";//每次登录都不一样
	public static final String path = Environment.getExternalStorageDirectory().getPath() + File.separator;
	private Bitmap bitmap;
	/**裁剪图片后的返回码*/
	public static final int REQUEST_CROP_IMG_CODE_TRUE = 100;
	public static final int REQUEST_CROP_IMG_CODE_FALSE = 101;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		String[] array = { "加载我的头像", "使用相机拍照并【获取】照片", "使用相机拍照并【裁剪】照片", "从图库获取并裁剪照片", "从图库获取并裁剪照片2", //
				"使用AsyncHttpClient上传图片", "通过HttpUrlCon拼接或HTTP协议(失败)" };
		for (int i = 0; i < array.length; i++) {
			array[i] = i + "、" + array[i];
		}
		imageView = new ImageView(this);
		getListView().addFooterView(imageView);
		tv_info = new TextView(this);
		tv_info.setTextColor(Color.BLUE);
		tv_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_info.setPadding(20, 10, 20, 10);
		getListView().addFooterView(tv_info);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));

		//初始化
		if (MyApplication.getApplication().getUser() != null) session_id = MyApplication.getApplication().getUser().getuSessionId();
		tv_info.setText("session_id=" + session_id);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			loadMyImg();
			break;
		case 1:
			startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 11);//调用系统相机拍照
			break;
		case 2:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path + "temp.png")));//将返回的数据保存到指定位置
			startActivityForResult(intent, 12);
			break;
		case 3:
			Intent intent3 = new Intent(Intent.ACTION_PICK);
			intent3.setType("image/*");
			startActivityForResult(intent3, 13);
			break;
		case 4:
			Intent intent4 = new Intent(Intent.ACTION_PICK);
			intent4.setType("image/*");
			startActivityForResult(intent4, 14);
			break;
		case 5:
			asyncUploadFile();
			break;
		case 6:
			httpURLConUploadFile();
			break;
		}
	}

	/**加载我的头像，也即刚上传的图片*/
	private void loadMyImg() {
		if (MyApplication.getApplication().getUser() != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String imgUrl = MyApplication.getApplication().getUser().getuAvatarUrl();//头像存放位置
					InputStream inputStream = HttpUrlClientUtils.getInputStreamFromUrl(imgUrl);
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					mHandler.sendMessage(Message.obtain(mHandler, MSG_WHAT_BITMAP, bitmap));
				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 11:// 数据通过onActivityResult中的Intent返回。返回的并非原图，照片会自动进行压缩！
			if (data == null) return;
			bitmap = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(bitmap);
			tv_info.setText("返回的Bitmap尺寸为" + bitmap.getWidth() + " * " + bitmap.getHeight());
			break;
		case 12://数据通过指定的Uri返回。注意，由于数据已返回到其他地方（这里是保存到了SD卡中），所以这里返回的data == null
			tv_info.setText("照片已保存到指定的Uri中－" + (data == null));
			cropImageReturnTrue(this, Uri.fromFile(new File(path + "temp.png")), 72 * 4, 72 * 4);
			break;
		case 13://数据通过系统默认的Uri返回。data.getData()的结果为【content://media/external/images/media/15767】
			if (data == null) return;
			tv_info.setText("18-已获取保存在Uri中的照片，Uri=：" + data.getData());
			cropImageReturnTrue(this, data.getData(), 72 * 4, 72 * 4);
			break;
		case 14://数据通过系统默认的Uri返回
			if (data == null) return;
			tv_info.setText("19-已获取保存在Uri中的照片，Uri=：" + data.getData());
			cropImageReturnFalse(this, data.getData(), 72 * 4, 72 * 4);
			break;
		case REQUEST_CROP_IMG_CODE_TRUE: // 裁剪后的图片通过onActivityResult中的Intent返回
			bitmap = (Bitmap) data.getParcelableExtra("data");
			imageView.setImageBitmap(bitmap);
			tv_info.setText("1、裁剪后的尺寸为：" + bitmap.getWidth() + " * " + bitmap.getHeight());
			break;
		case REQUEST_CROP_IMG_CODE_FALSE: //裁剪后的图片通过指定的Uri返回(这里是保存到SD上)
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(path + "temp.png")));
				imageView.setImageBitmap(bitmap);
				tv_info.setText("2、裁剪后的尺寸为：" + bitmap.getWidth() + " * " + bitmap.getHeight());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	//******************************************************************************************************************************************
	//																												获取图片
	//******************************************************************************************************************************************

	/**
	   * 调出图库APP的裁剪图片功能，将指定Uri中的图片裁剪为指定大小
	   */
	public static void cropImageReturnTrue(Activity context, Uri uri, int desWidth, int desHeight) {
		if (uri == null) return;
		//指定action是使用系统图库裁剪图片
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置在开启的Intent中，显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		//设置裁剪比例及裁剪图片的具体宽高
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", desWidth);
		intent.putExtra("outputY", desHeight);
		//设置是否允许拉伸
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		//设置输出格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		//设置是否需要人脸识别，默认不需要设置
		intent.putExtra("noFaceDetection", true);
		//设置是否返回数据。如果要在给定的uri中返回图片，则必须设置为false；如果设置为true，那么便不会在给定的uri中获取到裁剪的图片
		//这里我们直接将数据返回到了onActivityResult中的intent中了，不需要返回到给定的uri中，所以设为true
		intent.putExtra("return-data", true);
		context.startActivityForResult(intent, REQUEST_CROP_IMG_CODE_TRUE);
	}

	/**
	 * 调出图库APP的裁剪图片功能，将指定Uri中的图片裁剪为指定大小.方式二
	 */
	public static void cropImageReturnFalse(Activity context, Uri uri, int desWidth, int desHeight) {
		if (uri == null) return;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", desWidth);
		intent.putExtra("outputY", desHeight);
		intent.putExtra("scale", true);
		//        intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);//这里是唯一不同的地方
		//将数据返回到指定的uri中。注意：若裁减时打开图片的uri与保存图片的uri相同，会产生冲突，导致裁减完成后图片的大小变成0Byte。
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path + "temp.png")));
		context.startActivityForResult(intent, REQUEST_CROP_IMG_CODE_FALSE);
	}

	//******************************************************************************************************************************************
	//																												AsyncHttpClient
	//******************************************************************************************************************************************
	/**
	 * 采用AsyncHttpClient上传图片，只能是post方式
	 */
	private void asyncUploadFile() {
		if (bitmap == null) {
			Toast.makeText(this, "还没设置图片呢", Toast.LENGTH_SHORT).show();
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩
		RequestParams params = new RequestParams();
		//add方法可以给同一个key值传多个value，它会存在一个list里面；而put方法只能给同一个key传唯一的一个value，如果传多个，后一个会替换掉前一个
		params.put("uid", uid);
		params.put("session_id", session_id);
		params.put("image", new ByteArrayInputStream(baos.toByteArray()), "headImg", "image/*");//核心代码
		//params.put("image", new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)));//具体是以哪种方式发给服务器，需要和服务器商议确定

		ProgressDialogUtil.showProgressDialog(this, "", "提交中...");
		AsyncXiuHttpHelper.post(UrlOfServer.RQ_MSG_EDIT, params, new OnHttpListener<JSONObject>() {
			@Override
			public void onHttpListener(boolean httpSuccessed, JSONObject obj) {
				ProgressDialogUtil.dismissProgressDialog();
				tv_info.setText(JsonFormatTool.formatJson(obj.toString()));
				if (httpSuccessed) {
					Toast.makeText(UploadFileActivity.this, "成功", Toast.LENGTH_SHORT).show();
					reLogin();
				} else Toast.makeText(UploadFileActivity.this, "失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**重新登录*/
	protected void reLogin() {
		Bundle loginBundle = new Bundle();
		loginBundle.putString("account", "103468");
		loginBundle.putString("password", "103468");
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtras(loginBundle);
		startActivity(intent);
		imageView.setImageResource(R.drawable.ic_launcher);
	}

	//******************************************************************************************************************************************
	//																												HttpURLConn
	//******************************************************************************************************************************************
	/**
	 * 采用HttpURLConn上传图片，未成功
	 */
	private void httpURLConUploadFile() {
		final File file = new File(Environment.getExternalStorageDirectory(), "a.png");
		if (!file.exists() || file.length() == 0) {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		//普通参数
		final Map<String, String> strMap = new HashMap<String, String>();
		strMap.put("session_id", session_id);
		strMap.put("uid", uid + "");
		//文件参数
		final Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put("image", file);
		final String url = "http://" + AsyncXiuHttpHelper.SERVER_URL + UrlOfServer.RQ_MSG_EDIT;

		ProgressDialogUtil.showProgressDialog(this, "", "申请中...", false, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpUploadFilesUtils.post(url, strMap, fileMap);
				} catch (Exception e) {
					e.printStackTrace();
				}

				final String str = HttpUploadFilesUtils.post(url, strMap, fileMap);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ProgressDialogUtil.dismissProgressDialog();
						try {
							tv_info.setText(JsonFormatTool.formatJson(new JSONObject(str).toString()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}
}