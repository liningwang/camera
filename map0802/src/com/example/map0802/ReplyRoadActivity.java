package com.example.map0802;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.R;
import com.example.base.BaseHandler;
import com.example.base.BaseMessage;
import com.example.base.BaseTask;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.CommentAdapter;
import com.example.list.ReplyAdapter;
import com.example.model.Camera;
import com.example.model.Comment;
import com.example.model.Reply;
import com.example.model.ReplyRoad;
import com.example.util.AppCache;

import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ReplyRoadActivity extends BaseUi{
		
		private ListView lv;
		ReplyAdapter adapter;
		private String name;
		private String time;
		private String content;
		public TextView content_tv;
		public TextView time_tv;
		public TextView username;
		public LinearLayout private_cus;
		public LinearLayout reply;
		private TextView replySubmit;
		private EditText replyContent;
		private String safeId;
		private int count;
		private List<Map<String, Object>> listItem;
		private String url;
		private ImageView image;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.reply);
	    HashMap<String, String> commentParams = new HashMap<String, String>();
	    
	    lv = (ListView) findViewById(R.id.list);
	    lv.setPadding(0, 0, 0, 0);
	    View v = View.inflate(ReplyRoadActivity.this, R.layout.reply_road_header,null);
	    content_tv =(TextView)v.findViewById(R.id.content);
		time_tv=(TextView)v.findViewById(R.id.time);
		private_cus=(LinearLayout)v.findViewById(R.id.private_cus);
		username=(TextView)v.findViewById(R.id.userName);
		reply=(LinearLayout)v.findViewById(R.id.reply);
		image = (ImageView)v.findViewById(R.id.image);
		replyContent = (EditText)findViewById(R.id.add_reply);
		replySubmit = (TextView)findViewById(R.id.reply_submit);
		TextView v_return = (TextView) findViewById(R.id.tv_return);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });

		

		username.setText(getIntent().getExtras().get("user").toString());
		time_tv.setText(getIntent().getExtras().get("time").toString());
		content_tv.setText(getIntent().getExtras().get("comment").toString());
		safeId = getIntent().getExtras().get("safeId").toString();
		url = getIntent().getExtras().get("url").toString();
		count = Integer.valueOf(getIntent().getExtras().get("count").toString());
		Log.d("wang","image url is " + url);

		this.setHandler(new ConfigHandler(this));
                loadImage(url);

		replySubmit.setOnClickListener(new OnClickListener() {

                        @SuppressLint("NewApi")
						@Override
                        public void onClick(View arg0) {
                                // TODO Auto-generated method stub
	    			HashMap<String, String> commentParams = new HashMap<String, String>();
				content = ReplyRoadActivity.this.replyContent.getText().toString();
				String sign = app.getSign();
				String user = app.getUser();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        	time = df.format(new Date());// new Date()为获取当前系统时间
				commentParams.put("safeId", safeId);	
				commentParams.put("content", content);	
				if(sign.isEmpty()) {
					name = user;
					commentParams.put("name", user);
				} else {
					name = sign;
					commentParams.put("name", sign);
				}	
	    			doTaskAsync(C.task.replyRoadCreate, C.api.replyRoadCreate,commentParams);
                        }
                });
	
		private_cus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent user = new Intent();
				user.setClass(ReplyRoadActivity.this, ProfileActivity.class);
				ReplyRoadActivity.this.startActivity(user);
			}
		});
	     image.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                Intent user = new Intent();
                                user.putExtra("url",url);
                                user.setClass(ReplyRoadActivity.this, ScaleImageActivity.class);
                                startActivity(user);
                        }
                });
 
	     lv.addHeaderView(v);
	     listItem = new ArrayList<Map<String, Object>>();
	     /*adapter = new SimpleAdapter(this,listItem,R.layout.comment,
					new String[]{"user","time","comment"},
					new int[]{R.id.userName,R.id.time,R.id.content});*/
	      adapter = new ReplyAdapter(ReplyRoadActivity.this,listItem);
			lv.setAdapter(adapter);
	    commentParams.put("safeId", safeId);
	    doTaskAsync(C.task.replyRoadList, C.api.replyRoadList,commentParams);
    }
 private class ConfigHandler extends BaseHandler {
                public ConfigHandler(BaseUi ui) {
                        super(ui);
                }
                @Override
                public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        try {
                                switch (msg.what) {
                                        case BaseTask.LOAD_IMAGE:
						Log.d("wang","BaseTask.LOAD_IMAGE");
						int w = image.getWidth();
                        			int h = image.getHeight();
                        			image.setImageBitmap(AppCache.getImageAsSize(url,w,h));
                                                break;
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                                ui.toast(e.getMessage());
                        }
                }
        }

private void getData(ArrayList<ReplyRoad> dataList) {
	Map<String, Object> map;
	for(ReplyRoad data : dataList){
		map = new HashMap<String, Object>();
		map.put("user", data.getName());
		map.put("time", data.getUptime());
		map.put("comment", data.getContent());
		listItem.add(map);
	}
}
public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.replyRoadCreate:
			toast("create comment reply succefully");
			if((listItem.get(0).get("empty") != null)&&(listItem.get(0).get("empty").equals("1"))){
                                        listItem.remove(0);
                                }
			 Map<String, Object> map = new HashMap<String, Object>();
                                map.put("comment", content);
                                map.put("user", name);
                                map.put("time", time);
                                listItem.add(0,map);
                                adapter.notifyDataSetChanged();
			 HashMap<String, String> commentParams = new HashMap<String, String>();
			 commentParams.put("safeId", safeId);	
			 count = count + 1;
			 commentParams.put("count", String.valueOf(count));	
			 doTaskAsync(C.task.safeRoadCount, C.api.safeRoadCount,commentParams);
			break;
		case C.task.safeRoadCount:
			break;
		case C.task.replyRoadList:
			Log.d("wang","entry reply road commentList");
			
		try {
			if(message.getCode().equals("10000")) {
				ArrayList<ReplyRoad> replyList;
				replyList = (ArrayList<ReplyRoad>) message.getResultList("ReplyRoad");
				getData(replyList);
				adapter.notifyDataSetChanged();
			} else {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("empty","1");
				map1.put("comment", "目前没有任何回复，劳烦您为外地车贡献一个有用的回复");
				listItem.add(map1);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		break;
	}
}
}
