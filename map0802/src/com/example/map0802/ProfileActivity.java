package com.example.map0802;

import java.util.HashMap;

import com.example.R;
import com.example.base.BaseMessage;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.base.C.action.edittext;
import com.example.model.Customer;
import com.example.shareData.CustomerInfo;
import com.jauker.widget.BadgeView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends BaseUi{
		private EditText account;
		private EditText passwd;
		private View layout;
		AlertDialog.Builder build;
		private AlertDialog dialog;
		private String newsCount;
		private BadgeView news;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

			if(app.isLogin()){
				newsCount = getIntent().getExtras().get("newsCount").toString();
				loginView();
				//unLoginView();
			}else {
				unLoginView();
			}
			
		}
		
		@SuppressLint("NewApi")
		private void loginView() {
			setContentView(R.layout.account_info);
			TextView v_return = (TextView) findViewById(R.id.tv_return);
        		v_return.setOnClickListener(new OnClickListener() {
                        	@Override
                        	public void onClick(View arg0) {
					Intent intent = new Intent();
				        intent.putExtra("result","ok1");
        				setResult(RESULT_OK, intent);

                                	finish();
                                }
        		});
			TextView priva = (TextView) findViewById(R.id.tv_private);
			if(!app.getSign().isEmpty()) {
				priva.setText(app.getSign());
			}	
			TextView account = (TextView) findViewById(R.id.tv_account);
			account.setText(app.getUser());
			TextView qq = (TextView) findViewById(R.id.tv_qq);
			qq.setText(app.getQQ());
			TextView email = (TextView) findViewById(R.id.tv_email);
			email.setText(app.getEmail());
			TextView roadTopic = (TextView) findViewById(R.id.road_topic);
			news = (BadgeView) findViewById(R.id.news);
        		roadTopic.setOnClickListener(new OnClickListener() {
                        	@Override
                        	public void onClick(View arg0) {
					Intent intent = new Intent();
                			intent.setClass(ProfileActivity.this, MySafeRoadActivity.class);
					startActivityForResult(intent,1);
                                }
        		});
			//news.setTargetView(roadTopic);
		        //badgeView.setBadgeGravity(Gravity.CENTER | Gravity.RIGHT);
			//badgeView.setBadgeCount(Integer.valueOf(newsCount));
			news.setBadgeCount(Integer.valueOf(newsCount));
		}
		private void unLoginView(){
			setContentView(R.layout.login_register);
			TextView v_return = (TextView) findViewById(R.id.tv_return);
        		v_return.setOnClickListener(new OnClickListener() {
                        	@Override
                        	public void onClick(View arg0) {
                                	finish();
                                }
        		});

			TextView login = (TextView) findViewById(R.id.login);
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					layout = View.inflate(ProfileActivity.this, R.layout.login, null);
					account = (EditText) layout.findViewById(R.id.et_account);
					passwd = (EditText) layout.findViewById(R.id.et_passwd);
					TextView login = (TextView) layout.findViewById(R.id.login_button);
					login.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Log.d("wang","account is " + account.getText() + " passwd is " + passwd.getText());
							HashMap<String, String> locationParams = new HashMap<String, String>();
							locationParams.put("user",account.getText().toString());
							locationParams.put("password",passwd.getText().toString());
							doTaskAsync(C.task.login, C.api.login, locationParams);
						}
					});
					build = new AlertDialog.Builder(ProfileActivity.this);
					dialog = build.setView(layout).show();
				}
			});
			TextView register = (TextView) findViewById(R.id.register);
			register.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
                			intent.setClass(ProfileActivity.this, RegisterActivity.class);
                			startActivity(intent);		
				}
			});
			
		}
public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
	if((message.getCode().equals("10000")) && (taskId == C.task.login)){
		Customer customer;
		toast(message.getMessage());
		try {
			customer = (Customer) message.getResult("Customer");
			Log.d("wang","id: " + customer.getId() + " name: " + customer.getName() + " pass: " + customer.getPass() + " qq: " + customer.getQq() + 
					" email: " + customer.getEmail() + " sign: " + customer.getSign());
			SharedPreferences share = getSharedPreferences("customer", MODE_PRIVATE);
			SharedPreferences.Editor edit = share.edit();
			edit.putString("customerId", customer.getId());	
			edit.putString("user",customer.getName());
			edit.putString("sign",customer.getSign());
			edit.putString("aa",customer.getQq());
			edit.putString("email",customer.getEmail());
			edit.commit();
			app.setUser(customer.getName());
			app.setSign(customer.getSign());	
			app.setCustomerid(Integer.valueOf(customer.getId()));	
			app.setQQ(customer.getQq());	
			app.setEmail(customer.getEmail());	
			dialog.dismiss();
			
			//build.
			loginView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} else {
		toast(message.getMessage());
	}
}		
@SuppressLint("NewApi")
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("wang","MyRoad onActivityResult");
        if(data != null) {
                String result = data.getExtras().getString("result");
                if(!result.isEmpty()){
                        if(result.equals("ok1")) {
                                Log.d("wang","result is ok1");
				int flag;
				flag = app.getAllCount();
				if(flag == 0) {
					news.setVisibility(View.GONE);
				} else {
					news.setBadgeCount(flag);
				}
                        }
		}
	}
}
@Override
public void onBackPressed() {
// TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("result","ok1");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
}

}
