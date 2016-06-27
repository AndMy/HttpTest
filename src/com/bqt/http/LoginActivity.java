package com.bqt.http;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;

import com.bqt.http.fragment.LoginFragment;
import com.bqt.http.fragment.MyHeadFragment;

/**登录界面及登陆后用户首页界面，使用两个Fragment实现*/
public class LoginActivity extends Activity implements LoginInterface {
	/** 存放Fragment的容器 */
	private View fragmentContainer;
	/** 在使用replace替换Fragment是需要一个id */
	public static final int ROOT_ID = 0x0011223344;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		fragmentContainer = new ScrollView(this);//可以滚动
		fragmentContainer.setBackgroundColor(0xfffbf7ed);
		fragmentContainer.setId(ROOT_ID);
		setContentView(fragmentContainer);

		LoginFragment loginFragment = new LoginFragment();
		getFragmentManager().beginTransaction().replace(ROOT_ID, loginFragment).commit();
		loginFragment.setLoginInterface(this);//当登录后采用回调方式重新加载用户信息

		//如果进入此Activity时有出入账户密码，则直接登录
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String account = bundle.getString("account");
			String password = bundle.getString("password");
			loginFragment.login(this, account, password);
		}
	}

	@Override
	/**第三步，让【被调用者】实现此接口，并在实现的方法中自定义自己的回调逻辑*/
	public void loginComplete(boolean isSuccess, String result) {
		MyHeadFragment headFragment = new MyHeadFragment();
		Bundle bundle = new Bundle();
		bundle.putString("result", result);
		bundle.putBoolean("isSuccess", isSuccess);
		headFragment.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(ROOT_ID, headFragment).commitAllowingStateLoss();
		//当使用commit方法时，系统将进行状态判断，如果状态（mStateSaved）已经保存，将发生"Can not perform this action after onSaveInstanceState"错误。
	}
}