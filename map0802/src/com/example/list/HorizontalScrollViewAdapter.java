package com.example.list;

import java.util.List;

import com.example.R;
import com.example.util.SDUtil;
import com.example.util.SDUtil.ViewSize;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class HorizontalScrollViewAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> mDatas;
	private int width;
	private int height;
	private CheckBox check;

	public HorizontalScrollViewAdapter(Context context, List<String> mDatas)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	public int getCount()
	{
		return mDatas.size();
	}

	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			ViewSize op = new ViewSize();
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.image_list, parent, false);
			viewHolder.mImg = (ImageView)convertView.findViewById(R.id.image);
			viewHolder.mCheck = (CheckBox) convertView.findViewById(R.id.select);

			convertView.setTag(viewHolder);
			//SDUtil.measureMyView(convertView,op);
			width = op.width;
			height = op.height;
	
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}	
		//Log.d("wang","horizontal adapter getview");
		//viewHolder.mImg.setImageBitmap(SDUtil.getImageforSize(mDatas.get(position),width,height));

		return convertView;
	}
	public void setView(View v,Bitmap bmap){
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		//ViewGroup.LayoutParams param = v.getLayoutParams();
		//param.width = LayoutParams.WRAP_CONTENT;
		//v.setLayoutParams(param);
		if(bmap == null) {
			viewHolder.mImg.setImageResource(R.drawable.iconfont_geren);
			//viewHolder.mImg.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
		} else {
			viewHolder.mImg.setImageBitmap(bmap);
		}
	}
	public class ViewHolder
	{
		ImageView mImg;
		CheckBox mCheck;
	}

}
