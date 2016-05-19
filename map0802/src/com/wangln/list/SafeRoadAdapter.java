package com.wangln.list;

import java.util.List;
import java.util.Map;

import com.wangln.R;
import com.wangln.map0802.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SafeRoadAdapter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	public SafeRoadAdapter(Context context,List<Map<String, Object>> data){
		this.context=context;
		this.data=data;
		this.layoutInflater=LayoutInflater.from(context);
	}
	/**
	 * 组件集合，对应list.xml中的控件
	 * @author Administrator
	 */
	public final class Zujian{
		public ImageView image;
		public CheckBox select;
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
			arg1=layoutInflater.inflate(R.layout.image_list, null);
			zujian.image =(ImageView)arg1.findViewById(R.id.image);
		//	zujian.select=(CheckBox)arg1.findViewById(R.id.select);
			arg1.setTag(zujian);
		}else{
			zujian=(Zujian)arg1.getTag();
		}
		//绑定数据
		Log.d("wang","adapter show image");
		zujian.image.setImageBitmap((Bitmap) (data.get(arg0).get("image")));
		return arg1;
	}

}
