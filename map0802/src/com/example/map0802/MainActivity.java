package com.example.map0802;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.shareData.CustomerInfo;


import com.example.R;
import com.example.base.BaseMessage;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Overlay;

import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.CameraComment;
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
  private TextView profile;
  private TextView tv;
  private Overlay cameraOverlay;
  private LatLng cameraLocation;
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
  int tag=0;
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
	profile = (TextView) findViewById(R.id.profile);
	safeRoad = (TextView) findViewById(R.id.camera_topic);
	safeRoad.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                intent.setClass(MainActivity.this, SafeRoadActivity.class);
                startActivity(intent);
                }
        });

	profile.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
        	intent.setClass(MainActivity.this, ProfileActivity.class);
        	startActivity(intent);
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
   	doTaskAsync(C.task.getCamera, C.api.getCamera);
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

@SuppressLint("NewApi")
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if(data != null) {
		String result = data.getExtras().getString("result");
		if(!result.isEmpty()){
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
   public class Mylistern implements InfoWindow.OnInfoWindowClickListener {
	   public void onInfoWindowClick()
             {
                     // 闅愯棌InfoWindow
                     mBaiduMap.hideInfoWindow();
             }
 
   }
    @Override 
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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

