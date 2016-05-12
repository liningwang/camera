package com.example.map0802;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
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
import com.example.model.GongGao;
import com.example.model.UpdateApk;
import com.jauker.widget.BadgeView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;


public class MainActivity extends BaseUi {
    MapView mMapView = null;

   private BaiduMap mBaiduMap;

   private BitmapDescriptor mIconMaker; 
   private BitmapDescriptor mPanoramaMaker; 
   private BitmapDescriptor mbiaojiMaker; 
  private RelativeLayout mMarkerInfoLy;
  private boolean flag = false;
  private ImageView image;
  private ImageView weizhang_imag;
  private LinearLayout addMark;
  private LinearLayout profile;
  private LinearLayout nave;
  private TextView tv;
  private Overlay cameraOverlay;
  private Overlay locationOverlay;
  private Overlay panoramaOverlay;
  private LatLng cameraLocation;
  private LatLng locationMark;
  private LatLng panoramaLocation;
  private String et_desc;
  private String title;
  private String content;
  private TextView tv_content;
  private TextView tv_title;
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
  private String newsCount = "";
  private LocationClient mLocClient;
  private ProgressDialog progressDialog = null;
  private MyLocationListenner myListener;
  private boolean isFirstLoc = true;
  private LocationMode mCurrentMode;
  private BitmapDescriptor mCurrentMarker;
  private LinearLayout addLocation;
  private LinearLayout removeLocation;
  private LinearLayout panorama;
  private View layout;
  private View layout_welcom;
  private Dialog dialog_welcom;
  AlertDialog.Builder build;
  private AlertDialog dialog;
  private LinearLayout global;
  private ImageView panorama_imag;
  private ImageView tianjia;
  private int tag_location = 0;
  private int panorama_location = 0;
  private ArrayList<LatLng> latArray = new ArrayList<LatLng>();
  private ArrayList<Overlay> overlayArray = new ArrayList<Overlay>();
  private int tag=0;
  private double lati; 
  private double longi; 
  private TextToSpeech tts;
  private int m_newVerCode;
  private String m_newVerName;
  private String m_appNameStr;
  private Handler m_mainHandler;
  private ProgressDialog m_progressDlg;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.bmapView); 
	mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
       
	mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.jinjingmarker);
	mPanoramaMaker = BitmapDescriptorFactory.fromResource(R.drawable.quanjing_biao);
	mbiaojiMaker = BitmapDescriptorFactory.fromResource(R.drawable.biaoji);
	mBaiduMap = mMapView.getMap();
	image = (ImageView) mMarkerInfoLy.findViewById(R.id.info_img);
	tv = (TextView) mMarkerInfoLy.findViewById(R.id.info_name);
	addLocation = (LinearLayout) findViewById(R.id.add_location);
	tianjia	= (ImageView) addLocation.findViewById(R.id.tianjia);
	removeLocation = (LinearLayout) findViewById(R.id.remove_location);
	global = (LinearLayout) findViewById(R.id.global);
	addMark=(LinearLayout) global.findViewById(R.id.add_camera);
	weizhang_imag = (ImageView) addMark.findViewById(R.id.weizhang_image);
	panorama = (LinearLayout) global.findViewById(R.id.panorama_location);
	panorama_imag = (ImageView) panorama.findViewById(R.id.panorama_image);
	profile = (LinearLayout) findViewById(R.id.profile);
	nave = (LinearLayout) findViewById(R.id.nave);
	safeRoad = (TextView) findViewById(R.id.camera_topic);
	about = (TextView) findViewById(R.id.about);
	layout = View.inflate(MainActivity.this, R.layout.pop_dialog, null);
	layout_welcom = View.inflate(MainActivity.this, R.layout.welcom, null);
	TextView cancel = (TextView) layout.findViewById(R.id.cancel_button);
	cancel.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View arg0) {
                // TODO Auto-generated method stub
        	dialog.dismiss();
        }
});
    	badgeView.setTargetView(nave);
	badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);

	
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
	initVariable();
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
		badgeView.setBadgeCount(0);
		badgeView.setVisibility(View.GONE);
	}	
	doTaskAsync(C.task.update, C.api.update);
   	doTaskAsync(C.task.gongGao, C.api.gongGao);
   	doTaskAsync(C.task.getCamera, C.api.getCamera);
   	initLocation();
   	initTTS();
   	//showDialog();
	showWelcom();
   //	initPanorama();
    } 
    private void showWelcom()
    {
	dialog_welcom = new Dialog(this,R.style.Dialog_Fullscreen);  
   	dialog_welcom.setContentView(R.layout.welcom);  
	/*Window window = dialog_welcom.getWindow(); 
	WindowManager.LayoutParams lp = window.getAttributes();
	lp.alpha = 0.9f;
	window.setAttributes(lp);
	window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);*/
	TextView entry = (TextView) dialog_welcom.findViewById(R.id.entry_map);
	tv_title = (TextView) dialog_welcom.findViewById(R.id.title);
	tv_content = (TextView) dialog_welcom.findViewById(R.id.content);
	entry.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog_welcom.dismiss();
		showHelp();
        }
	});
	dialog_welcom.setCancelable(false);
   	dialog_welcom.show();

    }
    private void showCusPopUp()
    {
	View popupView;
	PopupWindow window = null;
	TextView cusPopupBtn1 = null;
	
        if(window == null)
        {
        //    popupView=LayoutInflater.from(MainActivity.this).inflate(R.layout.popup, null);
           // cusPopupBtn1=(TextView)popupView.findViewById(R.id.pop_button);
          //  window =new PopupWindow(popupView,LayoutParams.FILL_PARENT,300);
        }
        //window.setAnimationStyle(R.style.AnimBottom);
        window.setFocusable(true);
	 ColorDrawable color = new ColorDrawable(-00000);
 	window.setBackgroundDrawable(color);
        //window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
	    //设置背景颜色变暗
            //  WindowManager.LayoutParams lp=getWindow().getAttributes();
            //    lp.alpha=0.3f;
             // getWindow().setAttributes(lp);
        window.showAtLocation(mMapView, Gravity.CENTER_VERTICAL, 0, 0);
        cusPopupBtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
		toast("click ok!");
            }
        });
    }

   
    private void setOnclickListener() {
		// TODO Auto-generated method stub
    	
    	
    	
		addMark.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(tag == 0) {
					weizhang_imag.setBackgroundResource(R.drawable.weizhangdi);
					tag = 1;
				} else if(tag == 1) {
					weizhang_imag.setBackgroundResource(R.drawable.weizhangdi1);
					tag = 0;
				}
				}
		});
		addLocation.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(tag_location == 0) {
					tianjia.setBackgroundResource(R.drawable.tianjia);
					tag_location = 1;
				} else if(tag_location == 1) {
					tianjia.setBackgroundResource(R.drawable.notianjia);
					tag_location = 0;
				}
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
		panorama.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if(panorama_location == 0) {
						panorama_imag.setBackgroundResource(R.drawable.quanjing1);
						panorama_location = 1;
					} else if(panorama_location == 1) {
						panorama_imag.setBackgroundResource(R.drawable.quanjing);
						if(panoramaOverlay != null) {
							panoramaOverlay.remove();	
							mBaiduMap.hideInfoWindow();
						}
						panorama_location = 0;
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
    option.setScanSpan(20000);
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
        		if(distance[0] <= 2000.0){
        			toast("2000 distance is bigger " + Math.round(distance[0]));
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
			Log.d("wang","addCameraOverlay cameraLocation latitude " + cameraLocation.latitude + " longitude " + cameraLocation.longitude);
        } else if(tag_location == 1){
            OverlayOptions overlayOptions = null;
            overlayOptions = new MarkerOptions().position(arg0)
                       .icon(mbiaojiMaker).zIndex(5);
           locationOverlay = mBaiduMap.addOverlay(overlayOptions);
           overlayArray.add(locationOverlay);
           locationMark = arg0;
           latArray.add(locationMark);
	   tianjia.setBackgroundResource(R.drawable.notianjia);
           tag_location = 0;
        } else if(panorama_location == 1) {
                        //showInfoWindow(arg0);
			if(panoramaOverlay != null ) {
				panoramaOverlay.remove();	
				mBaiduMap.hideInfoWindow();
			}
			showInfoWindowForPanorama(arg0);
                        OverlayOptions overlayOptions = null;
                         overlayOptions = new MarkerOptions().position(arg0)
                                    .icon(mPanoramaMaker).zIndex(5);
                        panoramaOverlay = mBaiduMap.addOverlay(overlayOptions);
			panoramaLocation = arg0;
                        //panorama_location = 0;
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
	int customerId = app.getCustomerid();
	if(customerId != 0) {
        	locationParams.put("username", app.getUser());
	} else {
		locationParams.put("username", "游客");
	}
        doTaskAsync(C.task.createCamera, C.api.createCamera, locationParams);
	Log.d("wang","uploadCamera info successfully!!!");
}
public void onNetworkError (int taskId) {
	super.onNetworkError(taskId);
	switch(taskId){
		case C.task.safeRoadCountById:
			progressDialog.dismiss();
	}
}
        private void initVariable()
        {
                m_mainHandler = new Handler();
                m_progressDlg =  new ProgressDialog(this);
		m_progressDlg.setCancelable(true);
		m_progressDlg.setCanceledOnTouchOutside(false);
                m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                m_progressDlg.setIndeterminate(false);
                m_appNameStr = "haha.apk";
        }
       private void mustUpdate() {
		     int verCode = Common.getVerCode(getApplicationContext());
            String verName = Common.getVerName(getApplicationContext());

            String str= "version name "+verName+" Code:"+verCode+" ,new version name"+m_newVerName+
                        " Code:"+m_newVerCode+" ,is ready";
            Dialog dialog = new AlertDialog.Builder(this).setTitle("update apk").setMessage(str)

                    .setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    m_progressDlg.setTitle("update apk");
                                    m_progressDlg.setMessage("download..");
                                    downFile(C.api.apkUrl);
                                }
                            }).create();
	dialog.setCancelable(false);
            dialog.show();

       }
       private void doNewVersionUpdate() {
                int verCode = Common.getVerCode(getApplicationContext());
            String verName = Common.getVerName(getApplicationContext());

            String str= "version name "+verName+" Code:"+verCode+" ,new version name"+m_newVerName+
                        " Code:"+m_newVerCode+" ,is ready";
            Dialog dialog = new AlertDialog.Builder(this).setTitle("update apk").setMessage(str)
	

                    .setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    m_progressDlg.setTitle("update apk");
                                    m_progressDlg.setMessage("download..");
                                    downFile(C.api.apkUrl);
                                }
                            })
                    .setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {

                                    //finish();
                                }
                            }).create();
	dialog.setCancelable(false);
            dialog.show();
        }

               private void notNewVersionDlgShow()
                {
                        int verCode = Common.getVerCode(this);
                    String verName = Common.getVerName(this);
                    String str="verName:"+verName+" Code:"+verCode+",/n no new version for apk";
                    Dialog dialog = new AlertDialog.Builder(this).setTitle("can not find new version")
                            .setMessage(str)
                            .setPositiveButton("ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                int which) {
                                            finish();
                                        }
                                    }).create();
                    dialog.show();
                }

                private void downFile(final String url)
                {
                        m_progressDlg.show();
                    new Thread() {
                        public void run() {
                            HttpClient client = new DefaultHttpClient();
                            HttpGet get = new HttpGet(url);
                            HttpResponse response;
                            try {
                                response = client.execute(get);
                                HttpEntity entity = response.getEntity();
                                long length = entity.getContentLength();

                                m_progressDlg.setMax((int)length);

                                InputStream is = entity.getContent();
                                FileOutputStream fileOutputStream = null;
                                if (is != null) {
                                    File file = new File(
                                            Environment.getExternalStorageDirectory(),
                                            m_appNameStr);
                                    fileOutputStream = new FileOutputStream(file);
                                    byte[] buf = new byte[1024];
                                    int ch = -1;
                                    int count = 0;
                                    while ((ch = is.read(buf)) != -1) {
                                        fileOutputStream.write(buf, 0, ch);
                                        count += ch;
                                        if (length > 0) {
                                                 m_progressDlg.setProgress(count);
                                        }
                                    }
                                }
                                fileOutputStream.flush();
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                down();
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

      private void down() {
                m_mainHandler.post(new Runnable() {
                    public void run() {
                        m_progressDlg.cancel();
                        update();
                    }
                });
        }
        void update() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), m_appNameStr)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }

public void onTaskComplete(int taskId, BaseMessage message) {
	super.onTaskComplete(taskId, message);
	switch(taskId){
		case C.task.safeRoadCountById:
			AllRoadCount roadCount;
		try {
			if(message.getCode().equals("10000")) {
				roadCount = (AllRoadCount) message.getResult("AllRoadCount");
				newsCount = roadCount.getCount();	
				badgeView.setBadgeCount(Integer.valueOf(newsCount));
				progressDialog.dismiss();
				Toast.makeText(MainActivity.this, "您有 " + newsCount + " 条新评论，请点击个人信息查看", Toast.LENGTH_LONG).show();
				//toast("您有 " + newsCount + " 条新评论，请点击个人信息查看");
			} else {
				progressDialog.dismiss();
				toast(message.getMessage());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			break;
		case C.task.createCamera:
			toast("create camera succefully");
	 	Log.d("wang","cameraList size is " + cameraList.size());
		Camera newCamera;
		try {
			
			newCamera = (Camera) message.getResult("Camera");
			newCamera.setLongitude(String.valueOf(cameraLocation.longitude));
			newCamera.setLatitude(String.valueOf(cameraLocation.latitude));
			cameraList.add(newCamera);
			weizhang_imag.setBackgroundResource(R.drawable.weizhangdi1);
                        tag = 0;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			break;
		case C.task.gongGao:
		GongGao gong;
		try {
			
			gong = (GongGao) message.getResult("GongGao");
			title = gong.getTitle();
			content = gong.getContent();
			tv_title.setText(title);
			tv_content.setText(content);
			Log.d("wang","title is " + title + ", content is " + content);
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
				if(data.getType().equals("0")) {
                    mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.jinjingmarker);
				} else if(data.getType().equals("1")) {
	                     mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.weihaomarker);
			         }else if (data.getType().equals("2")) {
	                     mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.gaofengmarker);
			         }
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
		case C.task.update:
                        UpdateApk updateA;
                        try {
                                updateA = (UpdateApk) message.getResult("UpdateApk");
                                m_newVerName = updateA.getVerName();
                                m_newVerCode = Integer.valueOf(updateA.getVerCode());
				int flag = Integer.valueOf(updateA.getFlag());
                                int vercode = Common.getVerCode(getApplicationContext());
                                Log.d("wang","updateA id " + updateA.getId() + " verCode is " + updateA.getVerCode() + " verName is " + updateA.getVerName() + "local vercode is " + vercode);
                         if ((m_newVerCode > vercode) && (flag == 0)) {
					/*IntentFilter intentFilter = new IntentFilter();
                			intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                			intentFilter.addDataScheme("package");
                			registerReceiver(broadcastReceiver, intentFilter);
					*/
                                        doNewVersionUpdate();
                         } else if((m_newVerCode > vercode) && (flag == 1)){
                                //        notNewVersionDlgShow();
					/*IntentFilter intentFilter = new IntentFilter();
                			intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                			intentFilter.addDataScheme("package");
                			registerReceiver(broadcastReceiver, intentFilter);
					*/

				mustUpdate();
                         }
                        } catch (Exception e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                        }
                        
                break;
	}
}
private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
		toast("install finished");
		Log.d("wang","install finished!");
		openApk(MainActivity.this,new File(Environment
                        .getExternalStorageDirectory(), m_appNameStr).toString());	
                }
        };

       private void openApk(Context context, String url) {
		Log.d("wang","openApk url is " + url);
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageArchiveInfo(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + getFilePath(url), PackageManager.GET_ACTIVITIES);
                if (info != null) {
                        Intent intent = manager.getLaunchIntentForPackage(info.applicationInfo.packageName);
                        startActivity(intent);
                }
        }
        private File getFile(String url) {
                File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getFilePath(url));
                return files;
        }

        private String getFilePath(String url) {
                return url.substring(url.lastIndexOf("/"), url.length());
        }


private void showHelp() {
            SharedPreferences share = getSharedPreferences("customer", MODE_PRIVATE);
           int open = share.getInt("open", 0);
           if(open == 0) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {
					showDialog();
				}}, 4000);
			
			SharedPreferences.Editor edit = share.edit();
			edit.putInt("open", 1);
			edit.commit();
           }

}
@SuppressLint("NewApi") private void showDialog() {
	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	dialog = builder.create();
	
	//Window window = dialog.getWindow(); 
	//dialog.setView(layout,4,4,4,4);

//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//window.setBackgroundDrawableResource(android.R.color.transparent);
	/*WindowManager.LayoutParams lp = window.getAttributes();
	lp.alpha = 0.5f;
	window.setAttributes(lp);*/
	dialog.show();
	dialog.getWindow().setContentView(layout);
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
             p.y = -83;
             mInfoWindow = new InfoWindow(location, llInfo,p.y);
             mBaiduMap.showInfoWindow(mInfoWindow);
}
private void showInfoWindowForPanorama(LatLng ll) {

             InfoWindow mInfoWindow;
             //View location = getAddCameraView();
	     View location = getPanoramaView();
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
                     Point p = mBaiduMap.getProjection().toScreenLocation(markerLat);
             			//mMarkerInfoLy.setVisibility(View.VISIBLE);
                     View location = getPopCameraView();
                     p.y = -83;
                     mInfoWindow = new InfoWindow(location, markerLat,p.y);
		     mBaiduMap.showInfoWindow(mInfoWindow);
                     judgeClickCamera(markerLat);
             			flag = true;
                     return true;
                        }
                });
        }
 private void judgeClickCamera(LatLng location){
	 Log.d("wang","cameraList judgeClickCamera size is " + cameraList.size());
	 for(Camera data : cameraList){
		 if((location.latitude == Double.valueOf(data.getLatitude())) && (location.longitude == Double.valueOf(data.getLongitude()))) {
			
	 		Log.d("wang","judgeClickCamera find camera " + data.getId());
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
			intent.putExtra("username",mCurrentCamera.getUsername());
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
public View getPanoramaView(){
        View v = View.inflate(getApplicationContext(),R.layout.panorama_layout , null);
        LinearLayout bt = (LinearLayout) v.findViewById(R.id.bt_panorama);
        bt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                        // TODO Auto-generated method stub
        		Intent intent = new Intent();
			intent.putExtra("latitude",panoramaLocation.latitude);
			intent.putExtra("longitude",panoramaLocation.longitude);
        		intent.setClass(MainActivity.this, PanoramaMView.class);
                	startActivityForResult(intent,1);
                }

        });
        return v;
}

@SuppressLint("NewApi")
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	Log.d("wang","mainactivity onActivityResult data is " + data);
	if(data != null) {
		String result = data.getExtras().getString("result");
		if(!result.isEmpty()){
                	if(result.equals("ok1")) {
                                Log.d("wang","MainActivity result is ok1");
                                int flag;
                                flag = app.getAllCount();
                                if(flag == 0) {
                			badgeView.setVisibility(View.GONE);
                                	newsCount = String.valueOf(flag);
                                } else {
                                	newsCount = String.valueOf(flag);
					badgeView.setBadgeCount(flag);
                                }
                } else if(result.equals("panorama")) {
                		lati = data.getExtras().getDouble("latitude");
                		longi = data.getExtras().getDouble("longitude");
				Log.d("wang","mainActivity panorama lati is " + lati + ", longi is " + longi);
				LatLng latlng = new LatLng(lati, longi);

				panoramaOverlay.remove();	
				mBaiduMap.hideInfoWindow();

				panoramaLocation = latlng;
 				showInfoWindowForPanorama(latlng);
        	                OverlayOptions overlayOptions = null;
                	        overlayOptions = new MarkerOptions().position(latlng)
                                    .icon(mPanoramaMaker).zIndex(5);
                       		panoramaOverlay = mBaiduMap.addOverlay(overlayOptions);
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
		         if(camera_typ.equals("1")) {
                     BitmapDescriptor iconMaker = BitmapDescriptorFactory.fromResource(R.drawable.weihaomarker);
                     OverlayOptions overlayOptions = null;
                      overlayOptions = new MarkerOptions().position(cameraLocation)
                                 .icon(iconMaker).zIndex(5);
                     cameraOverlay = mBaiduMap.addOverlay(overlayOptions);
             
		         }else if (camera_typ.equals("2")) {
                     BitmapDescriptor iconMaker = BitmapDescriptorFactory.fromResource(R.drawable.gaofengmarker);
                     OverlayOptions overlayOptions = null;
                      overlayOptions = new MarkerOptions().position(cameraLocation)
                                 .icon(iconMaker).zIndex(5);
                     cameraOverlay = mBaiduMap.addOverlay(overlayOptions);
		         }
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

