package com.wangln.util;

import java.util.HashMap;

public class HttpClientFactory extends HttpFactory{
	public IntenetHttp createHttp(String url) {
		return new AppClient(url);
	}		
}
