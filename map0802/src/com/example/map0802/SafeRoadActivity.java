package com.example.map0802;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.R;
import com.example.base.BaseHandler;
import com.example.base.BaseMessage;
import com.example.base.BaseTask;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.RoadAdapter;
import com.example.model.SafeRoad;
import com.example.util.AppCache;
import com.example.util.AppUtil;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SafeRoadActivity extends BaseUi {
	
	private ImageView imageView;
	private String faceImageUrl;
	private ListView lv;
	private RoadAdapter adapter;
	private TextView add_road;
	private ArrayList<Map<String, Object>> listItem;
	private String name;
	private String safeid;
	private String replycount;
        private String time;
        private String content;
        private String url;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safe_road);
		
		
		lv = (ListView) findViewById(R.id.list);
                lv.setPadding(0, 0, 0, 0);

		// set handler
		/*this.setHandler(new ConfigHandler(this));
		imageView = (ImageView) this.findViewById(R.id.iv_picture);
		faceImageUrl = "http://172.28.32.117:8004/faces/default/face.png";
		loadImage(faceImageUrl);
		imageView.setOnTouchListener(new TouchListener());*/  	
	        TextView v_return = (TextView) findViewById(R.id.tv_return);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });

		View v = View.inflate(SafeRoadActivity.this, R.layout.safe_road_header,null);
		add_road = (TextView)v.findViewById(R.id.add_saferoad);
		add_road.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                                // TODO Auto-generated method stub
				if(app.isLogin()) {
                                	Intent intent = new Intent();
                                	intent.setClass(SafeRoadActivity.this, AddRoadActivity.class);
				
                                	SafeRoadActivity.this.startActivityForResult(intent, 1);
				} else {
					toast("please login!");
                                	Intent intent = new Intent();
                                	intent.setClass(SafeRoadActivity.this, ProfileActivity.class);
					startActivity(intent);
				}
                        }
                });

		lv.addHeaderView(v);
		lv.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                                // TODO Auto-generated method stub
                                if((listItem.get(arg2-1).get("safeId")) != null) {
                                        Intent intent = new Intent();
                                        intent.putExtra("user",listItem.get(arg2-1).get("user").toString());
                                        intent.putExtra("time", listItem.get(arg2-1).get("time").toString());
                                        intent.putExtra("comment", listItem.get(arg2-1).get("comment").toString());
                                        intent.putExtra("url", listItem.get(arg2-1).get("url").toString());
                                        intent.putExtra("safeId", listItem.get(arg2-1).get("safeId").toString());
                                        intent.putExtra("count", listItem.get(arg2-1).get("replycount").toString());
                                        intent.setClass(SafeRoadActivity.this, ReplyRoadActivity.class);
                                        startActivity(intent);
                                }
                        }
                });
		listItem = new ArrayList<Map<String, Object>>();
       adapter = new RoadAdapter(this,listItem);
		lv.setAdapter(adapter);
		doTaskAsync(C.task.safeRoadList, C.api.safeRoadList);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//listItem.clear();
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
						Bitmap face = AppCache.getImage(faceImageUrl);
						imageView.setImageBitmap(face);
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				ui.toast(e.getMessage());
			}
		}
	}
private void getData(ArrayList<SafeRoad> dataList) {
        Map<String, Object> map;
        int length;
	if(app.isLogin()){
		length = dataList.size();
	} else {
		length = 3;
		View v = View.inflate(SafeRoadActivity.this, R.layout.saferoad_foot,null);
		TextView login = (TextView) v.findViewById(R.id.login);
		      login.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
					Intent intent = new Intent();
                                        intent.setClass(SafeRoadActivity.this, ProfileActivity.class);
					startActivityForResult(intent, 1);
                                }
                        });

		lv.addFooterView(v);
	}
	SafeRoad data;
	for(int i = 0;i < length; i++) {
		data = dataList.get(i);
                map = new HashMap<String, Object>();
                map.put("user", data.getUsername());
                map.put("time", data.getUptime());
                map.put("comment", data.getContent());
                map.put("url", data.getUrl());
                map.put("safeId", data.getId());
                map.put("replycount", data.getReplycount());
                listItem.add(map);
        }
}

public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch(taskId){
                case C.task.safeRoadList:
                        Log.d("wang","entry safeRoadList");

                try {
                        if(message.getCode().equals("10000")) {
                                ArrayList<SafeRoad> safeRoadList;
                                safeRoadList = (ArrayList<SafeRoad>) message.getResultList("SafeRoad");
                                getData(safeRoadList);
                                adapter.notifyDataSetChanged();
                        } else {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("empty","1");
                                map.put("comment", "no comment, please add a new comment");
                                listItem.add(map);
                                adapter.notifyDataSetChanged();
                        }
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                break;
        }
}

@SuppressLint("NewApi")
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
    
                String result = data.getExtras().getString("result");
                if(!result.isEmpty()){
                        if(result.equals("ok")) {
                                content = data.getExtras().getString("content");
                                time = data.getExtras().getString("time");
				name = data.getExtras().getString("username");
				safeid = data.getExtras().getString("safeid");
				replycount = data.getExtras().getString("replycount");
				url = data.getExtras().getString("url");
                                if((listItem.get(0).get("empty") != null)&&(listItem.get(0).get("empty").equals("1"))){
                                        listItem.remove(0);
                                }
                                Log.d("wang","RoadActivity name: " + name + " content: " + content + " time: " + time);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("comment", content);
                                map.put("user", name);
                                map.put("time", time);
                                map.put("url", url);
                                map.put("safeId", safeid);
                		map.put("replycount", replycount);
                                listItem.add(0,map);
                                adapter.notifyDataSetChanged();
                        } else if(result.equals("ok1")) {
				toast("please reflash!!!");
			}
                }
        }
}
}
                    
