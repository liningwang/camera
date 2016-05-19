package com.wangln.map0802;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.wangln.R;
import com.wangln.base.BaseMessage;
import com.wangln.base.BaseUi;
import com.wangln.base.C;

public class UploadActivity extends BaseUi
{
    private String fileName = "image.jpg";  //鎶ユ枃涓殑鏂囦欢鍚嶅弬鏁�
    private String path = Environment.getExternalStorageDirectory().getPath();  //Don't use "/sdcard/" here
    private String uploadFile = path + "/" + fileName;    //寰呬笂浼犵殑鏂囦欢璺緞
    private String postUrl = "http://101.200.149.134:8003/upload/upload"; //澶勭悊POST璇锋眰鐨勯〉闈�
    private TextView mText1;
    private TextView mText2;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        //"鏂囦欢璺緞锛歕n"+
        mText1 = (TextView) findViewById(R.id.myText1);
        mText1.setText(uploadFile);
        //"涓婁紶缃戝潃锛歕n"+
        mText2 = (TextView) findViewById(R.id.myText2);
        mText2.setText(postUrl);
        /* 璁剧疆mButton鐨刼nClick浜嬩欢澶勭悊 */
        mButton = (Button) findViewById(R.id.myButton);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
 		HashMap<String, String> commentParams = new HashMap<String, String>();
		commentParams.put("file",uploadFile);
		doTaskAsync(C.task.upload, C.api.upload,commentParams,true);
            }
        });
    }
  
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch(taskId){
                case C.task.upload:
                        Log.d("wang","upload complete");

                try {
                        if(message.getCode().equals("10000")) {
				toast("上传成功！");
                        } else {
				toast(message.getMessage());
                        }
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                break;
        }
}

}
