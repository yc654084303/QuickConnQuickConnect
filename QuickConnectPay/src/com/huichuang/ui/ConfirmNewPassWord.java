package com.huichuang.ui;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.MainActivity;
import com.huichuang.quickconnectpay.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ConfirmNewPassWord extends BaseActivity implements OnClickListener{

	private TextView commit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_new_password_password_layout);
		init();
		addListeners();
	}

	private void addListeners() {
		commit.setOnClickListener(this);
		
	}

	private void init() {
		commit=(TextView)findViewById(R.id.commit);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			toActivity(MainActivity.class, null);
			break;
		}
		
	}
	public void toActivity(Class<?> cls, Bundle bun) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}
}
