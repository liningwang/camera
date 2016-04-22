package com.example.base;

import java.net.Socket;

public final class C {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// core settings (important)
	
	public static final class dir {
		public static final String base				= "/sdcard/demos";
		public static final String faces			= base + "/faces";
		public static final String images			= base + "/images";
	}
	
	public static final class api {
		public static final String base				= "http://172.28.32.177:8003";
		//public static final String base				= "http://192.168.1.107:8003";
		public static final String imageUrl 			= "http://172.28.32.177:8004/faces/road/";
		public static final String apkUrl 			= "http://172.28.32.177:8004/apk/update.apk";
		public static final String register			= "/customer/customerCreate";
		public static final String login			= "/customer/login";
		public static final String logout			= "/index/logout";
		public static final String createCamera		= "/camera/cameraCreate";
		public static final String getCamera 		= "/camera/getCamera";
		public static final String zan				= "/camera/cameraZan";
		public static final String buzan			= "/camera/cameraBuzan";
		public static final String getCameraById		= "/camera/getCameraById";
		public static final String commentList		= "/comment/commentList";
		public static final String commentCreate	= "/comment/commentCreate";
		public static final String replyList		= "/reply/replyList";
		public static final String replyCreate		= "/reply/replyCreate";
		public static final String replyRoadList	= "/replyRoad/replyRoadList";
		public static final String replyRoadCreate		= "/replyRoad/replyRoadCreate";
		public static final String upload			= "/upload/upload";
		public static final String safeRoadCreate			= "/safeRoad/safeRoadCreate";
		public static final String safeRoadList			= "/safeRoad/safeRoadList";
		public static final String safeRoadCount			= "/safeRoad/safeRoadCount";
		public static final String safeRoadCountById			= "/safeRoad/safeRoadCountById";
		public static final String safeRoadEachCountById			= "/safeRoad/safeRoadEachCountById";
		public static final String commentCount			= "/comment/commentCount";
		public static final String update			= "/update/updateApk";
	}
	
	public static final class task {
		public static final int register			= 1001;
		public static final int login				= 1002;
		public static final int logout				= 1003;
		public static final int createCamera		= 1004;
		public static final int getCamera			= 1005;
		public static final int zan					= 1006;
		public static final int buzan			= 1007;
		public static final int getCameraById			= 1008;
		public static final int commentList			= 1009;
		public static final int commentCreate		= 1010;
		public static final int replyList		= 1011;
		public static final int replyCreate		= 1012;
		public static final int upload				= 1013;
		public static final int safeRoadCreate				= 1014;
		public static final int safeRoadList				= 1015;
		public static final int replyRoadCreate				= 1016;
		public static final int replyRoadList				= 1017;
		public static final int safeRoadCount			= 1018;
		public static final int commentCount			= 1019;
		public static final int safeRoadCountById		= 1020;
		public static final int safeRoadEachCountById		= 1021;
		public static final int update		= 1022;
	}
	
	public static final class err {
		public static final String network			= "���Ӵ���";
		public static final String message			= "��Ϣ����";
		public static final String jsonFormat		= "��Ϣ��ʽ����";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// intent & action settings
	
	public static final class intent {
		public static final class action {
			public static final String EDITTEXT		= "com.app.demos.EDITTEXT";
			public static final String EDITBLOG		= "com.app.demos.EDITBLOG";
		}
	}
	
	public static final class action {
		public static final class edittext {
			public static final int CONFIG			= 2001;
			public static final int COMMENT			= 2002;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// additional settings
	
	public static final class web {
		public static final String base				= "http://10.0.2.2:8002";
		public static final String index			= base + "/index.php";
		public static final String gomap			= base + "/gomap.php";
	}
}
