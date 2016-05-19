package com.wangln.base;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

import com.wangln.util.HttpClientFactory;
import com.wangln.util.HttpConnectionFactory;
import com.wangln.util.HttpFactory;
import com.wangln.util.HttpUtil;
import com.wangln.util.IntenetHttp;

import com.wangln.util.AppClient;
import android.util.Log;

public class BaseTaskPool {
	
	// task thread pool
	static private ExecutorService taskPool;
	
	// for HttpUtil.getNetType
	private Context context;
	
	public BaseTaskPool (BaseUi ui) {
		this.context = ui.getContext();
		taskPool = Executors.newCachedThreadPool();
	}
	
	// http post task with params
	public void addTask (int taskId, String taskUrl, HashMap<String, String> taskArgs, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, taskArgs, baseTask, delayTime, false));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	// http upload file
	public void addTask (int taskId, String taskUrl, HashMap<String, String> taskArgs, boolean isFile, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, taskArgs, baseTask, delayTime, isFile));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	// http post task without params
	public void addTask (int taskId, String taskUrl, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, null, baseTask, delayTime, false));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	// custom task
	public void addTask (int taskId, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, null, null, baseTask, delayTime,false));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	// task thread logic
	private class TaskThread implements Runnable {
		private Context context;
		private String taskUrl;
		private HashMap<String, String> taskArgs;
		private BaseTask baseTask;
		private int delayTime = 0;
		private boolean isFile;
		public TaskThread(Context context, String taskUrl, HashMap<String, String> taskArgs, BaseTask baseTask, int delayTime, boolean isFile) {
			this.context = context;
			this.taskUrl = taskUrl;
			this.taskArgs = taskArgs;
			this.baseTask = baseTask;
			this.delayTime = delayTime;
			this.isFile = isFile;
		}
		@Override
		public void run() {
			try {
				Log.d("wang","task pool run");
				baseTask.onStart();
				String httpResult = null;
				// set delay time
				if (this.delayTime > 0) {
					Thread.sleep(this.delayTime);
				}
				try {
					// remote task
					if (this.taskUrl != null) {
						HttpFactory clientFactory;
						IntenetHttp client;
						Log.d("wang","task pool url " + this.taskUrl);
						if(isFile == false) {
							// init app client
							 clientFactory = new HttpClientFactory();
							 client = clientFactory.createHttp(this.taskUrl);
						} else {
							 clientFactory = new HttpConnectionFactory();
							 client = clientFactory.createHttp(this.taskUrl);
						}
						/*if (HttpUtil.WAP_INT == HttpUtil.getNetType(context)) {
							((AppClient) client).useWap();
						}*/
						// http get
						if (taskArgs == null) {
							httpResult = ((AppClient) client).get();
						// http post
						} else {
							httpResult = client.post(this.taskArgs);
						}
						//Log.d("wang","task httpresult is " + httpResult);
					}
						// remote task
						if (httpResult != null) {
							baseTask.onComplete(httpResult);
						// local task
						} else {
							baseTask.onComplete();	
					}
				} catch (Exception e) {
					baseTask.onError(e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					baseTask.onStop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
