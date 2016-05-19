package com.wangln.list;

import java.util.List;
import java.util.Map;

import com.wangln.R;
import com.wangln.map0802.ProfileActivity;
import com.jauker.widget.BadgeView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MyRoadAdapter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	private boolean flag;
	public MyRoadAdapter(Context context,List<Map<String, Object>> data){
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
		public TextView reply;
		public BadgeView news;
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
	public void setFlag(boolean flag) {
		this.flag = flag;
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
		Zujian zujian=null; if(arg1==null){
			zujian=new Zujian();
			//获得组件，实例化组件
			arg1=layoutInflater.inflate(R.layout.my_road, null);
			zujian.content =(TextView)arg1.findViewById(R.id.content);
			zujian.time=(TextView)arg1.findViewById(R.id.time);
			zujian.private_cus=(LinearLayout)arg1.findViewById(R.id.private_cus);
			zujian.username=(TextView)arg1.findViewById(R.id.userName);
			zujian.news=(BadgeView)arg1.findViewById(R.id.news);
			zujian.reply=(TextView)arg1.findViewById(R.id.reply);
			arg1.setTag(zujian);
		}else{
			zujian=(Zujian)arg1.getTag();
		}
		//绑定数据
		zujian.content.setText((String)data.get(arg0).get("comment"));
		zujian.time.setText((String)data.get(arg0).get("time"));
		zujian.username.setText((String)data.get(arg0).get("user"));
		zujian.news.setText((String) data.get(arg0).get("itemcount"));
		String itemcount = (String) data.get(arg0).get("itemcount");
		if((itemcount == null) || (itemcount.equals("0"))) {
			zujian.news.setVisibility(View.GONE);
		}
		zujian.reply.setText((String) data.get(arg0).get("replycount"));
		zujian.private_cus.setTag(arg0);
		zujian.private_cus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int post = (Integer) arg0.getTag();
					Intent user = new Intent();
					   String user_name = (String)data.get(post).get("user");
					//Log.d("wang","MyRoadAdapter user name is " + user_name);
                                        if(user_name == null) {
					//Log.d("wang","1 MyRoadAdapter user name is " + user_name);
                                                user_name = "";
                                        }

					user.putExtra("user",user_name);
					user.setClass(MyRoadAdapter.this.context, ProfileActivity.class);
					MyRoadAdapter.this.context.startActivity(user);
				}
			});
		return arg1;
	}
    public void updataView(int posi, ListView listView) {  
        int visibleFirstPosi = listView.getFirstVisiblePosition();  
        int visibleLastPosi = listView.getLastVisiblePosition();  
	Log.d("wang","visibleFirstPosi " + visibleFirstPosi + " visibleLastPosi " + visibleLastPosi + " posi " + posi);
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {  
            View view = listView.getChildAt(posi - visibleFirstPosi);  
	    TextView news=(BadgeView)view.findViewById(R.id.news);
	    news.setVisibility(View.GONE);
	    //listView.addView(view, posi);
        }  
    } 

}
