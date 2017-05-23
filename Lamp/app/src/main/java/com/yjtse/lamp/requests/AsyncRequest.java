package com.yjtse.lamp.requests;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.asynchttp.BinaryNetWorkCallBack;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;

public class AsyncRequest {
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void ClientGet(String url, TextNetWorkCallBack callback){
		client.get(url, callback);
	}
	
	public static void ClientGet(String url, RequestParams params, TextNetWorkCallBack callback){

		client.get(url, params, callback);
	}
	public static void ClientGet(String url, BinaryNetWorkCallBack bcallback){
		client.get(url, bcallback);
	}

	public static void ClientGet(String url, RequestParams params, BinaryNetWorkCallBack bcallback){
		client.get(url, params, bcallback);
	}

	public static void ClientPost(String url, RequestParams params, TextNetWorkCallBack callback){
		client.post(url, params, callback);
	}
	public static void ClientPost(String url, RequestParams params, BinaryNetWorkCallBack bcallback){
		client.post(url, params, bcallback);
	}
	public static void ClientPost(String url, TextNetWorkCallBack callback){
		client.post(url, callback);
	}
	public static void ClientPost(String url, BinaryNetWorkCallBack bcallback){
		client.post(url,  bcallback);
	}
}
