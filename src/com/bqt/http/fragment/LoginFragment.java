package com.bqt.http.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bqt.http.LoginInterface;
import com.bqt.http.MyApplication;
import com.bqt.http.R;
import com.bqt.http.bean.UrlOfServer;
import com.bqt.http.bean.UserBean;
import com.bqt.http.utils.AsyncHttpHelper.OnHttpListener;
import com.bqt.http.utils.AsyncXiuHttpHelper;
import com.bqt.http.utils.ProgressDialogUtil;
import com.bqt.http.view.MineLoginEditText;
import com.bqt.http.view.MineLoginEditText.ITextChangeListener;
import com.loopj.android.http.RequestParams;

public class LoginFragment extends Fragment implements OnClickListener, ITextChangeListener, OnLayoutChangeListener {
	private Activity mContext;
	private View root;
	private ImageView iv_close;
	private LinearLayout ll_third_login;
	private RelativeLayout rl_root;
	private RelativeLayout rl_login;
	private TextView tv_qq_login;
	private TextView tv_wechat_login;
	private TextView tv_forget;
	private TextView tv_login;
	private TextView tv_signup;
	private TextView tv_tips;
	private MineLoginEditText et_account;
	private MineLoginEditText et_pass;
	private Rect firstRect; //用于储存开始的可见区域

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		root = inflater.inflate(R.layout.fragment_login, container, false);
		findViews();
		initViews();
		setOnClickListeners();
		return root;
	}

	//***********************************************************************************************************************************
	//																										初始化	
	//***********************************************************************************************************************************
	private void findViews() {
		rl_root = (RelativeLayout) root.findViewById(R.id.rl_root);
		iv_close = (ImageView) root.findViewById(R.id.iv_close);

		ll_third_login = (LinearLayout) root.findViewById(R.id.ll_third_login);
		tv_qq_login = (TextView) root.findViewById(R.id.tv_qq_login);
		tv_wechat_login = (TextView) root.findViewById(R.id.tv_wechat_login);

		rl_login = (RelativeLayout) root.findViewById(R.id.rl_login);
		tv_tips = (TextView) root.findViewById(R.id.tv_tips);
		et_account = (MineLoginEditText) root.findViewById(R.id.et_account);
		et_pass = (MineLoginEditText) root.findViewById(R.id.et_pass);
		tv_forget = et_pass.getTv_forget();
		tv_signup = (TextView) root.findViewById(R.id.tv_signup);
		tv_login = (TextView) root.findViewById(R.id.tv_login);
	}

	private void initViews() {
		et_account.setTextChangeListener(this);
		et_account.getEditText().setHint("请输入账号");
		et_account.getTv_forget().setWidth(0);//若设置setVisibility则后面有可能显示出来了

		et_pass.setTextChangeListener(this);
		et_pass.getEditText().setHint("请输入密码");
		et_pass.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		rl_root.addOnLayoutChangeListener(this);
		rl_root.post(new Runnable() {
			@Override
			public void run() {
				firstRect = new Rect();
				rl_root.getWindowVisibleDisplayFrame(firstRect);
			}
		});
	}

	private void setOnClickListeners() {
		iv_close.setOnClickListener(this);
		tv_qq_login.setOnClickListener(this);
		tv_wechat_login.setOnClickListener(this);
		tv_signup.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		tv_forget.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_close:
			if (loginInterface != null) loginInterface.loginComplete(false, "这哥们按了关闭按钮，并没有登录");
			break;
		case R.id.tv_login:
			login(mContext, et_account.getEditText().getText().toString().trim(), et_pass.getEditText().getText().toString().trim());
			break;
		case R.id.tv_signup://直接登录
			login(mContext, "103468", "103468");
			break;
		case R.id.tv_qq_login://qq登录
			break;
		case R.id.tv_wechat_login://微信登录
			break;
		case R.id.tv_forget://跳到找回密码界面
			break;
		default:
			break;
		}
	}

	//***********************************************************************************************************************************
	//																										登录		
	//***********************************************************************************************************************************
	/**账号密码登录*/
	public void login(final Context mContext, String userAccount, String pass) {

		if (TextUtils.isEmpty(userAccount)) {
			Toast.makeText(mContext, "请输入账号或手机号", Toast.LENGTH_SHORT).show();
			return;
		}

		if (userAccount.length() < 6 || userAccount.length() > 12 || !checkUserName(userAccount)) {
			et_account.getIvClean().setVisibility(View.VISIBLE);
			Toast.makeText(mContext, "账号格式错误，账号为6–12位数字、字母或@的组合", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(pass)) {
			Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		}

		if (pass.length() < 6 || pass.length() > 20 || !checkPassword(pass)) {
			et_pass.getIvClean().setVisibility(View.VISIBLE);
			Toast.makeText(mContext, "密码格式错误，请输入6–20位字符（字母，数字或下划线组成）", Toast.LENGTH_SHORT).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("user", userAccount);
		params.put("pass", pass);
		params.put("imsi", getImsi(mContext));
		ProgressDialogUtil.showProgressDialog(mContext, "", "登录中...");
		AsyncXiuHttpHelper.post(UrlOfServer.RQ_LOGIN, params, new OnHttpListener<JSONObject>() {
			@Override
			public void onHttpListener(boolean httpSuccessed, JSONObject json) {
				ProgressDialogUtil.dismissProgressDialog();
				if (httpSuccessed) {
					try {
						int result = json.getInt("result");
						if (result == 1) {//请求成功
							//设置用户信息
							MyApplication.getApplication().setUser(new UserBean(json));
							if (loginInterface != null) loginInterface.loginComplete(true, json.toString());//回调
							Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
						} else if (result == -1) {
							tv_tips.setVisibility(View.VISIBLE);
							if (et_account.getEditText().getText().toString().length() > 0) et_account.getIvClean().setVisibility(View.VISIBLE);
							if (et_pass.getEditText().getText().toString().length() > 0) et_pass.getIvClean().setVisibility(View.VISIBLE);
							Toast.makeText(mContext, "账号或密码错误", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(mContext, "通信失败，请稍候重试", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onTextChange() {
		final String userAccount = et_account.getEditText().getText().toString().trim();
		final String pass = et_pass.getEditText().getText().toString().trim();
		boolean isAccountOk = false;
		boolean isPassOk = false;
		if (userAccount != null && userAccount.length() >= 6 && userAccount.length() <= 12) isAccountOk = true;
		if (pass != null && pass.length() >= 6 && pass.length() <= 20) isPassOk = true;
		if (isAccountOk && isPassOk) tv_login.setTextColor(0xFFFFFFFF);
		else tv_login.setTextColor(0x7FFFFFFF);
	}

	//***********************************************************************************************************************************
	//																										回调		
	//***********************************************************************************************************************************

	/**第二步，在【调用者】中定义一个实例，并暴露一个对此实例进行set的方法*/
	private LoginInterface loginInterface;

	public void setLoginInterface(LoginInterface loginInterface) {
		this.loginInterface = loginInterface;
	}

	//***********************************************************************************************************************************
	//																										键盘显示与隐藏		
	//***********************************************************************************************************************************

	@Override
	/**Fragment不可见时隐藏软键盘*/
	public void onStop() {
		super.onStop();
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_account.getWindowToken(), 0);
	}

	@Override
	/**Fragment从Activity中移除时取消监听*/
	public void onDetach() {
		super.onDetach();
		rl_root.removeOnLayoutChangeListener(this);
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		Rect rect = new Rect();
		rl_root.getWindowVisibleDisplayFrame(rect);
		if (firstRect != null) {
			if (firstRect.height() - rect.height() > 50) softInputShowAnimate();
			else softInputHideAnimate();
		}
	}

	/**键盘弹出时的动画*/
	private void softInputShowAnimate() {
		//既然都隐藏了，就不让点击了
		tv_qq_login.setClickable(false);
		tv_wechat_login.setClickable(false);

		//如果偏移过就不偏移了
		if (Math.abs(rl_login.getTranslationY()) > 0) return;
		int translation_y = mContext.getResources().getInteger(R.integer.anim_translation_y);//也可直接通过dimens文件适配，这样就不用在代码中转换单位了
		float translationY = translation_y * mContext.getResources().getDisplayMetrics().density + 0.5f;//将dp转为px
		ObjectAnimator loginAnimator = ObjectAnimator.ofFloat(rl_login, "translationY", 0, -translationY);
		loginAnimator.setInterpolator(new AccelerateInterpolator());
		ObjectAnimator thirdLoginAnimator = ObjectAnimator.ofFloat(ll_third_login, "alpha", 1, 0);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(loginAnimator, thirdLoginAnimator);
		animatorSet.setDuration(400);
		animatorSet.start();
	}

	/**键盘隐藏时的动画*/
	private void softInputHideAnimate() {
		//显示后可以点击
		tv_qq_login.setClickable(true);
		tv_wechat_login.setClickable(true);

		ObjectAnimator loginAnimator = ObjectAnimator.ofFloat(rl_login, "translationY", rl_login.getTranslationY(), 0);
		loginAnimator.setInterpolator(new DecelerateInterpolator());
		ObjectAnimator thirdLoginAnimator = ObjectAnimator.ofFloat(ll_third_login, "alpha", 0, 1);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(loginAnimator, thirdLoginAnimator);
		animatorSet.setDuration(400);
		animatorSet.start();
	}

	//***********************************************************************************************************************************
	//																											工具方法	
	//***********************************************************************************************************************************

	/**
	 * 检查账号，账号为6–12位数字、字母或@的组合      [0-9a-zA-Z@]*
	 */
	public static boolean checkUserName(String password) {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z@]+$"); // [0-9a-zA-Z@]*
		Matcher m = pattern.matcher(password);
		return m.matches();
	}

	/**
	 * 检查密码，6–20位字符（字母，数字或下划线组成）     [0-9a-zA-Z_]*
	 */
	public static boolean checkPassword(String password) {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]+$"); // [0-9a-zA-Z_]*
		Matcher m = pattern.matcher(password);
		return m.matches();
	}

	/**
	 * 获取IMSI。Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Requires Permission: READ_PHONE_STATE
	 */
	public static String getImsi(Context context) {
		TelephonyManager iPhoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return iPhoneManager.getSubscriberId();
	}
}