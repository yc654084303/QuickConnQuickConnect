package newlandSdk.adapter;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;

public class FileAdapter extends BaseAdapter {
	List<File> allFiles = new ArrayList<File>();
	BaseActivity mainActivity;

	public FileAdapter(List<File> allFiles, Activity mainActivity) {
		super();
		this.allFiles = allFiles;
		this.mainActivity = (BaseActivity)mainActivity;
	}

	@Override
	public int getCount() {
		return allFiles.size();
	}

	@Override
	public Object getItem(int arg0) {
		return allFiles.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mainActivity);
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.items_layout, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.text1);
		textView.setTextSize(20);
		textView.setText(allFiles.get(position).getName());
		return convertView;
	}

}
