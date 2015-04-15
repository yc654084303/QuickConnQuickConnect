package com.huichuang.ui;

import java.util.ArrayList;
import java.util.List;
import com.huichuang.adapter.MoreListViewAdapter;
import com.huichuang.base.BaseActivity;
import com.huichuang.entity.EveryFee;
import com.huichuang.quickconnectpay.R;
import com.huichuang.widget.CustomListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class More extends BaseActivity implements OnItemClickListener {
	private List<EveryFee> more_arrays = new ArrayList<EveryFee>();
	private MoreListViewAdapter moreListViewAdapter;
	private CustomListView more_listview;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		init();
		initData();
		addListeners();
	}

	private void addListeners() {

		more_listview.setOnItemClickListener(this);
	}

	private void initData() {
		int[] pic_resources = { R.drawable.about_me,
				R.drawable.function_details, R.drawable.suggestion,
				R.drawable.card_reader_auto, R.drawable.ic_card_auto,
				R.drawable.update };
		String[] every_fee_names = { "关于我们", "功能介绍", "意见建议", "刷卡器自助检测",
				"ic刷卡器自动配置", "检查版本更新" };
		for (int i = 0; i < pic_resources.length; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(pic_resources[i]);
			fee.setFeeName(every_fee_names[i]);
			more_arrays.add(fee);
		}
		moreListViewAdapter = new MoreListViewAdapter(more_arrays, this);
		more_listview.setAdapter(moreListViewAdapter);

	}

	private void init() {
		more_listview = (CustomListView) findViewById(R.id.more_listview);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(this, "item click", Toast.LENGTH_LONG).show();

	}
}
