package com.example.map0802;

import java.util.HashMap;

import com.baidu.mapapi.map.Text;
import com.example.R;
import com.example.base.BaseMessage;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.base.C.action.edittext;
import com.example.model.Customer;
import com.example.shareData.CustomerInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseUi{
	private EditText account;
	private EditText account_code;
	private EditText sure_code;
	private EditText sign;
	private EditText qq;
	private EditText email;
	private Button register;
	private TextView tv_return;
	@Override
   	public void onCreate(Bundle savedInstanceState) {
           // TODO Auto-generated method stub
           super.onCreate(savedInstanceState);
	   setContentView(R.layout.register_account);
	account = (EditText) findViewById(R.id.et_account);
	account_code = (EditText) findViewById(R.id.account_code);
	sure_code = (EditText) findViewById(R.id.sure_code);
	sign = (EditText) findViewById(R.id.sign);
	qq = (EditText) findViewById(R.id.qq);
	email = (EditText) findViewById(R.id.email);
	register = (Button) findViewById(R.id.register);
	tv_return = (TextView) findViewById(R.id.tv_return);
	tv_return.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                        	finish();
                        	}
	});
	register.setOnClickListener(new OnClickListener() {
                        @SuppressLint("NewApi")
						@Override
                        public void onClick(View arg0) {
				if(account.getText().toString().isEmpty()) {
					toast("账号不能是空的！");
				} else if(!account_code.getText().toString().equals(sure_code.getText().toString())) {
					toast("两次输入的密码不同，请重新输入!");
				} else {
				 HashMap<String, String> locationParams = new HashMap<String, String>();
                                 locationParams.put("name",account.getText().toString());
                                 locationParams.put("pass",account_code.getText().toString());
				 if(sign.getText().toString().isEmpty()) {
                                 	locationParams.put("sign"," ");
				 } else {
                                 	locationParams.put("sign",sign.getText().toString());
				 }
				 if(qq.getText().toString().isEmpty()) {
					locationParams.put("qq"," ");
				 } else {
                                 	locationParams.put("qq",qq.getText().toString());
				 }
				 if(email.getText().toString().isEmpty()) {
                                 	locationParams.put("email"," ");
				 } else { 
                                 	locationParams.put("email",email.getText().toString());
				 }
                                 doTaskAsync(C.task.register, C.api.register, locationParams);	
				}
			}
	});
	}
	public void onTaskComplete(int taskId, BaseMessage message) {
        	super.onTaskComplete(taskId, message);
		if((message.getCode().equals("10000")) && (taskId == C.task.register)){
			toast(message.getMessage());
		} else {
			toast(message.getMessage());
		}
	}
}
