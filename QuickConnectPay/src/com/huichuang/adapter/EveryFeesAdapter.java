package com.huichuang.adapter;

import java.util.List;

import com.huichuang.entity.EveryFee;
import com.huichuang.quickconnectpay.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EveryFeesAdapter extends BaseAdapter {

	private List<EveryFee> everyFees;
	private Context context;
	private LayoutInflater inflater;

	public EveryFeesAdapter(List<EveryFee> everyFees, Context context) {
		this.everyFees = everyFees;
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
	}

	public void changeData(List<EveryFee> everyFees) {
		this.everyFees = everyFees;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return this.everyFees != null ? this.everyFees.size() : 0;
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {

		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = this.inflater.inflate(
					R.layout.every_fee_gridview_item_layout, null);
			holder.pic_fee = (ImageView) contentView.findViewById(R.id.pic_fee);
			holder.text_pic_name = (TextView) contentView
					.findViewById(R.id.txt_fee_name);
			contentView.setTag(holder);
		}
		EveryFee fee = this.everyFees.get(position);
		holder = (ViewHolder) contentView.getTag();
		holder.pic_fee.setBackgroundResource(fee.getFeeReId());
		holder.text_pic_name.setText(fee.getFeeName());

		return contentView;
	}

	class ViewHolder {
		ImageView pic_fee;
		TextView text_pic_name;
	}
}
