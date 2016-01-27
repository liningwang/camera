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
import com.example.model.Camera;
import com.example.model.Comment;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class CommentActivity extends BaseUi{
		private TextView v;
		private ListView lv;
		SimpleAdapter adapter;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_camera_comment);	
	    HashMap<String, String> commentParams = new HashMap<String, String>();
	    
	    lv = (ListView) findViewById(R.id.list);
	   
		commentParams.put("cameraId", String.valueOf(2));
	    doTaskAsync(C.task.commentList, C.api.commentList,commentParams);
    }
private List<Map<String, Object>> getData(ArrayList<Comment> dataList) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Map<String, Object> map;
	for(Comment data : dataList){
		map = new HashMap<String, Object>();
		map.put("user", data.getName());
		map.put("time", data.getUptime());
		map.put("comment", data.getContent());
		list.add(map);
	}
	return list;
}
public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.commentCreate:
			toast("create comment succefully");
			break;
		case C.task.commentList:
			Log.d("wang","entry commentList");
			ArrayList<Comment> commentList;
		try {
			commentList = (ArrayList<Comment>) message.getResultList("Comment");
		    adapter = new SimpleAdapter(this,getData(commentList),R.layout.comment,
	                new String[]{"user","time","comment"},
	                new int[]{R.id.userName,R.id.time,R.id.content});
		    lv.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		break;
	}
}
}
