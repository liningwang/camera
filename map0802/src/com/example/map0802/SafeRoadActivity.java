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
		View v = View.inflate(SafeRoadActivity.this, R.layout.safe_road_header,null);
		add_road = (TextView)v.findViewById(R.id.add_saferoad);
		add_road.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.setClass(SafeRoadActivity.this, AddRoadActivity.class);
				
                                SafeRoadActivity.this.startActivityForResult(intent, 1);
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
        for(SafeRoad data : dataList){
                map = new HashMap<String, Object>();
                map.put("user", data.getUsername());
                map.put("time", data.getUptime());
                map.put("comment", data.getContent());
                map.put("url", data.getUrl());
                map.put("safeId", data.getId());
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
                                map.put("comment", "目前没有任何评论，劳烦您为外地车贡献一个有用的评论");
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
                                listItem.add(0,map);
                                adapter.notifyDataSetChanged();
                        }
                }
        }
}
                    
   private final class TouchListener implements OnTouchListener {

        /** 记录是拖拉照片模式还是放大缩小照片模式 */
        private int mode = 0;// 初始状态    
        /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;

        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        /** 用于记录拖拉图片移动的坐标位置 */
        private Matrix matrix = new Matrix();
        /** 用于记录图片要进行拖拉时候的坐标位置 */
        private Matrix currentMatrix = new Matrix();

        /** 两个手指的开始距离 */
        private float startDis;
        /** 两个手指的中间点 */
        private PointF midPoint;

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (arg1.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕  
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DRAG;
                // 记录ImageView当前的移动位置  
                currentMatrix.set(imageView.getImageMatrix());
                startPoint.set(arg1.getX(), arg1.getY());
                break;
            // 手指在屏幕上移动，改事件会被不断触发  
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片  
                if (mode == MODE_DRAG) {
                    float dx = arg1.getX() - startPoint.x; // 得到x轴的移动距离  
                    float dy = arg1.getY() - startPoint.y; // 得到x轴的移动距离  
                    // 在没有移动之前的位置上进行移动  
                    matrix.set(currentMatrix);
                    matrix.postTranslate(dx, dy);
                }
                // 放大缩小图片  
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(arg1);// 结束距离  
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10  
                        float scale = endDis / startDis;// 得到缩放倍数  
                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                    }
                                                                                                                                                                                          
                }
                break;
            // 手指离开屏幕  
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)  
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕  
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = distance(arg1);
                /** 计算两个手指间的中间点 */
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10  
                    midPoint = mid(arg1);
                    //记录当前ImageView的缩放倍数  
                    currentMatrix.set(imageView.getImageMatrix());
                }
                break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }

        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return FloatMath.sqrt(dx * dx + dy * dy);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }


    }


}
