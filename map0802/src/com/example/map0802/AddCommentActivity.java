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
import com.example.model.Camera;
import com.example.model.Comment;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class AddCommentActivity extends BaseUi{
	private RadioGroup rd_group;
	private RadioButton rd_jjz;
	private RadioButton zan;
	private RadioButton buzan;
	private int radioId;
	private TextView submit;
	private EditText addComment;
	private SharedPreferences share;
	private SharedPreferences share_no;
	private       int share_zan;
	private       int share_buzan;
	private int zan_flag;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_comment);
	    submit = (TextView) findViewById(R.id.submit);
	    addComment = (EditText) findViewById(R.id.add_comment);
        rd_group = (RadioGroup) findViewById(R.id.radio_group);
	rd_jjz = (RadioButton) findViewById(R.id.radio_jjz);
	zan = (RadioButton) findViewById(R.id.radio_rushour);
	buzan = (RadioButton) findViewById(R.id.radio_tailno);
	TextView v_return = (TextView) findViewById(R.id.re);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });

	share = getSharedPreferences("zan", MODE_PRIVATE);
	share_no = getSharedPreferences("buzan", MODE_PRIVATE);
	share_zan = share.getInt(app.getCameraId(), 0);
	share_buzan =  share_no.getInt(app.getCameraId(), 0);
	zan_flag = 0;
	if(share_zan != 0) {
		zan.setChecked(true);
		zan_flag = 1;
	} else if(share_buzan != 0) {
		buzan.setChecked(true);
		zan_flag = 1;
	}
	    rd_group.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if(zan_flag == 0) {
					if(arg1 == rd_jjz.getId()) {
						radioId = arg1;
						Toast.makeText(AddCommentActivity.this, "radio id:" + rd_jjz.getId() + " text: " + rd_jjz.getText(), Toast.LENGTH_LONG).show();
					} else if(arg1 == zan.getId()) {
						radioId = arg1;
						HashMap<String, String> locationParams = new HashMap<String, String>();				      
						locationParams.put("cameraId", app.getCameraId());
						doTaskAsync(C.task.zan,C.api.zan,locationParams);
						Toast.makeText(AddCommentActivity.this, "radio id:" + zan.getId() + " text: " + zan.getText(), Toast.LENGTH_LONG).show();
					} else if(arg1 == buzan.getId()) {
						radioId = arg1;
						 HashMap<String, String> locationParams = new HashMap<String, String>();				      
						 locationParams.put("cameraId", app.getCameraId());
						 doTaskAsync(C.task.buzan,C.api.buzan,locationParams);
						Toast.makeText(AddCommentActivity.this, "radio id:" + buzan.getId() + " text: " + buzan.getText(), Toast.LENGTH_LONG).show();
					}	
				} else {
					toast("no select");
				}
			}
			
		});
	    if(zan_flag == 1) {
	    	zan.setEnabled(false);
	    	buzan.setEnabled(false);
	    	rd_jjz.setEnabled(false);
	    }
	    submit.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			if(zan.isChecked() || buzan.isChecked() || rd_jjz.isChecked()) {
				String content = AddCommentActivity.this.addComment.getText().toString();
				String cameraId = app.getCameraId();
				String customerId = String.valueOf(app.getCustomerid());
				String name = app.getSign();
				String user = app.getUser();
				 HashMap<String, String> commentParams = new HashMap<String, String>();
				 commentParams.put("cameraId", cameraId);
				 commentParams.put("customerId",customerId);
				 if(name.isEmpty()) {
					 commentParams.put("name",user);
				 } else {
					 commentParams.put("name",name);
				 }
				 commentParams.put("content",content);
			    doTaskAsync(C.task.commentCreate, C.api.commentCreate,commentParams);
			} else {
			  toast("璇峰厛閫夋嫨鏄惁鎷嶅鍦拌溅");	
			}
			}
		});
    }

public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.commentCreate:
			toast("create comment succefully");
			Intent intent = new Intent();
			Log.d("wang"," addComment value:" + addComment.getText() + " name: " + app.getSign() + " user:"+app.getUser()+" cameraId:" + app.getCameraId()
					+ " customerId: " + app.getCustomerid());
			intent.putExtra("content",AddCommentActivity.this.addComment.getText().toString());	
			intent.putExtra("cameraId",app.getCameraId());
			intent.putExtra("customerId",String.valueOf(app.getCustomerid()));
			intent.putExtra("name",app.getSign());
			intent.putExtra("user",app.getUser());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//璁剧疆鏃ユ湡鏍煎紡
			intent.putExtra("time",df.format(new Date()));// new Date()涓鸿幏鍙栧綋鍓嶇郴缁熸椂闂�
			intent.putExtra("result","ok");
			setResult(RESULT_OK, intent);
			finish();
			break;
		case C.task.commentList:
			Log.d("wang","entry commentList");
		break;
		case C.task.zan:
			if(message.getCode().equals("10000")) {
				SharedPreferences share = getSharedPreferences("zan", MODE_PRIVATE);
				toast(message.getMessage());
				SharedPreferences.Editor edit = share.edit();
				edit.putInt(app.getCameraId(), 1);
				edit.commit();
			}
			break;
		case C.task.buzan:
			if(message.getCode().equals("10000")) {
				SharedPreferences share = getSharedPreferences("buzan", MODE_PRIVATE);
				toast(message.getMessage());
				SharedPreferences.Editor edit = share.edit();
				edit.putInt(app.getCameraId(), 1);
				edit.commit();
			}
			break;
	}
}
}
