package com.wangln.map0802;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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
import com.wangln.R;
import com.wangln.base.BaseMessage;
import com.wangln.base.BaseUi;
import com.wangln.base.C;
import com.wangln.list.CommentAdapter;
import com.wangln.list.HorizontalScrollViewAdapter;
import com.wangln.list.MyHorizontalScrollView;
import com.wangln.list.MyHorizontalScrollView.OnItemCheckListener;
import com.wangln.list.MyHorizontalScrollView.OnItemClickListener;
import com.wangln.list.SafeRoadAdapter;
import com.wangln.model.Camera;
import com.wangln.model.Comment;
import com.wangln.model.SafeId;
import com.wangln.util.AppCache;
import com.wangln.util.SDUtil;

import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class AddRoadActivity extends BaseUi{
	private TextView submit;
	private EditText addContent;
	private TextView upload;
	private TextView sure_upload;
	private List<Map<String, Object>> listItem;
	private List<String> mDatas;
	private ListView lv;
	private LinearLayout layout;
	private boolean isChecked = false;
	private boolean hasPicture = false;
//	private SafeRoadAdapter adapter;
	private MyHorizontalScrollView mHorizontalScrollView;
        private HorizontalScrollViewAdapter adapter;
	private int position;
	private ImageView image;
	private String url;
	private ProgressDialog progressDialog = null;

@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_road);
	    submit = (TextView) findViewById(R.id.submit);
	    sure_upload = (TextView) findViewById(R.id.upload_button);
	    addContent = (EditText) findViewById(R.id.add_content);
	    image = (ImageView) findViewById(R.id.image);
	    upload = (TextView) findViewById(R.id.upload);
	    lv = (ListView) findViewById(R.id.list);
	    SDUtil.computeScreenSize(AddRoadActivity.this);
            layout = (LinearLayout) findViewById(R.id.picture);
	    layout.setVisibility(View.GONE);
	    //listItem = new ArrayList<Map<String, Object>>();
	   mDatas = new ArrayList<String>();
	    TextView v_return = (TextView) findViewById(R.id.re);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });

	    upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				layout.setVisibility(View.VISIBLE);
				upload.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						upload.setEnabled(false);
						//upload.setClickable(false);
						getPicture();
         					adapter = new HorizontalScrollViewAdapter(AddRoadActivity.this, mDatas);
                				mHorizontalScrollView.initDatas(adapter);
						
					}
				});
			}
		});
	    submit.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String content = AddRoadActivity.this.addContent.getText().toString();
				String customerId = String.valueOf(app.getCustomerid());
				Log.d("wang"," AddRoadActivity customerId is " + customerId);
				if(!mDatas.isEmpty()) {
					String uploadFile = mDatas.get(position);
					File file1 = new File(uploadFile);
					String fileName = AppCache.getMd5Path(file1.getName());
					url = C.api.imageUrl + fileName;
				} else {
					url = "";
				}

				HashMap<String, String> commentParams = new HashMap<String, String>();
				commentParams.put("username",app.getUser());
				commentParams.put("content", content);
				commentParams.put("customerId",customerId);
				commentParams.put("replycount","0");
				commentParams.put("url",url);
				doTaskAsync(C.task.safeRoadCreate, C.api.safeRoadCreate,commentParams);
			}
		});
	 mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
	 mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener()
                {

                        @Override
                        public void onClick(View view, int position)
                        {
				//toast("click successfully position is " + position);
                                //mImg.setImageResource(mDatas.get(position));
                                //view.setBackgroundColor(Color.parseColor("#AA024DA4"));
				AddRoadActivity.this.position = position;
				 HashMap<String, String> commentParams = new HashMap<String, String>();
				 commentParams.put("file", mDatas.get(position));
				 progressDialog = ProgressDialog.show(AddRoadActivity.this, "上传照片", "照片上传中...", true);
			    	doTaskAsync(C.task.upload, C.api.upload,commentParams,true);
                        }
                });
	mHorizontalScrollView.setOnItemCheckedListener(new OnItemCheckListener()
		{
			 @Override
                        public void onClick(View view, int position)
                        {
				isChecked = true;
				//toast("checked successfully position is " + position);
				AddRoadActivity.this.position = position;
			}
		});
	 sure_upload.setOnClickListener(new OnClickListener()
                {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if(isChecked) {
								 HashMap<String, String> commentParams = new HashMap<String, String>();
							 	commentParams.put("file", mDatas.get( AddRoadActivity.this.position));
				 				progressDialog = ProgressDialog.show(AddRoadActivity.this, "上传图片", "图片上传中...", true);
						    		doTaskAsync(C.task.upload, C.api.upload,commentParams,true);
							} else {
								toast("请先选择要上传的照片！");
							}
						}
                });
	 //adapter = new SafeRoadAdapter(AddRoadActivity.this,listItem);
         //lv.setAdapter(adapter);
    }
private void getPicture() {
	ContentResolver mContentResolver = AddRoadActivity.this.getContentResolver();
  	Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
  	new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
  	null, null, MediaStore.Images.Media._ID + " DESC"); // 閹稿娴橀悧鍢擠闂勫秴绨幒鎺戝灙
	int i;
	for(i = 0; i < 21; i++) {
  		mCursor.moveToNext();
	  	// 閹垫挸宓僉OG閺屻儳婀呴悡褏澧朓D閻ㄥ嫬锟斤拷
	  	long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
	  	//Log.d("wang", "id = " + id + "");
	      	// 鏉╁洦鎶ら幒澶夌瑝闂囷拷鐟曚胶娈戦崶鍓у閿涘苯褰ч懢宄板絿閹峰秶鍙庨崥搴＄摠閸屻劎鍙庨悧鍥╂畱閻╃鍞介柌宀�娈戦崶鍓у
	        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
	    	Log.d("wang","image path=" + path);
		/*Bitmap bitmap =	SDUtil.getImageforSize(path,300,480);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image",bitmap);
		listItem.add(map);*/
		mDatas.add(path);
  	}
  	mCursor.close();

}
public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.upload:
			progressDialog.dismiss();
			toast("上传图片成功！");
			//mHorizontalScrollView.setVisibility(View.GONE);
			//mHorizontalScrollView.removeAllViews();
			layout.setVisibility(View.GONE);
			//layout.removeView(mHorizontalScrollView);
			//layout.removeView(sure_upload);
			ImageView v = new ImageView(this);
			int w = image.getWidth();
			int h = image.getHeight();
			hasPicture = true;
			image.setImageBitmap(SDUtil.getImageforSize(mDatas.get(position),w,h));	
			upload.setEnabled(true);
			break;
		case C.task.safeRoadCreate:
			//toast(message.getMessage());
		SafeId safeid;
		try {
			safeid = (SafeId) message.getResult("SafeId");
		
			Log.d("wang","entry addRoad List safeid is " + safeid.getId() + "username is " + app.getUser());
			Intent intent = new Intent();
			intent.putExtra("content",AddRoadActivity.this.addContent.getText().toString());
			intent.putExtra("hasPicture", hasPicture);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//鐠佸墽鐤嗛弮銉︽埂閺嶇厧绱�
                        intent.putExtra("time",df.format(new Date()));// new Date()娑撻缚骞忛崣鏍х秼閸撳秶閮寸紒鐔告闂傦拷
			intent.putExtra("result","ok");
			intent.putExtra("safeid",safeid.getId());
			intent.putExtra("username",app.getUser());
			intent.putExtra("replycount","0");
			intent.putExtra("url",url);
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
	}
}
}
