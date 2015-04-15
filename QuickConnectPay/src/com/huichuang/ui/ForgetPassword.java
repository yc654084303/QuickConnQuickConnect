package com.huichuang.ui;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ForgetPassword extends BaseActivity implements OnClickListener{

	private TextView next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_paddword_layout);
		init();
		addListeners();
	}

	private void addListeners() {
		next.setOnClickListener(this);
		
	}

	private void init() {
		next=(TextView)findViewById(R.id.next);
		
	}

	public void toActivity(Class<?> cls, Bundle bun) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.next:
		toActivity(ConfirmNewPassWord.class, null);
		break;

	}
		
	}
}
