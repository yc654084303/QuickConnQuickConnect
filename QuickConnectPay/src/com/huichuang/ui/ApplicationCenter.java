package com.huichuang.ui;

import java.util.ArrayList;
import java.util.List;

import com.huichuang.adapter.BannerPagerAdapter;
import com.huichuang.adapter.EveryFeesAdapter;
import com.huichuang.base.BaseActivity;
import com.huichuang.entity.EveryFee;
import com.huichuang.quickconnectpay.R;
import com.huichuang.widget.CustomGirdView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ApplicationCenter extends BaseActivity implements
		OnPageChangeListener, OnCheckedChangeListener, OnItemClickListener {

	private ViewPager banner_pager;
	private LinearLayout gallery_point_linear;
	private View banner_pager_item_layout;
	private ArrayList<View> banner_pager_views;
	private BannerPagerAdapter bannerPagerAdapter;
	private int currentItem;// 当前显示的广告位下标索引
	private int giveBannerPosition = 0;
	public static final int CHANGE_VIEW_PAGER_ITEM = 1;// handler的what数值
	private List<EveryFee> PersonalFinance_Arrays = new ArrayList<EveryFee>();// 个人金融数据集合
	private List<EveryFee> life_service_Arrays = new ArrayList<EveryFee>();// 生活服务数据集合
	private List<EveryFee> Business_travel_service_Arrays = new ArrayList<EveryFee>();// 商旅服务数据集合
	private List<EveryFee> Preferential_mall_Arrays = new ArrayList<EveryFee>();// 特惠商城数据集合
	private RadioButton PersonalFinance;
	private RadioButton life_service;
	private RadioButton Business_travel_service;
	private RadioButton Preferential_mall;
	private CustomGirdView everyFeesGv;
	private int whichItem = 0;
	private Handler bannerHanlder = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case CHANGE_VIEW_PAGER_ITEM:
				if (currentItem <= banner_pager_views.size() - 1) {
					banner_pager.setCurrentItem(currentItem);
					changePointView(currentItem);
					currentItem++;
				} else {
					currentItem = 0;
					banner_pager.setCurrentItem(currentItem);
					changePointView(currentItem);
				}
				break;

			default:
				break;
			}
		};
	};
	private EveryFeesAdapter everyFeesAdapter;
	private RadioGroup main_application_tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_center);
		initData();
		init();
		initBannerPager();
		addListeners();
		startBanners();
	}

	private void initData() {
		/* 个人金融数据 */
		int[] pic_resources = { R.drawable.to_pay, R.drawable.phone_no_pay,
				R.drawable.credit_card_assistant,
				R.drawable.credit_card_repayment, R.drawable.the_rapid_transfer };
		// String[] every_fee_names = { "当面付", "手机号付款", "信用卡助手", "信用卡还款", "急速还款"
		// };
		String[] every_fee_names = { "刷卡付", "无卡付" };
		for (int i = 0; i < every_fee_names.length; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(pic_resources[i]);
			fee.setFeeName(every_fee_names[i]);
			PersonalFinance_Arrays.add(fee);
		}
		/* 生活服务数据 */
		int[] life_service_resources = { R.drawable.phone_recharge,
				R.drawable.gas_fee, R.drawable.illegal_agent,
				R.drawable.power_rate, R.drawable.credit_card_repayment,
				R.drawable.water_rate };
		String[] life_service_names = { "手机充值", "燃气费", "违章代办", "电费", "信用卡还款",
				"水费" };
		for (int i = 0; i < life_service_resources.length; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(life_service_resources[i]);
			fee.setFeeName(life_service_names[i]);
			life_service_Arrays.add(fee);
		}
		/* 商旅服务数据 */
		int[] business_recources = { R.drawable.train_tickets,
				R.drawable.air_ticket, R.drawable.hotel_book };
		String[] business_names = { "火车票", "飞机票", "酒店预订" };
		for (int i = 0; i < 3; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(business_recources[i]);
			fee.setFeeName(business_names[i]);
			Business_travel_service_Arrays.add(fee);
		}
		/* 特惠商城数据 */
		int[] preferential_mall_resources = { R.drawable.older_pay };
		String[] preferential_mall_name = { "订单支付" };
		for (int i = 0; i < preferential_mall_resources.length; i++) {
			EveryFee fee = new EveryFee();
			fee.setFeeReId(preferential_mall_resources[i]);
			fee.setFeeName(preferential_mall_name[i]);
			Preferential_mall_Arrays.add(fee);
		}

		everyFeesAdapter = new EveryFeesAdapter(PersonalFinance_Arrays, this);
	}

	private void startBanners() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message message = new Message();
					message.what = CHANGE_VIEW_PAGER_ITEM;
					bannerHanlder.sendMessage(message);
				}
			}
		}).start();

	}

	private void addListeners() {
		banner_pager.setOnPageChangeListener(this);
		main_application_tabs.setOnCheckedChangeListener(this);
		everyFeesGv.setOnItemClickListener(this);
	}

	/**
	 * 填充viewpager现在是死数据到时候对接口的时候需要查找出里面的ImageView控件,现在写死里面只有2个控件
	 */
	private void initBannerPager() {
		for (int i = 0; i < 2; i++) {
			banner_pager_item_layout = getLayoutInflater().inflate(
					R.layout.banner_pager_item_layout, null);
			banner_pager_views.add(banner_pager_item_layout);
		}
		bannerPagerAdapter = new BannerPagerAdapter(banner_pager_views);
		for (int i = 0; i < banner_pager_views.size(); i++) {
			ImageView pointView = new ImageView(this);
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else {
				pointView.setBackgroundResource(R.drawable.feature_point);
			}
			gallery_point_linear.addView(pointView);
		}
		banner_pager.setAdapter(bannerPagerAdapter);
		bannerPagerAdapter.changeData(banner_pager_views);
	}

	private void init() {
		banner_pager_views = new ArrayList<View>();
		banner_pager = (ViewPager) findViewById(R.id.banner_pager);
		gallery_point_linear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		gallery_point_linear.setBackgroundColor(Color.argb(200, 135, 135, 152));
		main_application_tabs = (RadioGroup) findViewById(R.id.main_application_tabs);
		PersonalFinance = (RadioButton) findViewById(R.id.PersonalFinance);
		life_service = (RadioButton) findViewById(R.id.life_service);
		Business_travel_service = (RadioButton) findViewById(R.id.Business_travel_service);
		Preferential_mall = (RadioButton) findViewById(R.id.Preferential_mall);
		everyFeesGv = (CustomGirdView) findViewById(R.id.everyFeesGv);
		everyFeesGv.setAdapter(everyFeesAdapter);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		changePointView(position);
	}

	private void changePointView(int cur) {
		LinearLayout gallery_point_linear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View giveView = gallery_point_linear.getChildAt(giveBannerPosition);
		View curView = gallery_point_linear.getChildAt(cur);
		if (giveView != null && curView != null) {
			ImageView giveView_ = (ImageView) giveView;
			ImageView curView_ = (ImageView) curView;
			giveView_.setBackgroundResource(R.drawable.feature_point);
			curView_.setBackgroundResource(R.drawable.feature_point_cur);
			giveBannerPosition = cur;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.PersonalFinance:
			everyFeesAdapter.changeData(PersonalFinance_Arrays);
			changeBc(PersonalFinance, Business_travel_service,
					Preferential_mall, life_service);
			whichItem = 0;
			break;
		case R.id.life_service:
			everyFeesAdapter.changeData(life_service_Arrays);
			changeBc(life_service, PersonalFinance, Preferential_mall,
					Business_travel_service);
			whichItem = 1;
			break;
		case R.id.Business_travel_service:
			everyFeesAdapter.changeData(Business_travel_service_Arrays);
			changeBc(Business_travel_service, PersonalFinance,
					Preferential_mall, life_service);
			whichItem = 2;
			break;
		case R.id.Preferential_mall:
			everyFeesAdapter.changeData(Preferential_mall_Arrays);
			changeBc(Preferential_mall, PersonalFinance,
					Business_travel_service, life_service);
			whichItem = 3;
			break;

		}

	}

	private void changeBc(RadioButton changeRb, RadioButton noChangeButton1,
			RadioButton noChangeButton2, RadioButton noChangeButton3) {

		changeRb.setTextColor(Color.parseColor("#f3c0a6"));
		noChangeButton1.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton2.setTextColor(Color.parseColor("#b4b5b6"));
		noChangeButton3.setTextColor(Color.parseColor("#b4b5b6"));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		EveryFee fee = null;
		switch (whichItem) {
		case 0:
			fee = PersonalFinance_Arrays.get(position);
			toActivity(CardPay.class);
			break;

		case 1:
			fee = life_service_Arrays.get(position);
			break;

		case 2:
			fee = Business_travel_service_Arrays.get(position);
			break;

		case 3:
			fee = Preferential_mall_Arrays.get(position);
			break;
		}
		Toast.makeText(this, "item click", Toast.LENGTH_LONG).show();

	}

	public void toActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}
}
