package com.example.map0802;
/**
 * @author harvic
 * @date 2014-5-7
 * @address http://blog.csdn.net/harvic880925
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.example.R;
import com.example.base.BaseMessage;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.model.Camera;
import com.example.model.UpdateApk;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UpdateApkActivity extends BaseUi {

	Button m_btnCheckNewestVersion;
	int m_newVerCode; 
	String m_newVerName; 
	String m_appNameStr; 
	
	Handler m_mainHandler;
	ProgressDialog m_progressDlg;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_apk);
		
		
		initVariable();
		
		m_btnCheckNewestVersion.setOnClickListener(btnClickListener);
	}
	private void initVariable()
	{
		m_btnCheckNewestVersion = (Button)findViewById(R.id.chek_newest_version);
		m_mainHandler = new Handler();
		m_progressDlg =  new ProgressDialog(this);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		     
		m_progressDlg.setIndeterminate(false);    
		m_appNameStr = "haha.apk";
	}
	
	OnClickListener btnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doTaskAsync(C.task.update, C.api.update);
			//new checkNewestVersionAsyncTask().execute();
		}
	};
	
	private void doNewVersionUpdate() {
		int verCode = Common.getVerCode(getApplicationContext());  
	    String verName = Common.getVerName(getApplicationContext());  
	    
	    String str= "当前版本"+verName+" Code:"+verCode+" ,发现新版本:"+m_newVerName+
	    		" Code:"+m_newVerCode+" ,是否更新？";  
	    Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新").setMessage(str)  
	   
	            .setPositiveButton("更新", 
	                    new DialogInterface.OnClickListener() {  
	                        @Override  
	                        public void onClick(DialogInterface dialog,  
	                                int which) { 
	                            m_progressDlg.setTitle("正在下载");  
	                            m_progressDlg.setMessage("请稍后...");  
	                            downFile(C.api.apkUrl);  
	                        }  
	                    })  
	            .setNegativeButton("暂不更新",  
	                    new DialogInterface.OnClickListener() {  
	                        public void onClick(DialogInterface dialog,  
	                                int whichButton) {  
	                          
	                            finish();  
	                        }  
	                    }).create(); 
	    dialog.show();  
	}
		private void notNewVersionDlgShow()
		{
			int verCode = Common.getVerCode(this);  
		    String verName = Common.getVerName(this); 
		    String str="verName:"+verName+" Code:"+verCode+",/n已是最新版本，无需更新";
		    Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")  
		            .setMessage(str) 
		            .setPositiveButton("确定",
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
	        intent.setDataAndType(Uri.fromFile(new File(Environment
	                .getExternalStorageDirectory(), m_appNameStr)),
	                "application/vnd.android.package-archive");
	        startActivity(intent);
	    }
public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch(taskId){
		case C.task.update:
			UpdateApk updateA;
			try {
				updateA = (UpdateApk) message.getResult("UpdateApk");
				m_newVerName = updateA.getVerName();
				m_newVerCode = Integer.valueOf(updateA.getVerCode());
				Log.d("wang","updateA id " + updateA.getId() + " verCode is " + updateA.getVerCode() + " verName is " + updateA.getVerName());
				int vercode = Common.getVerCode(getApplicationContext()); 
		         if (m_newVerCode > vercode) {  
		        	 	doNewVersionUpdate();
		         } else {  
		        	 	notNewVersionDlgShow();
		         }  
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		break;
	}
}


}
