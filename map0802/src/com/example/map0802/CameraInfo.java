package com.example.map0802;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.base.C;

public class CameraInfo extends Activity{
	private EditText et_desc;
	private EditText et_addr;
	private RadioGroup rd_group;
	private RadioButton rd_jjz;
	private RadioButton rd_gfq;
	private RadioButton rd_wh;
	private CheckBox dir_w2e;
	private CheckBox dir_e2w;
	private CheckBox dir_s2n;
	private CheckBox dir_n2s;
	private Button submit;
	private	int radioId;
	private String direction = " ";

public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	setContentView(R.layout.camera_info);
	submit = (Button) findViewById(R.id.camera_button);
	et_desc = (EditText) findViewById(R.id.et_desc);
	et_addr = (EditText) findViewById(R.id.et_addr);
        rd_group = (RadioGroup) findViewById(R.id.radio_group);
	rd_jjz = (RadioButton) findViewById(R.id.radio_jjz);
	rd_gfq = (RadioButton) findViewById(R.id.radio_rushour);
	rd_wh = (RadioButton) findViewById(R.id.radio_tailno);
	dir_w2e = (CheckBox) findViewById(R.id.dir_w2e);
	dir_e2w = (CheckBox) findViewById(R.id.dir_e2w);
	dir_s2n = (CheckBox) findViewById(R.id.dir_s2n);
	dir_n2s = (CheckBox) findViewById(R.id.dir_n2s);
	TextView tv_return = (TextView) findViewById(R.id.re);
        tv_return.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                        	CameraInfo.this.finish();
                                }
        });

	CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			CheckBox box = (CheckBox)arg0;
			direction = box.getText().toString();
			//Toast.makeText(CameraInfo.this,"checkbox is " + box.getText(),Toast.LENGTH_LONG).show();
		}
		
	};
	dir_w2e.setOnCheckedChangeListener(listener);
	dir_e2w.setOnCheckedChangeListener(listener);
	dir_s2n.setOnCheckedChangeListener(listener);
	dir_n2s.setOnCheckedChangeListener(listener);

	rd_group.setOnCheckedChangeListener(new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			if(arg1 == rd_jjz.getId()) {
				radioId = 0;
				//Toast.makeText(CameraInfo.this, "radio id:" + rd_jjz.getId() + " text: " + rd_jjz.getText(), Toast.LENGTH_LONG).show();
			} else if(arg1 == rd_gfq.getId()) {
				radioId = 1;
				//Toast.makeText(CameraInfo.this, "radio id:" + rd_gfq.getId() + " text: " + rd_gfq.getText(), Toast.LENGTH_LONG).show();
			} else if(arg1 == rd_wh.getId()) {
				radioId = 2;
				//Toast.makeText(CameraInfo.this, "radio id:" + rd_wh.getId() + " text: " + rd_wh.getText(), Toast.LENGTH_LONG).show();
			}	
		}
		
	});
	submit.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Log.d("wang","et_dest value:" + et_desc.getText() + " et_addr: " + et_addr.getText());
			intent.putExtra("et_desc",et_desc.getText().toString());	
			intent.putExtra("et_addr",et_addr.getText().toString());
			intent.putExtra("camera_typ",String.valueOf(radioId));
			intent.putExtra("direction",direction);
			intent.putExtra("result","ok");
			CameraInfo.this.setResult(RESULT_OK, intent);
			CameraInfo.this.finish();
		}
		
	});
    }

}
