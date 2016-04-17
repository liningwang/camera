package com.example.map0802;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.shareData.CustomerInfo;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.Poi;
import com.baidu.lbsapi.panoramaview.*;

import com.example.R;
import com.example.base.BaseMessage;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;

import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.CameraComment;
import com.example.model.AllRoadCount;
import com.example.model.Camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;


public class MainActivity extends BaseUi {
    MapView mMapView = null;
   private BaiduMap mBaiduMap;
   private BitmapDescriptor mIconMaker; 
  private RelativeLayout mMarkerInfoLy;
  private boolean flag = false;
  private ImageView image;
  private TextView addMark;
  private LinearLayout profile;
  private TextView tv;
  private Overlay cameraOverlay;
  private Overlay locationOverlay;
  private LatLng cameraLocation;
  private LatLng locationMark;
  private String et_desc;
  private String et_addr;
  private String camera_typ;
  private String direction;
  private TextView mZanCount;
  private TextView mBuZanCount;
  private int mCurrentZan;
  private int mCurrentBuZan;
  private ArrayList<Camera> cameraList;
  private Camera mCurrentCamera;
  private InfoWindow mInfoWindow;
  private 	int share_zan;
  private 	int share_buzan;
  private TextView safeRoad;
  private TextView about;
  private TextView news;
  private String newsCount;
  private LocationClient mLocClient;
  private ProgressDialog progressDialog = null;
  private MyLocationListenner myListener;
  private boolean isFirstLoc = true;
  private LocationMode mCurrentMode;
  private BitmapDescriptor mCurrentMarker;
  private TextView addLocation;
  private TextView removeLocation;
  private int tag_location = 0;
  private ArrayList<LatLng> latArray = new ArrayList<LatLng>();
  private ArrayList<Overlay> overlayArray = new ArrayList<Overlay>();
  int tag=0;
  private TextToSpeech tts;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.bmapView); 
	mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
       
	mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
	mBaiduMap = mMapView.getMap();
	image = (ImageView) mMarkerInfoLy.findViewById(R.id.info_img);
	tv = (TextView) mMarkerInfoLy.findViewById(R.id.info_name);
	addMark=(TextView) findViewById(R.id.add_camera);
	addLocation = (TextView) findViewById(R.id.add_location);
	removeLocation = (TextView) findViewById(R.id.remove_location);
	profile = (LinearLayout) findViewById(R.id.profile);
	safeRoad = (TextView) findViewById(R.id.camera_topic);
	about = (TextView) findViewById(R.id.about);
	news = (TextView) findViewById(R.id.news);
	
	safeRoad.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                intent.setClass(MainActivity.this, SafeRoadActivity.class);
                startActivity(intent);
                }
        });

	about.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                intent.setClass(MainActivity.this, UpdateApkActivity.class);
                startActivity(intent);
                }
        });

	profile.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
		intent.putExtra("newsCount",newsCount);
        	intent.setClass(MainActivity.this, ProfileActivity.class);
		startActivityForResult(intent,1);
		}
	});
	tv.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent intent = new Intent();
        	intent.setClass(MainActivity.this, CommentActivity.class);
        	startActivity(intent);
		}
		
	});
	image.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 Intent intent = new Intent();  
               
             intent.setType("image/*");  
               
             intent.setAction(Intent.ACTION_GET_CONTENT);   
              
             startActivityForResult(intent, 1); 
		}
		
	});
	//showMark();
	setOnclickListener();
	initClickMap();
	initMarkerClickEvent();
	setCustomerInfo();
	int customerId = app.getCustomerid();
	if(customerId != 0) 
	{
		HashMap<String, String> params = new HashMap<String, String>();
        	params.put("customerId", String.valueOf(customerId));
		Log.d("wang","customerId is" + customerId);
		progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "加载中....", true);
   		doTaskAsync(C.task.safeRoadCountById, C.api.safeRoadCountById,params);
	} else {
		news.setText("0");	
                news.setVisibility(View.GONE);
	}	
   	doTaskAsync(C.task.getCamera, C.api.getCamera);
   	initLocation();
   	initTTS();
   //	initPanorama();
    } 
   
    private void setOnclickListener() {
		// TODO Auto-generated method stub
    	
    	
    	
		addMark.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					tag = 1;
				}
		});
		addLocation.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					tag_location = 1;
				}
		});
		removeLocation.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					for(Overlay over : overlayArray) {
						over.remove();
						latArray.clear();
					}
				}
		});
	}
private void initTTS(){
	OnInitListener listener = new TtsListener();
	tts = new TextToSpeech(this, listener);
	
}
private void initPanorama(){
	//PanoramaView mPanoView = (PanoramaView)findViewById(R.id.panorama);
	//mPanoView.setPanorama("0100220000130817164838355J5");
}
class TtsListener implements OnInitListener
{
	public void onInit(int status) {  
        //如果装载TTS引擎成功  
        if(status == TextToSpeech.SUCCESS)  
        {  
            //设置使用美式英语朗读(虽然设置里有中文选项Locale.Chinese,但并不支持中文)  
            int result = tts.setLanguage(Locale.CHINESE);  
            //如果不支持设置的语言  
            if(result != TextToSpeech.LANG_COUNTRY_AVAILABLE   
                    && result != TextToSpeech.LANG_AVAILABLE)  
            {  
                Toast.makeText(MainActivity.this, "TTS暂时不支持这种语言朗读", 50000).show();  
            }  
        }  
    }
}
private void initLocation() {
    // 开启定位图层
	mCurrentMode = LocationMode.NORMAL;
	 mBaiduMap
     .setMyLocationConfigeration(new MyLocationConfiguration(
             mCurrentMode, true, null));
    mBaiduMap.setMyLocationEnabled(true);
    // 定位初始化
    mLocClient = new LocationClient(this);
    myListener = new MyLocationListenner();
    mLocClient.registerLocationListener(myListener);
    LocationClientOption option = new LocationClientOption();
    option.setOpenGps(true); // 打开gps
    option.setCoorType("bd09ll"); // 设置坐标类型
    option.setScanSpan(5000);
    mLocClient.setLocOption(option);
    mLocClient.start();
}
public class MyLocationListenner implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        Log.d("wang","onReceiveLocation successfully");
    	if (location == null || mMapView == null) {
            return;
        }
    	Log.d("wang","onReceiveLocation location is null");
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        //mMapView.refreshDrawableState();
        if(latArray.size() != 0) {
        	float[] distance = new float[1];
        	for(int i = 0; i < latArray.size();i++) {
        		LatLng loc = latArray.get(i);
        		Location.distanceBetween(loc.latitude, loc.longitude, location.getLatitude(), location.getLongitude(), distance);
        		Log.d("wang","distance is " + distance[0]);
        		if(distance[0] <= 200.0){
        			toast("200 distance is bigger " + Math.round(distance[0]));
        			tts.speak("距离标记位置" + i + "还有" + Math.round(distance[0]) + "米", TextToSpeech.QUEUE_ADD, null);
        		} else {
        			//toast("200 distance is smaller " + distance[0]);
        		}
        	}
    	}
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }
}

   @SuppressLint("NewApi")
private void setCustomerInfo() {
	   SharedPreferences share = getSharedPreferences("customer", MODE_PRIVATE);
	   String user = share.getString("user", "");
	   if(!user.isEmpty()) {
		   String username = share.getString("user", "");
		   app.setUser(username);
		   String sign = share.getString("sign", "");
		   app.setSign(sign);
		   String customerId = share.getString("customerId", "");
		   app.setCustomerid(Integer.valueOf(customerId));
		   String qq = share.getString("qq", "");
		   app.setQQ(qq);
		   String email = share.getString("email", "");
		   app.setEmail(email);
	   }
   }
/*	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {  
            Uri uri = data.getData();  
            Log.e("wang", uri.toString());  
            ContentResolver cr = this.getContentResolver();  
            try {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                 
                image.setImageBitmap(bitmap);  
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  
*/
    public void showMark(){
		OverlayOptions overlayOptions = null;
                Marker marker = null;
		LatLng latLng = null;
		 latLng = new LatLng(34.242652, 108.971171);
                        // 鍥炬爣
                        overlayOptions = new MarkerOptions().position(latLng)
                                        .icon(mIconMaker).zIndex(5);
                        marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
                        //Bundle bundle = new Bundle();
                        //bundle.putSerializable("info", info);
                        //marker.setExtraInfo(bundle);
    }	
private void initClickMap(){
			mBaiduMap.setOnMapClickListener(new OnMapClickListener(){

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub
					 if(flag){
						 mBaiduMap.hideInfoWindow();
						 flag = false;
					 }
					 addCameraOverlay(arg0);
				}

				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					// TODO Auto-generated method stub
					return false;
				}
				
			});
}
private void addCameraOverlay(LatLng arg0) {
	    if(tag == 1) {
                        showInfoWindow(arg0);
                        OverlayOptions overlayOptions = null;
                         overlayOptions = new MarkerOptions().position(arg0)
                                    .icon(mIconMaker).zIndex(5);
                        cameraOverlay = mBaiduMap.addOverlay(overlayOptions);
			cameraLocation = arg0;
                        tag = 0;
        } else if(tag_location == 1){
        	Intent intent = new Intent();
        	intent.setClass(MainActivity.this, PanoramaMView.class);
        	startActivity(intent);
        	
 /*           OverlayOptions overlayOptions = null;
            overlayOptions = new MarkerOptions().position(arg0)
                       .icon(mIconMaker).zIndex(5);
           locationOverlay = mBaiduMap.addOverlay(overlayOptions);
           overlayArray.add(locationOverlay);
           locationMark = arg0;
           latArray.add(locationMark);
           tag_location = 0;*/
        }

}
private void uploadCameraOverlay() {
	HashMap<String, String> locationParams = new HashMap<String, String>();
        locationParams.put("longitude", String.valueOf(cameraLocation.longitude));
        locationParams.put("latitude", String.valueOf(cameraLocation.latitude));
        locationParams.put("name", this.et_desc);
        locationParams.put("address", this.et_addr);
        locationParams.put("direction", this.direction);
        locationParams.put("type", this.camera_typ);
        locationParams.put("zan", "0");
        locationParams.put("buzan", "0");
        doTaskAsync(C.task.createCamera, C.api.createCamera, locationParams);
	Log.d("wang","uploadCamera info successfully!!!");
}
public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.safeRoadCountById:
			AllRoadCount roadCount;
		try {
			roadCount = (AllRoadCount) message.getResult("AllRoadCount");
			newsCount = roadCount.getCount();	
			news.setText(newsCount);	
			progressDialog.dismiss();
			Toast.makeText(MainActivity.this, "您有 " + newsCount + " 条新评论，请点击个人信息查看", Toast.LENGTH_LONG).show();
			//toast("您有 " + newsCount + " 条新评论，请点击个人信息查看");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			break;
		case C.task.createCamera:
			toast("create camera succefully");
		Camera newCamera;
		try {
			newCamera = (Camera) message.getResult("Camera");
			cameraList.add(newCamera);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			break;
		case C.task.getCamera:
			Log.d("wang","entry getCamera");
		try {
			cameraList = (ArrayList<Camera>) message.getResultList("Camera");
			Log.d("wang","camera list size is " + cameraList.size());
			OverlayOptions overlayOptions = null;
			Marker marker = null;
			LatLng latLng = null;
			for(Camera data : cameraList){
			    Log.d("wang","camera addr = " + data.getAddress() + "name = " + data.getName() + "direction = " + data.getDirection());
				latLng = new LatLng(Double.valueOf(data.getLatitude()),Double.valueOf(data.getLongitude()));
				overlayOptions = new MarkerOptions().position(latLng)
                         .icon(mIconMaker).zIndex(5);
                marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
				//Log.d("wang", "latitude = " + data.getLatitude());
				//Log.d("wang", "longitude = " + data.getLongitude());
				//Log.d("wang", "id = " + data.getId());
				
			}
			toast("get camera successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
		case C.task.zan:
			if(message.getCode().equals("10000")) {
				SharedPreferences share = getSharedPreferences("zan", MODE_PRIVATE);
				toast(message.getMessage());
				mCurrentZan = mCurrentZan + 1;
				mZanCount.setText(String.valueOf(mCurrentZan));
				SharedPreferences.Editor edit = share.edit();
				edit.putInt(mCurrentCamera.getId(), 1);
				edit.commit();
			}
			break;
		case C.task.buzan:
			if(message.getCode().equals("10000")) {
				SharedPreferences share = getSharedPreferences("buzan", MODE_PRIVATE);
				toast(message.getMessage());
				mCurrentBuZan = mCurrentBuZan + 1;
				mBuZanCount.setText(String.valueOf(mCurrentBuZan));
				SharedPreferences.Editor edit = share.edit();
				edit.putInt(mCurrentCamera.getId(), 1);
				edit.commit();
			}
			break;
		case C.task.getCameraById:
			Camera camera;
		try {
			camera = (Camera) message.getResult("Camera");
			 mCurrentCamera = camera;
			 mCurrentZan = Integer.valueOf(mCurrentCamera.getZan());
			 mZanCount.setText(String.valueOf(mCurrentZan));
			 mCurrentBuZan = Integer.valueOf(mCurrentCamera.getBuzan());
			 mBuZanCount.setText(String.valueOf(mCurrentBuZan));
			 mBaiduMap.showInfoWindow(mInfoWindow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			break;
	}
}
private void showInfoWindow(LatLng ll) {

             InfoWindow mInfoWindow;
             View location = getAddCameraView();
             //final LatLng ll = marker.getPosition();
             Point p = mBaiduMap.getProjection().toScreenLocation(ll);
             Log.d("wang","haha x = " + p.x + "y = " + p.y);
             LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
             //LatLng llInfo = new LatLng(34.242652, 108.971171);
             Log.d("wang","latitude = " + llInfo.latitude + " longitude" + llInfo.longitude);
             p.y = -100;
             mInfoWindow = new InfoWindow(location, llInfo,p.y);
             mBaiduMap.showInfoWindow(mInfoWindow);
}
 private void initMarkerClickEvent() {
               
                mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener()
                {
                        @Override
                        public boolean onMarkerClick(final Marker marker)
                        {
                     
                     LatLng markerLat = marker.getPosition();
                     judgeClickCamera(markerLat);
                     Point p = mBaiduMap.getProjection().toScreenLocation(markerLat);
             			//mMarkerInfoLy.setVisibility(View.VISIBLE);
                     View location = getPopCameraView();
                     p.y = -100;
                     mInfoWindow = new InfoWindow(location, markerLat,p.y);
                     
             			flag = true;
                     return true;
                        }
                });
        }
 private void judgeClickCamera(LatLng location){
	 for(Camera data : cameraList){
		 if((location.latitude == Double.valueOf(data.getLatitude())) && (location.longitude == Double.valueOf(data.getLongitude()))) {
			
			 HashMap<String, String> locationParams = new HashMap<String, String>();				      
				locationParams.put("cameraId", data.getId());
				doTaskAsync(C.task.getCameraById,C.api.getCameraById,locationParams);
				break;
		 }
	 }
 }
 public View getPopCameraView(){
		View v = View.inflate(getApplicationContext(),R.layout.pop_for_camera , null);
		Button zan = (Button) v.findViewById(R.id.zan);
		Button buzan = (Button) v.findViewById(R.id.buzan);
		TextView comment = (TextView) v.findViewById(R.id.comment_camera);
	
		mZanCount = (TextView) v.findViewById(R.id.zan_count);
		mBuZanCount = (TextView) v.findViewById(R.id.buzan_count);
		final SharedPreferences share = getSharedPreferences("zan", MODE_PRIVATE);
		final SharedPreferences share_no = getSharedPreferences("buzan", MODE_PRIVATE);
		
		zan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				share_zan = share.getInt(mCurrentCamera.getId(), 0);
				share_buzan = share_no.getInt(mCurrentCamera.getId(), 0);
				Log.d("wang","zan button, zan " + share_zan + " share_buzan " + share_buzan);
				if((share_zan == 0) && (share_buzan == 0)) {
					HashMap<String, String> locationParams = new HashMap<String, String>();				      
					locationParams.put("cameraId", mCurrentCamera.getId());
					doTaskAsync(C.task.zan,C.api.zan,locationParams);
				}
				//mZanCount.setText(Integer.valueOf(mCurrentCamera.getZan()) + 1);
			}
		});
		buzan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				share_zan = share.getInt(mCurrentCamera.getId(), 0);
				share_buzan = share_no.getInt(mCurrentCamera.getId(), 0);
				Log.d("wang","buzan button, zan " + share_zan + " share_buzan " + share_buzan);
				if((share_buzan == 0) && (share_zan == 0)) {
				   HashMap<String, String> locationParams = new HashMap<String, String>();				      
					locationParams.put("cameraId", mCurrentCamera.getId());
					doTaskAsync(C.task.buzan,C.api.buzan,locationParams);
				}
			}
		});
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.this.app.setCameraName(mCurrentCamera.getName());
				MainActivity.this.app.setCameraUptime(mCurrentCamera.getUptime());
				MainActivity.this.app.setCameraAddress(mCurrentCamera.getAddress());
				MainActivity.this.app.setCameraDirecton(mCurrentCamera.getDirection());
				MainActivity.this.app.setCameraId(mCurrentCamera.getId());
				Intent intent = new Intent();
            	intent.setClass(MainActivity.this, CommentActivity.class);
            	startActivity(intent);
		mBaiduMap.hideInfoWindow();
			}
		});
		return v;
 }
public View getAddCameraView(){
	View v = View.inflate(getApplicationContext(),R.layout.tpl_list_blogs , null);
	Button bt = (Button) v.findViewById(R.id.ok);
        Button cancel = (Button) v.findViewById(R.id.cancel);
	cancel.setOnClickListener(new OnClickListener() {
		@Override
       public void onClick(View arg0) {
                        // TODO Auto-generated method stub
			cameraOverlay.remove();
			mBaiduMap.hideInfoWindow();
		}
	});
	bt.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
                	intent.setClass(MainActivity.this, CameraInfo.class);
                	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	startActivityForResult(intent,1);
		}
		
	});
	return v;
}
public View getSureLocationView(){
	View v = View.inflate(getApplicationContext(),R.layout.sure_location , null);
	Button bt = (Button) v.findViewById(R.id.ok);
        Button cancel = (Button) v.findViewById(R.id.cancel);
	cancel.setOnClickListener(new OnClickListener() {
		@Override
       public void onClick(View arg0) {
                        // TODO Auto-generated method stub
			locationOverlay.remove();
			mBaiduMap.hideInfoWindow();
			locationMark = null;
		}
	});
	bt.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		}
		
	});
	return v;
}

@SuppressLint("NewApi")
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if(data != null) {
		String result = data.getExtras().getString("result");
		if(!result.isEmpty()){
                if(result.equals("ok1")) {
                                Log.d("wang","MainActivity result is ok1");
                                int flag;
                                flag = app.getAllCount();
                                if(flag == 0) {
                                        news.setVisibility(View.GONE);
                                } else {
                                		  newsCount = String.valueOf(flag);
                                        news.setText(String.valueOf(flag));
                                }
                } else {
			if(result.equals("ok")) {
				et_desc = data.getExtras().getString("et_desc");
				if(et_desc.isEmpty()){
					et_desc = "拍外地摄像头";
				}
				et_addr = data.getExtras().getString("et_addr");
				if(et_addr.isEmpty()){
					et_addr = "该用户没有留下地址";
				}
				camera_typ = data.getExtras().getString("camera_typ");
				if(camera_typ.isEmpty()){
					camera_typ = "0";
				}
				direction = data.getExtras().getString("direction");
				if(direction.isEmpty()){
					direction = " ";
				}
				Log.d("wang","upload camera info:et_desc = " + et_desc + " et_addr = " + et_addr + " camera_typ = " + camera_typ + " direction = " + direction);
		                mBaiduMap.hideInfoWindow();	
		        	uploadCameraOverlay();
			} else {
				cameraOverlay.remove();
		                mBaiduMap.hideInfoWindow();		
			}
		}
		}
	}
}
   public class Mylistern implements InfoWindow.OnInfoWindowClickListener {
	   public void onInfoWindowClick()
             {
                     // 闅愯棌InfoWindow
                     mBaiduMap.hideInfoWindow();
             }
 
   }
    @Override 
    protected void onDestroy() {
       
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        super.onDestroy();
    }  
    @Override 
    protected void onResume() {
        super.onResume();
        mMapView.onResume(); 
        } 
    @Override 
    protected void onPause() {
        super.onPause(); 
        mMapView.onPause();
        }
 }

