package com.bqt.http.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bqt.http.R;

/**
 * 一个简单的EditText，可监听内容变化，有清除内容的功能
 * @author 白乾涛
 */
public class MineLoginEditText extends FrameLayout implements TextWatcher {
	private EditText et;
	private ImageView iv_clean;
	private TextView tv_forget;

	public MineLoginEditText(Context context) {
		super(context);
		initView(context);
	}

	public MineLoginEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public MineLoginEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		inflate(context, R.layout.view_login_et, this);
		et = (EditText) findViewById(R.id.et);
		iv_clean = (ImageView) findViewById(R.id.iv_clean);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		et.addTextChangedListener(this);
		iv_clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				et.setText("");
			}
		});
	}

	//监听**************************************************************************************************************************
	private ITextChangeListener mTextChangeListener;

	public interface ITextChangeListener {
		void onTextChange();
	}

	public void setTextChangeListener(ITextChangeListener mTextChangeListener) {
		this.mTextChangeListener = mTextChangeListener;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0) {
			iv_clean.setVisibility(View.VISIBLE);
			iv_clean.setImageResource(R.drawable.mine_delete_selector);
		} else iv_clean.setVisibility(View.GONE);

		if (mTextChangeListener != null) mTextChangeListener.onTextChange();
	}

	//get方法**************************************************************************************************************************

	public TextView getTv_forget() {
		return tv_forget;
	}

	public EditText getEditText() {
		return et;
	}

	public ImageView getIvClean() {
		return iv_clean;
	}
}