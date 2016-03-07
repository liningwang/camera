package com.example.map0802;
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

import com.example.R;
import com.example.base.BaseMessage;
import com.example.base.BaseUi;
import com.example.base.C;

public class UploadActivity extends BaseUi
{
    private String fileName = "image.jpg";  //报文中的文件名参数
    private String path = Environment.getExternalStorageDirectory().getPath();  //Don't use "/sdcard/" here
    private String uploadFile = path + "/" + fileName;    //待上传的文件路径
    private String postUrl = "http://172.28.32.117:8003/upload/upload"; //处理POST请求的页面
    private TextView mText1;
    private TextView mText2;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        //"文件路径：\n"+
        mText1 = (TextView) findViewById(R.id.myText1);
        mText1.setText(uploadFile);
        //"上传网址：\n"+
        mText2 = (TextView) findViewById(R.id.myText2);
        mText2.setText(postUrl);
        /* 设置mButton的onClick事件处理 */
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
				toast("upload complete");
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