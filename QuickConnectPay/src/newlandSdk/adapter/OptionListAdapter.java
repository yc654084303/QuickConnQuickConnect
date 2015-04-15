package newlandSdk.adapter;


import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
/**
 * 二级菜单适配器
 * @author evil
 *
 */
public class OptionListAdapter extends BaseExpandableListAdapter {

	public static final int ItemHeight = 84;// 每项的高度
	public static final int PaddingLeft = 72;// 每项的高度
	private int myPaddingLeft = 0;// 如果是由SuperTreeView调用，则作为子项需要往右移

	static public class TreeNode {
		public Object parent;
		public List<Object> childs = new ArrayList<Object>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public OptionListAdapter(Context view, int myPaddingLeft) {
		parentContext = view;
		this.myPaddingLeft = myPaddingLeft;
	}

	public List<TreeNode> GetTreeNode() {
		return treeNodes;
	}

	public void UpdateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	public void RemoveAll() {
		treeNodes.clear();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return treeNodes.get(groupPosition).childs.get(childPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return treeNodes.get(groupPosition).childs.size();
	}

	static public TextView getTextView(Context context) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ItemHeight);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		textView.setPadding(18, 0, 0, 0);
		textView.setTextSize(18);
		textView.setTextColor(context.getResources().getColor(R.color.black));
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		return textView;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textView = getTextView(this.parentContext);
		textView.setText(getChild(groupPosition, childPosition).toString());
		// textView.setPadding(myPaddingLeft+PaddingLeft-20, 0, 0, 0);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(15);
		if ((groupPosition == 0 && childPosition == 0) | (groupPosition == 0 && childPosition == 3)) {
			textView.setEnabled(false);
			textView.setClickable(false);
			textView.setTextColor(this.parentContext.getResources().getColor(android.R.color.darker_gray));
			textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		}
		return textView;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView textView = getTextView(this.parentContext);
		textView.setText(getGroup(groupPosition).toString());
		textView.setPadding(myPaddingLeft + (PaddingLeft >> 1), 0, 0, 0);
		return textView;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public Object getGroup(int groupPosition) {
		return treeNodes.get(groupPosition).parent;
	}

	public int getGroupCount() {
		return treeNodes.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

}
