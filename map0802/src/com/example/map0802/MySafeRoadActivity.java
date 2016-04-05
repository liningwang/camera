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
import com.example.list.MyRoadAdapter;
import com.example.list.RoadAdapter;
import com.example.model.SafeEachRoad;
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

public class MySafeRoadActivity extends BaseUi {
	
	private ImageView imageView;
	private String faceImageUrl;
	private ListView lv;
	private MyRoadAdapter adapter;
	private TextView add_road;
	private ArrayList<Map<String, Object>> listItem;
	private String name;
	private String safeid;
	private String replycount;
        private String time;
        private String content;
        private String url;
	private int itemId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_safe_road);
		
		
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
					Intent intent = new Intent();
        				intent.putExtra("result","ok1");
        				setResult(RESULT_OK, intent);
                                        finish();
                                }
                        });

		View v = View.inflate(MySafeRoadActivity.this, R.layout.safe_road_header,null);
		add_road = (TextView)v.findViewById(R.id.add_saferoad);
		add_road.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.setClass(MySafeRoadActivity.this, AddRoadActivity.class);
				
                                MySafeRoadActivity.this.startActivityForResult(intent, 1);
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
					itemId = arg2 - 1;
                                        intent.setClass(MySafeRoadActivity.this, ReplyRoadActivity.class);
                                        startActivityForResult(intent,1);
                                }
                        }
                });
		listItem = new ArrayList<Map<String, Object>>();
       adapter = new MyRoadAdapter(this,listItem);
		lv.setAdapter(adapter);
		HashMap<String, String> commentParams = new HashMap<String, String>();
		String customerId = String.valueOf(app.getCustomerid());
		commentParams.put("customerId",customerId);
		doTaskAsync(C.task.safeRoadEachCountById, C.api.safeRoadEachCountById,commentParams);
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
private void getData(ArrayList<SafeEachRoad> dataList) {
        Map<String, Object> map;
        for(SafeEachRoad data : dataList){
                map = new HashMap<String, Object>();
                map.put("user", data.getUsername());
                map.put("time", data.getUptime());
                map.put("comment", data.getContent());
                map.put("url", data.getUrl());
                map.put("safeId", data.getId());
                map.put("replycount", data.getReplycount());
                map.put("itemcount", data.getItemcount());
                listItem.add(map);
        }
}

public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch(taskId){
		case C.task.safeRoadEachCountById:
                        Log.d("wang","entry safeEachRoadList");
		try {
                        if(message.getCode().equals("10000")) {
                                ArrayList<SafeEachRoad> safeRoadList;
                                safeRoadList = (ArrayList<SafeEachRoad>) message.getResultList("SafeEachRoad");
                                getData(safeRoadList);
                                adapter.notifyDataSetChanged();
                        } else {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("empty","1");
                                map.put("comment", "目前没有任何评论，劳烦您为外地车贡献一个有用的评论");
                                listItem.add(map);
                                adapter.notifyDataSetChanged();
                        }

		int allCount = computeAllNews();
		app.setAllCount(allCount);
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

			break; 
        }
}

@SuppressLint("NewApi")
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.d("wang","MyRoad onActivityResult");
        if(data != null) {
                String result = data.getExtras().getString("result");
                if(!result.isEmpty()){
			if(result.equals("ok1")) {
				Log.d("wang","result is ok1");
                                //adapter.updataView(itemId, lv);
				listItem.get(itemId).put("itemcount", "0");
				int allCount = computeAllNews();
				app.setAllCount(allCount);
				Log.d("wang","allCount is " + allCount);
                                adapter.notifyDataSetChanged();
                                //adapter.notifyAll();
			}
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
                        }
                }
        }
}
private int computeAllNews() {
	int count = 0;
	for(int i = 0; i < listItem.size(); i++) {
		
		count = count + Integer.valueOf((String) listItem.get(i).get("itemcount"));	

	}	
	Log.d("wang","computeAllNews is " + count);
	return count;
}
@Override
public void onBackPressed() {
// TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("result","ok1");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
}

}
                    
