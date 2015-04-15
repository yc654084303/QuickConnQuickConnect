package com.huichuang.quickconnectpay;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.huichuang.ui.ApplicationCenter;
import com.huichuang.ui.More;
import com.huichuang.ui.SelfCenter;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements
		OnCheckedChangeListener {

	private TabHost tabHost;
	private RadioGroup main_tabs;
	private RadioButton application_center_radio_btn;
	private RadioButton self_center_radio_btn;
	private RadioButton more_radio_btn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		init();
		addListeners();
	}

	private void addListeners() {
		main_tabs.setOnCheckedChangeListener(this);

	}

	private void init() {
		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup(getLocalActivityManager());
		main_tabs = (RadioGroup) findViewById(R.id.main_tabs);
		tabHost.addTab(tabHost.newTabSpec("application_center")
				.setIndicator("application_center")
				.setContent(new Intent(this, ApplicationCenter.class)));
		tabHost.addTab(tabHost.newTabSpec("self_center")
				.setIndicator("self_center")
				.setContent(new Intent(this, SelfCenter.class)));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("more")
				.setContent(new Intent(this, More.class)));
		application_center_radio_btn = (RadioButton) findViewById(R.id.application_center_radio_btn);
		self_center_radio_btn = (RadioButton) findViewById(R.id.self_center_radio_btn);
		more_radio_btn = (RadioButton) findViewById(R.id.more_radio_btn);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.application_center_radio_btn:
			tabHost.setCurrentTabByTag("application_center");
			changeBc1(application_center_radio_btn, self_center_radio_btn, more_radio_btn);
			break;

		case R.id.self_center_radio_btn:
			tabHost.setCurrentTabByTag("self_center");
			changeBc2(self_center_radio_btn, application_center_radio_btn, more_radio_btn);
			break;

		case R.id.more_radio_btn:
			tabHost.setCurrentTabByTag("more");
			changeBc3(more_radio_btn, application_center_radio_btn, self_center_radio_btn);
			break;
		}

	}

	private void changeBc1(RadioButton changeRb, RadioButton noChangeButton1,
			RadioButton noChangeButton2) {

		changeRb.setTextColor(Color.parseColor("#ff7f2e"));
		changeRb.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
				.getDrawable(R.drawable.application_center_yes), null, null);
		noChangeButton1.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton1.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(R.drawable.self_center_no), null,
				null);
		noChangeButton2.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton2.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(R.drawable.more_no), null, null);
	}
	private void changeBc2(RadioButton changeRb, RadioButton noChangeButton1,
			RadioButton noChangeButton2) {
		
		changeRb.setTextColor(Color.parseColor("#ff7f2e"));
		changeRb.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
				.getDrawable(R.drawable.self_center_yes), null, null);
		noChangeButton1.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton1.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(R.drawable.application_center_no), null,
				null);
		noChangeButton2.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton2.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(R.drawable.more_no), null, null);
	}
	private void changeBc3(RadioButton changeRb, RadioButton noChangeButton1,
			RadioButton noChangeButton2) {
		
		changeRb.setTextColor(Color.parseColor("#ff7f2e"));
		changeRb.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
				.getDrawable(R.drawable.more_yes), null, null);
		noChangeButton1.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton1.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(R.drawable.application_center_no), null,
				null);
		noChangeButton2.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton2.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(R.drawable.self_center_no), null, null);
	}

}
