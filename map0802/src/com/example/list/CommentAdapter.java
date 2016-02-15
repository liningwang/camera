package com.example.list;

import java.util.List;
import java.util.Map;

import com.example.R;
import com.example.map0802.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CommentAdapter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	public CommentAdapter(Context context,List<Map<String, Object>> data){
		this.context=context;
		this.data=data;
		this.layoutInflater=LayoutInflater.from(context);
	}
	/**
	 * 组件集合，对应list.xml中的控件
	 * @author Administrator
	 */
	public final class Zujian{
		public TextView content;
		public TextView time;
		public TextView username;
		public LinearLayout private_cus;
		public LinearLayout reply;
	}
	@Override
	public int getCount() {
		return data.size();
	}
	/**
	 * 获得某一位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	/**
	 * 获得唯一标识
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Zujian zujian=null;
		if(arg1==null){
			zujian=new Zujian();
			//获得组件，实例化组件
			arg1=layoutInflater.inflate(R.layout.comment, null);
			zujian.content =(TextView)arg1.findViewById(R.id.content);
			zujian.time=(TextView)arg1.findViewById(R.id.time);
			zujian.private_cus=(LinearLayout)arg1.findViewById(R.id.private_cus);
			zujian.username=(TextView)arg1.findViewById(R.id.userName);
			zujian.reply=(LinearLayout)arg1.findViewById(R.id.reply);
			zujian.private_cus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent user = new Intent();
					user.setClass(CommentAdapter.this.context, ProfileActivity.class);
					CommentAdapter.this.context.startActivity(user);
				}
			});
			arg1.setTag(zujian);
		}else{
			zujian=(Zujian)arg1.getTag();
		}
		//绑定数据
		zujian.content.setText((String)data.get(arg0).get("comment"));
		zujian.time.setText((String)data.get(arg0).get("time"));
		zujian.username.setText((String)data.get(arg0).get("user"));
		return arg1;
	}

}
