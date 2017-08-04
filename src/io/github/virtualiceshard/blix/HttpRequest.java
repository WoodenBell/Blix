package io.github.virtualiceshard.blix;

import java.util.HashMap;
import java.nio.ByteBuffer;

public class HttpRequest {
	private HashMap<String, String> requestHeaders;
	public String path;
	public String method;
	public boolean hasFormData;
	private HashMap<String, ArrayBoxing.IntArray> form;
	
	public HttpRequest(String path, String method, HashMap<String, String> reqHeaders) {
		this.path = path;
		this.method = method;
		hasFormData = false;
		form = null;
		requestHeaders = reqHeaders;
	}
	public HttpRequest(String path, String method, HashMap<String, String> reqHeaders, HashMap<String, ArrayBoxing.IntArray> form) {
		this.path = path;
		this.method = method;
		hasFormData = true;
		this.form = form;
		requestHeaders = reqHeaders;
	}
	public String getHeaderValue(String val) {
		return requestHeaders.get(val);
	}

	public String getFormValue(String name) {
		if(form == null) return null;
		ArrayBoxing.IntArray intArray = form.get(name);
		if(intArray == null) {
			return null;
		} else {
			int[] val = intArray.array;
			StringBuilder valueStr = new StringBuilder();
			for(int v : val) {
				valueStr.append((char) v);
			}
			return valueStr.toString();
		}
	}
	
	public byte[][] getFormValueBytes(String name) {
		if(form == null) return null;
		ArrayBoxing.IntArray intArray = form.get(name);
		byte[][] btes = new byte[intArray.array.length][4];
		int count = 0;
		for(int i : intArray.array) {
			btes[count] = ByteBuffer.allocate(4).putInt(i).array();
			count++;
		}
		return btes;
	}
	
	public int[] getFormValueInt(String name) {
		if(form == null) return null;
		ArrayBoxing.IntArray intArray = form.get(name);
		return intArray.array;
	}
	
}
