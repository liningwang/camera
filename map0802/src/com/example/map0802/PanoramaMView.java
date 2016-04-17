package com.example.map0802;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.baidu.lbsapi.tools.CoordinateConverter;
import com.baidu.lbsapi.tools.Point;
import com.example.R;
import com.example.base.BaseUi;
import com.example.shareData.CustomerInfo;
import com.example.shareData.CustomerInfo.MyGeneralListener;
import com.example.util.SDUtil;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import com.baidu.lbsapi.BMapManager;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class PanoramaMView extends Activity
{
	private Point point;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initBMapManager();
		setContentView(R.layout.panorama);
		
		//private void initPanorama(){
			PanoramaView mPanoView = (PanoramaView)findViewById(R.id.panorama);
			mPanoView.setShowTopoLink(true);
			//mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
			 
			//设置全景图片的显示级别
			//根据枚举类ImageDefinition来设置清晰级别
			//较低清晰度 ImageDefinationLow
			//中等清晰度 ImageDefinationMiddle
			//较高清晰度 ImageDefinationHigh
			mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionMiddle);
			//mPanoView. setPanoramaImageLevel(ImageDefinationMiddle);
            double lat = 39.945;
            double lon = 116.404;
            mPanoView.setPanoramaViewListener(new PanoramaViewListener() {

				@Override
				public void onLoadPanoramaBegin() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLoadPanoramaEnd(String arg0) {
					// TODO Auto-generated method stub
					Log.d("wang"," string is " + arg0);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(arg0);
						Integer x = (Integer) jsonObject.get("X");
						Integer y = (Integer) jsonObject.get("Y");
						Log.d("wang","x is " + x.intValue() + " y is " + y.intValue());
						point = CoordinateConverter.MCConverter2LL(x, y);
						Log.d("wang","latitude is " + point.x + " longitude is " + point.y);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
					
					//CoordinateConverter.MCConverter2LL(x, y);
					//toast(arg0);
				}

				@Override
				public void onLoadPanoramaError(String arg0) {
					// TODO Auto-generated method stub
					
				}
            	
            });
            mPanoView.setPanorama(lon, lat);
		//}
	}

    private void initBMapManager() {
        CustomerInfo app = (CustomerInfo) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);
            app.mBMapManager.init(new CustomerInfo.MyGeneralListener());
        }
    }

}
