/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：MyAdapter   
 * 类描述：主ListView的适配器，继承于可滑动删除的ListView  
 * 创建人：Xu/Group Friend   
 * 创建时间：2015-12-7 15:33:03      
 *    
 */
package com.friend.memorandumdemo.myui;

import java.util.ArrayList;
import java.util.Map;

import com.example.memorandumdemo.R;
import com.friend.memorandumdemo.model.UserData;
import com.friend.memorandumdemo.utils.Utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private ArrayList<UserData> menuList = Utils.getList(); // 主ListView
	private Context mContext; // 上下文对象
	private int size; // listview每一项的尺寸

	public MyAdapter(Context context, int size) {
		mContext = context;
		this.size = size;
	}

	@Override
	public int getCount() {
		return menuList.size();
	}

	@Override
	public Object getItem(int position) {
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_item, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		UserData item = menuList.get(position);
		if (item.getAlertTime().equals("")) {
			viewHolder.alerttime.setText("未设置提醒！");
			viewHolder.image.setBackgroundResource(R.drawable.ic_listitem);
		} else {
			viewHolder.alerttime.setText("已设置提醒！ " + "时间："
					+ Utils.timeTransfer((String) item.getAlertTime()));
			viewHolder.image
					.setBackgroundResource(R.drawable.ic_listitem_alert);
		}
		viewHolder.title.setText(item.getTitle());
		return convertView;
	}

	class ViewHolder {
		ImageView image;
		TextView alerttime;
		TextView title;

		public ViewHolder(View view) {
			image = (ImageView) view.findViewById(R.id.image);
			alerttime = (TextView) view.findViewById(R.id.time);
			title = (TextView) view.findViewById(R.id.title);

			alerttime.setTextSize(size - 6);
			title.setTextSize(size);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					Utils.dp2px(size * 2, mContext), Utils.dp2px(size * 2,
							mContext));
			image.setLayoutParams(layoutParams);
			view.setTag(this);
		}
	}

}
