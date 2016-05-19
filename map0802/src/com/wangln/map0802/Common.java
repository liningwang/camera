package com.wangln.map0802;
/**
 * @author harvic
 * @date 2014-5-7
 * @address http://blog.csdn.net/harvic880925
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Common {

	public static int getVerCode(Context context) {
        int verCode = -1;
        try {
        	
            verCode = context.getPackageManager().getPackageInfo(
                    "com.wangln", 0).versionCode;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.wangln", 0).versionName;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verName;   
}
	
	
	
	
	
	
}
