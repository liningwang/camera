package com.wangln.map0802;

import java.util.HashMap;

import com.wangln.R;
import com.wangln.base.BaseMessage;
import com.wangln.base.BaseUi;
import com.wangln.base.C;
import com.wangln.base.C.action.edittext;
import com.wangln.model.Customer;
import com.wangln.shareData.CustomerInfo;
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
		private String userName = "";
		private String user = "";
		private String sign = "";
		private String mQq = "";
		private String mEmail = "";
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			if((getIntent() != null) && (getIntent().getExtras() != null) && (getIntent().getExtras().get("user") != null)){
				userName = getIntent().getExtras().get("user").toString();
			}
			Log.d("wang","Profile user name is " + userName);
			if(!userName.equals("")) {
				
				HashMap<String, String> locationParams = new HashMap<String, String>();
				locationParams.put("user",userName);
				doTaskAsync(C.task.getCustomer, C.api.getCustomer, locationParams);
			} else if((userName.equals("")) && (getIntent() != null) && (getIntent().getExtras() != null) && (getIntent().getExtras().get("user") != null)) { 
				uselessView();
			} else if(app.isLogin()){
				if((getIntent() != null) && (getIntent().getExtras() != null) && (getIntent().getExtras().get("newsCount") != null)) {
					newsCount = getIntent().getExtras().get("newsCount").toString();
					loginView();
				} else {
					unLoginView();
				}	
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
			if(!newsCount.equals("")){
				news.setBadgeCount(Integer.valueOf(newsCount));
			}
		}
	              @SuppressLint("NewApi")
                private void userView() {
                        setContentView(R.layout.user_info);
                        TextView v_return = (TextView) findViewById(R.id.tv_return);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });
                        TextView priva = (TextView) findViewById(R.id.tv_private);
                        if(!app.getSign().isEmpty()) {
                                priva.setText(sign);
                        }
                        TextView account = (TextView) findViewById(R.id.tv_account);
                        account.setText(user);
                        TextView qq = (TextView) findViewById(R.id.tv_qq);
                        qq.setText(mQq);
                        TextView email = (TextView) findViewById(R.id.tv_email);
                        email.setText(mEmail);
                }

                private void uselessView() {
                        setContentView(R.layout.useless_info);
                        TextView v_return = (TextView) findViewById(R.id.tv_return);
                        v_return.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                        finish();
                                }
                        });
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
		
	} else if(taskId == (C.task.getCustomer)){
		if((message.getCode().equals("10000"))) {
			Customer customer;
			try {
				customer = (Customer) message.getResult("Customer");
				Log.d("wang","getCustomer id: " + customer.getId() + " name: " + customer.getName() + " pass: " + customer.getPass() + " qq: " + customer.getQq() + 
						" email: " + customer.getEmail() + " sign: " + customer.getSign());
				user =  customer.getName();
				sign =  customer.getSign();
				mQq = customer.getQq();
				mEmail = customer.getEmail();
				userView();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
		    uselessView();
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
