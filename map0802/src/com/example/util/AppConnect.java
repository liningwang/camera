package com.example.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.example.base.C;

import android.util.Log;

public class AppConnect extends AppClient implements IntenetHttp{
	private String apiUrl;
	public AppConnect(String url) {
                initConnect(url);
        }
	private void initConnect (String url) {
                // initialize API URL
                this.apiUrl = C.api.base + url;
                String apiSid = "wang123";
                if (apiSid != null && apiSid.length() > 0) {
                        this.apiUrl += "?sid=" + apiSid;
                }
        }

public String post(HashMap urlParams) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        StringBuffer b = new StringBuffer();;
        try
        {
        	 String uploadFile =  (String) urlParams.get("file");
        	 File file1 = new File(uploadFile);
        	 String fileName = file1.getName();
		Log.d("wang","uploadFile is " + uploadFile + " filename is " + fileName);
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
          /* Output to the connection. Default is false,
             set to true because post method must write something to the connection */
            con.setDoOutput(true);
          /* Read from the connection. Default is true.*/
            con.setDoInput(true);
          /* Post cannot use caches */
            con.setUseCaches(false);
          /* Set the post method. Default is GET*/
            con.setRequestMethod("POST");
          /* 设置请求属性 */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
          /*设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接*/
            //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
          /* 设置DataOutputStream，getOutputStream中默认调用connect()*/
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());  //output to the connection
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file\";filename=\"" +
                    fileName + "\"" + end);
            ds.writeBytes(end);
          /* 取得文件的FileInputStream */
            InputStream fStream = SDUtil.getImageStreamfromPath(uploadFile);
          /* 设置每次写入8192bytes */
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];   //8k
            int length = -1;
	    //byte b;
          /* 从文件读取数据至缓冲区 */
	   /*while(b != -1) {
	  	 for(int i = 0; i < bufferSize; i++)
	  	 {
	  	      b = bStream.read();
	  	      if(b == -1) 
		      {
			 break;
		      }
	  	      buffer[i] = b;
	  	 }
		 length = 
          	 ds.write(buffer, 0, length);
	    }*/
            while ((length = fStream.read(buffer)) != -1)
            {
            /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* 关闭流，写入的东西自动生成Http正文*/
            fStream.close();
          /* 关闭DataOutputStream */
            ds.close();
          /* 从返回的输入流读取响应信息 */
            InputStream is = con.getInputStream();  //input from the connection 正式建立HTTP连接
            int ch;
           
            while ((ch = is.read()) != -1)
            {
                b.append((char) ch);
            }
          /* 显示网页响应内容 */
            Log.d("wang","wang upload b.toString()" + b.toString());
             return b.toString();
            //Toast.makeText(UploadActivity.this, b.toString().trim(), Toast.LENGTH_SHORT).show();//Post成功
        } catch (Exception e)
        {
            /* 显示异常信息 */
                e.printStackTrace();
            //Toast.makeText(UploadActivity.this, "Fail:" + e, Toast.LENGTH_SHORT).show();//Post失败
        }
        return b.toString();
    }
}

