package com.example.base;

import com.example.util.AppUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class BaseHandler extends Handler {
	
	protected BaseUi ui;
	
	public BaseHandler (BaseUi ui) {
		this.ui = ui;
	}
	
	public BaseHandler (Looper looper) {
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		try {
			int taskId;
			String result;
			switch (msg.what) {
				case BaseTask.TASK_COMPLETE:
					taskId = msg.getData().getInt("task");
					result = msg.getData().getString("data");
					Log.d("wang","handleMessage is " + result);
					if (result != null) {
						ui.onTaskComplete(taskId, AppUtil.getMessage(result));
					} else if (!AppUtil.isEmptyInt(taskId)) {
						ui.onTaskComplete(taskId);
					} else {
						ui.toast(C.err.message);
					}
					break;
				case BaseTask.NETWORK_ERROR:
					
					taskId = msg.getData().getInt("task");
					ui.onNetworkError(taskId);
					break;
				case BaseTask.SHOW_LOADBAR:
					
					break;
				case BaseTask.HIDE_LOADBAR:
					
					break;
				case BaseTask.SHOW_TOAST:
					
					result = msg.getData().getString("data");
					ui.toast(result);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ui.toast(e.getMessage());
		}
	}
	
}