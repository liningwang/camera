package com.wangln.util;

import java.util.HashMap;

public class HttpConnectionFactory extends HttpFactory{
	public IntenetHttp createHttp(String url) {
		return new AppConnect(url);
	}		
}
