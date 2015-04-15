package com.huichuang.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class BannerPagerAdapter extends PagerAdapter {

	List<View> mViews;

	public BannerPagerAdapter(List<View> mViews) {
		this.mViews = mViews;
	}

	public void changeData(List<View> mViews){
		this.mViews=mViews;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
	
		return mViews.size();
	}

	@Override
	public int getItemPosition(Object object) {
		
		return POSITION_NONE;
		// return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
	
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		((ViewPager) container).removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		((ViewPager) container).addView(mViews.get(position));
		return mViews.get(position);
	}

}
