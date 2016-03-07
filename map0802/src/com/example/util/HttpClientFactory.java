package com.example.util;

import java.util.HashMap;

public class HttpClientFactory extends HttpFactory{
	public IntenetHttp createHttp(String url) {
		return new AppClient(url);
	}		
}
