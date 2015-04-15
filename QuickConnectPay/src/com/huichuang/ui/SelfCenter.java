package com.huichuang.ui;

import java.util.ArrayList;
import java.util.List;

import com.huichuang.adapter.SelfCenterListViewAdapter;
import com.huichuang.base.BaseActivity;
import com.huichuang.entity.EveryFee;
import com.huichuang.quickconnectpay.R;
import com.huichuang.widget.CustomListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SelfCenter extends BaseActivity implements OnItemClickListener{

	private CustomListView self_center_listview;
	private List<EveryFee> self_center_arrays = new ArrayList<EveryFee>();
	private SelfCenterListViewAdapter centerListViewAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_center);
		init();
		initData();
		addListeners();
	}

	private void addListeners() {
		self_center_listview.setOnItemClickListener(this);
		
	}

	private void initData() {
		int[] pic_resources = { R.drawable.extract, R.drawable.transaction_records,
				R.drawable.msg_center,
				R.drawable.my_card_reader, R.drawable.safe_center };
		String[] every_fee_names = { "提款到银行账户", "交易记录", "消息中心", "我的刷卡器", "安全中心" };
		for (int i = 0; i < 5; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(pic_resources[i]);
			fee.setFeeName(every_fee_names[i]);
			self_center_arrays.add(fee);
		}
		 centerListViewAdapter=new SelfCenterListViewAdapter(self_center_arrays, this);
		 self_center_listview.setAdapter(centerListViewAdapter);
		 
	}

	private void init() {
		self_center_listview=(CustomListView)findViewById(R.id.self_center_listview);
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Toast.makeText(this, "item click", Toast.LENGTH_LONG).show();
	}
}
