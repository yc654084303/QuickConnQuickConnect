package com.huichuang.adapter;

import java.util.List;

import com.huichuang.adapter.EveryFeesAdapter.ViewHolder;
import com.huichuang.entity.EveryFee;
import com.huichuang.quickconnectpay.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreListViewAdapter extends BaseAdapter {
	private List<EveryFee> everyFees;
	private Context context;
	private LayoutInflater inflater;

	public MoreListViewAdapter(List<EveryFee> everyFees, Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = this.inflater.inflate(
					R.layout.more_listview_item_layout, null);
			holder.pic_fee = (ImageView) convertView.findViewById(R.id.pic_self_center_item);
			holder.text_pic_name = (TextView) convertView
					.findViewById(R.id.txt_self_center_item);
			convertView.setTag(holder);
		}
		EveryFee fee = this.everyFees.get(position);
		holder = (ViewHolder) convertView.getTag();
		holder.pic_fee.setBackgroundResource(fee.getFeeReId());
		holder.text_pic_name.setText(fee.getFeeName());

		return convertView;
	}

	class ViewHolder {
		ImageView pic_fee;
		TextView text_pic_name;
	}
}
