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
import com.example.base.BaseMessage;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.CommentAdapter;
import com.example.list.ReplyAdapter;
import com.example.model.Camera;
import com.example.model.Comment;
import com.example.model.Reply;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ReplyActivity extends BaseUi{
		
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
		private String commentId;
		private List<Map<String, Object>> listItem;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.reply);
	    HashMap<String, String> commentParams = new HashMap<String, String>();
	    
	    lv = (ListView) findViewById(R.id.list);
	    lv.setPadding(0, 0, 0, 0);
	    View v = View.inflate(ReplyActivity.this, R.layout.reply_header,null);
	    content_tv =(TextView)v.findViewById(R.id.content);
		time_tv=(TextView)v.findViewById(R.id.time);
		private_cus=(LinearLayout)v.findViewById(R.id.private_cus);
		username=(TextView)v.findViewById(R.id.userName);
		reply=(LinearLayout)v.findViewById(R.id.reply);
		replyContent = (EditText)findViewById(R.id.add_reply);
		replySubmit = (TextView)findViewById(R.id.reply_submit);
		username.setText(getIntent().getExtras().get("user").toString());
		time_tv.setText(getIntent().getExtras().get("time").toString());
		content_tv.setText(getIntent().getExtras().get("comment").toString());
		commentId = getIntent().getExtras().get("commentId").toString();
		replySubmit.setOnClickListener(new OnClickListener() {

                        @SuppressLint("NewApi")
						@Override
                        public void onClick(View arg0) {
                                // TODO Auto-generated method stub
	    			HashMap<String, String> commentParams = new HashMap<String, String>();
				content = ReplyActivity.this.replyContent.getText().toString();
				String sign = app.getSign();
				String user = app.getUser();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        	time = df.format(new Date());// new Date()为获取当前系统时间
				commentParams.put("commentId", commentId);	
				commentParams.put("content", content);	
				if(sign.isEmpty()) {
					name = user;
					commentParams.put("name", user);
				} else {
					name = sign;
					commentParams.put("name", sign);
				}	
	    			doTaskAsync(C.task.replyCreate, C.api.replyCreate,commentParams);
                        }
                });
	
		private_cus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent user = new Intent();
				user.setClass(ReplyActivity.this, ProfileActivity.class);
				ReplyActivity.this.startActivity(user);
			}
		});
	   
	     lv.addHeaderView(v);
	     listItem = new ArrayList<Map<String, Object>>();
	     /*adapter = new SimpleAdapter(this,listItem,R.layout.comment,
					new String[]{"user","time","comment"},
					new int[]{R.id.userName,R.id.time,R.id.content});*/
	      adapter = new ReplyAdapter(ReplyActivity.this,listItem);
			lv.setAdapter(adapter);
		 commentParams.put("commentId", commentId);
	    doTaskAsync(C.task.replyList, C.api.replyList,commentParams);
    }
private void getData(ArrayList<Reply> dataList) {
	Map<String, Object> map;
	for(Reply data : dataList){
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
		case C.task.replyCreate:
			toast("create comment reply succefully");
			 Map<String, Object> map = new HashMap<String, Object>();
                                map.put("comment", content);
                                map.put("user", name);
                                map.put("time", time);
                                listItem.add(0,map);
                                adapter.notifyDataSetChanged();
			break;
		case C.task.replyList:
			Log.d("wang","entry commentList");
			
		try {
			if(message.getCode().equals("10000")) {
				ArrayList<Reply> replyList;
				replyList = (ArrayList<Reply>) message.getResultList("Reply");
				getData(replyList);
				adapter.notifyDataSetChanged();
			} else {
				Map<String, Object> map1 = new HashMap<String, Object>();
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