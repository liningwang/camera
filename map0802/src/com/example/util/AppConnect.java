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
        	 String fileName = AppCache.getMd5Path(file1.getName());
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
          /* 璁剧疆璇锋眰灞炴�� */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
          /*璁剧疆StrictMode 鍚﹀垯HTTPURLConnection杩炴帴澶辫触锛屽洜涓鸿繖鏄湪涓昏繘绋嬩腑杩涜缃戠粶杩炴帴*/
            //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
          /* 璁剧疆DataOutputStream锛実etOutputStream涓粯璁よ皟鐢╟onnect()*/
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());  //output to the connection
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file\";filename=\"" +
                    fileName + "\"" + end);
            ds.writeBytes(end);
          /* 鍙栧緱鏂囦欢鐨凢ileInputStream */
            InputStream fStream = SDUtil.getImageStreamfromPath(uploadFile);
          /* 璁剧疆姣忔鍐欏叆8192bytes */
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];   //8k
            int length = -1;
	    //byte b;
          /* 浠庢枃浠惰鍙栨暟鎹嚦缂撳啿鍖� */
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
            /* 灏嗚祫鏂欏啓鍏ataOutputStream涓� */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* 鍏抽棴娴侊紝鍐欏叆鐨勪笢瑗胯嚜鍔ㄧ敓鎴怘ttp姝ｆ枃*/
            fStream.close();
          /* 鍏抽棴DataOutputStream */
            ds.close();
          /* 浠庤繑鍥炵殑杈撳叆娴佽鍙栧搷搴斾俊鎭� */
            InputStream is = con.getInputStream();  //input from the connection 姝ｅ紡寤虹珛HTTP杩炴帴
            int ch;
           
            while ((ch = is.read()) != -1)
            {
                b.append((char) ch);
            }
          /* 鏄剧ず缃戦〉鍝嶅簲鍐呭 */
            Log.d("wang","wang upload b.toString()" + b.toString());
             return b.toString();
            //Toast.makeText(UploadActivity.this, b.toString().trim(), Toast.LENGTH_SHORT).show();//Post鎴愬姛
        } catch (Exception e)
        {
            /* 鏄剧ず寮傚父淇℃伅 */
                e.printStackTrace();
            //Toast.makeText(UploadActivity.this, "Fail:" + e, Toast.LENGTH_SHORT).show();//Post澶辫触
        }
        return b.toString();
    }
}

