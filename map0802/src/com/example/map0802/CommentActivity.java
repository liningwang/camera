package com.example.map0802;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.R;
import com.example.base.BaseMessage;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.CommentAdapter;
import com.example.model.Camera;
import com.example.model.Comment;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class CommentActivity extends BaseUi{
		private TextView v;
		private ListView lv;
		CommentAdapter adapter;
		private String name;
		private String time;
		private String content;
		List<Map<String, Object>> listItem;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_camera_comment);
	    TextView name = (TextView) findViewById(R.id.cameraName);
	    TextView address = (TextView) findViewById(R.id.cameraAddress);
	    TextView time = (TextView) findViewById(R.id.cameraDatetime);
	    TextView owner = (TextView) findViewById(R.id.owner);
	    TextView v_return = (TextView) findViewById(R.id.tv_return);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });

	    name.setText(app.getCameraName());
	    address.setText(app.getCameraAddress() + app.getCameraDirecton());
	    time.setText(app.getCameraUptime());
	    owner.setText("´´½¨Õß£º" + app.getUser());
	    HashMap<String, String> commentParams = new HashMap<String, String>();
	    
	    lv = (ListView) findViewById(R.id.list);
	    lv.setPadding(0, 0, 0, 0);
	    View v = View.inflate(CommentActivity.this, R.layout.list_header,null);
	     TextView add_com = (TextView) v.findViewById(R.id.add_comment);
	     add_com.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(CommentActivity.this, AddCommentActivity.class);
				CommentActivity.this.startActivityForResult(intent, 1);
			}
		});
	     lv.addHeaderView(v);
	     lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if((listItem.get(arg2-1).get("commentId")) != null) {
					Intent intent = new Intent();
					intent.putExtra("user",listItem.get(arg2-1).get("user").toString());
					intent.putExtra("time", listItem.get(arg2-1).get("time").toString());
					intent.putExtra("comment", listItem.get(arg2-1).get("comment").toString());
					intent.putExtra("commentId", listItem.get(arg2-1).get("commentId").toString());
					intent.putExtra("count", listItem.get(arg2-1).get("replycount").toString());
					intent.setClass(CommentActivity.this, ReplyActivity.class);
					startActivity(intent);
				}
			}
		});
	     listItem = new ArrayList<Map<String, Object>>();
	     /*adapter = new SimpleAdapter(this,listItem,R.layout.comment,
					new String[]{"user","time","comment"},
					new int[]{R.id.userName,R.id.time,R.id.content});*/
	      adapter = new CommentAdapter(CommentActivity.this,listItem);
			lv.setAdapter(adapter);
		 commentParams.put("cameraId", app.getCameraId());
	    doTaskAsync(C.task.commentList, C.api.commentList,commentParams);
    }
private void getData(ArrayList<Comment> dataList) {
	Map<String, Object> map;
	for(Comment data : dataList){
		map = new HashMap<String, Object>();
		map.put("user", data.getName());
		map.put("time", data.getUptime());
		map.put("comment", data.getContent());
		map.put("commentId", data.getId());
		map.put("replycount", data.getReplycount());
		listItem.add(map);
	}
}
public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.commentCreate:
			toast("create comment succefully");
			break;
		case C.task.commentList:
			Log.d("wang","entry commentList");
			
		try {
			if(message.getCode().equals("10000")) {
				ArrayList<Comment> commentList;
				commentList = (ArrayList<Comment>) message.getResultList("Comment");
				getData(commentList);
				adapter.notifyDataSetChanged();
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("empty","1");
				map.put("comment", "no momment,please add a comment");
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
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if(data != null) {
		String result = data.getExtras().getString("result");
		if(!result.isEmpty()){
			if(result.equals("ok")) {
				name = data.getExtras().getString("name");
				if(name.isEmpty()) {
					name = data.getExtras().getString("user");
				}
				content = data.getExtras().getString("content");
				time = data.getExtras().getString("time");
				if((listItem.get(0).get("empty") != null)&&(listItem.get(0).get("empty").equals("1"))){
					listItem.remove(0);
				}
				Log.d("wang","CommentActivity name: " + name + " content: " + content + " time: " + time);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("comment", content);
				map.put("user", name);
				map.put("time", time);
				listItem.add(0,map);
				adapter.notifyDataSetChanged();
			}
		}
	}
}
}
