package com.huichuang.ui;

import java.util.HashMap;
import java.util.Map;

import com.huichuang.base.BaseActivity;
import com.huichuang.http.Callback;
import com.huichuang.http.NetManager;
import com.huichuang.quickconnectpay.MainActivity;
import com.huichuang.quickconnectpay.R;
import com.huichuang.test.bean.RequestResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Login extends BaseActivity implements OnClickListener {

	private TextView forget_password;
	private TextView login;
	private TextView register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		getActionBar().hide();
		init();
		addListeners();
	}

	private void addListeners() {
		forget_password.setOnClickListener(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);

	}

	private void init() {
		forget_password = (TextView) findViewById(R.id.forget_password);
		login = (TextView) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_password:
			toActivity(ForgetPassword.class, null);
			break;
		case R.id.login:
		toActivity(MainActivity.class, null);
			finish();
			break;
		case R.id.register:
			toActivity(Register.class, null);
			finish();
			break;
		}

	}
	
}
