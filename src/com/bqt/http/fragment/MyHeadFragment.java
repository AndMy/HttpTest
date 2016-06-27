package com.bqt.http.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bqt.http.utils.JsonFormatTool;

public class MyHeadFragment extends Fragment {
	private Activity mContext;
	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		textView = new TextView(mContext);
		textView.setText("用户首页的Fragment");
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textView.append("\n" + new SimpleDateFormat("yyyy-MM-dd E aHH:mm:ss", Locale.getDefault()).format(new Date()));
			}
		});

		Bundle bundle = getArguments();//从activity传过来的Bundle
		if (bundle != null) {
			boolean isSuccess = bundle.getBoolean("isSuccess");
			String result = bundle.getString("result");
			textView.append("\n是否登录成功" + isSuccess + "：\n" + JsonFormatTool.formatJson(result));
		}
		return textView;
	}
}