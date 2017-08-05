package io.github.woodenbell.blix;

import java.util.HashMap;

public class HttpRequest {
	private HashMap<String, String> requestHeaders;
	public String path;
	public String method;
	public boolean hasFormData;
	private HashMap<String, ParsedFormData> form;
	
	public HttpRequest(String path, String method, HashMap<String, String> reqHeaders) {
		this.path = path;
		this.method = method;
		hasFormData = false;
		form = null;
		requestHeaders = reqHeaders;
	}
	public HttpRequest(String path, String method, HashMap<String, String> reqHeaders, HashMap<String, ParsedFormData> form2) {
		this.path = path;
		this.method = method;
		hasFormData = true;
		this.form = form2;
		requestHeaders = reqHeaders;
	}
	public String getHeaderValue(String val) {
		return requestHeaders.get(val);
	}

	public String getFormValue(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		int[] intArray = form.get(name).content;
		if(intArray == null) {
			return null;
		} else {
			StringBuilder valueStr = new StringBuilder();
			for(int v : intArray) {
				valueStr.append((char) v);
			}
			return valueStr.toString();
		}
	}
	
	public String getFormValueContentDisposition(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		if(form.get(name).headers.get("Content-Disposition") == null) return null;
		String cd =  form.get(name).headers.get("Content-Disposition");
		return cd.split(";")[0];
	}
	
	public String getFormValueFileName(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		if(form.get(name).headers.get("Content-Disposition") == null) return null;
		String ret = null;
		String cd =  form.get(name).headers.get("Content-Disposition");
		for(String s : cd.split(";")) {
			if(s.split("=")[0].trim().equals("filename")) {
				ret = s.split("=")[1].trim();
			}
		}
		return ret.replaceAll("^\"", "").replaceAll("\"$", "");
	}
	
	public String getFormValueContentType(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		return form.get(name).headers.get("Content-Type");
	}
	
	public int[] getFormValueInt(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		int[] intArray = form.get(name).content;
		return intArray;
	}
	
}
