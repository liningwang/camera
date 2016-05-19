package com.wangln.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wangln.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.SimpleAdapter;

public class CameraComment extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_comment);
	    SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.comment,
	                new String[]{"user","time","comment"},
	                new int[]{R.id.userName,R.id.time,R.id.content});
	    setListAdapter(adapter);
	}
	 
	    private List<Map<String, Object>> getData() {
	        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	 
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("user", "G1");
	        map.put("time", "google 1");
	        map.put("comment", "haha");
	        list.add(map);
	 
	        map = new HashMap<String, Object>();
	        map.put("user", "G2");
	        map.put("time", "google 2");
	        map.put("comment", "xixi");
	        list.add(map);
	 
	        map = new HashMap<String, Object>();
	        map.put("user", "G3");
	        map.put("time", "google 3");
	        map.put("comment", "jiji");
	        list.add(map);
	         
	        return list;
	    }
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_comment, menu);
		return true;
	}

}
