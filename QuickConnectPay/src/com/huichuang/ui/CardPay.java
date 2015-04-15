package com.huichuang.ui;

import java.util.ArrayList;
import java.util.List;

import newlandSdk.mainapp.NewlandPOSMainActivity;

import com.huichuang.adapter.CardPayListViewAdapter;
import com.huichuang.base.BaseActivity;
import com.huichuang.entity.EveryFee;
import com.huichuang.quickconnectpay.R;
import com.huichuang.widget.CustomListView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CardPay extends BaseActivity implements OnItemClickListener{

	private CustomListView card_pay_listview;
	private List<EveryFee> card_pay_arrays = new ArrayList<EveryFee>();
	private CardPayListViewAdapter CardPayListViewAdapter;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_pay);
		getActionBar().hide();
		init();
		initData();
		addListeners();
	}
	
	
	private void addListeners() {

		card_pay_listview.setOnItemClickListener(this);
	}

	private void initData() {
		int[] pic_resources = { R.drawable.about_me,
				R.drawable.lakala, R.drawable.newland,
				R.drawable.happy_pay, R.drawable.hezi_pay};
		String[] every_fee_names = { "快连", "拉卡拉", "新大陆", "快乐付",
		"盒子支付"};
		for (int i = 0; i < pic_resources.length; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(pic_resources[i]);
			fee.setFeeName(every_fee_names[i]);
			card_pay_arrays.add(fee);
		}
		CardPayListViewAdapter = new CardPayListViewAdapter(card_pay_arrays, this);
		card_pay_listview.setAdapter(CardPayListViewAdapter);

	}

	private void init() {
		card_pay_listview = (CustomListView) findViewById(R.id.card_pay_listview);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(this, "item click", Toast.LENGTH_LONG).show();
		switch (arg2) {
		case 2:
			Intent intent1=new Intent();
			intent1.setClass(this, NewlandActivity_.class);
			startActivity(intent1);
			
			break;
		case 3:
			Intent intent=new Intent();
			intent.setClass(this, NewlandPOSMainActivity.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}

	}
}
