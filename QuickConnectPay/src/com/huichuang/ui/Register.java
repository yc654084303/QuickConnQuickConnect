package com.huichuang.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.MainActivity;
import com.huichuang.quickconnectpay.R;

public class Register extends BaseActivity implements OnClickListener {

	private TextView now_register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		init();
		addListeners();
	}

	private void addListeners() {
		now_register.setOnClickListener(this);

	}

	private void init() {
		now_register = (TextView) findViewById(R.id.now_register);

	}

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.now_register:
		toActivity(MainActivity.class, null);
		break;
	}

	}

	public void toActivity(Class<?> cls, Bundle bun) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}
}
