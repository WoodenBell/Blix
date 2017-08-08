package io.github.woodenbell.blix;

import java.util.HashMap;

/**
 * The class representing the client request to the server. Passed
 * as argument to RequestHandler when a request is received.
 * @author WoodenBell
 * @version 0.3
 */

public class HttpRequest {
	
	/**
	 * Query string variables present in the URL
	 */
	
	private HashMap<String, String> queryString;
	
	/**
	 * The internally used hashMap that contains the  request headers.
	 */
	
	private HashMap<String, String> requestHeaders;
	
	/**
	 * The path of the request, starting from the root directory.
	 */
	
	public String path;
	
	/**
	 * The request method, either GET or POST.
	 */
	
	public String method;
	
	/**
	 * Internally used to indicate if the request body contains form data.
	 */
	
	public boolean hasFormData;
	
	/**
	 * The internally used HashMap containing the parsed form data
	 */
	
	private HashMap<String, ParsedFormData> form;
	
	/**
	 * The HTTPRequest constructor internally used to store all the parsed request body.
	 * @param path The server request path.
	 * @param method The HTTP request method.
	 * @param reqHeaders The HTTP request headers.
	 */
	
	public HttpRequest(String path, String method, HashMap<String, String> reqHeaders) {
		this.path = path;
		this.method = method;
		hasFormData = false;
		form = null;
		requestHeaders = reqHeaders;
		queryString = null;
	}
	
	/**
	 * Overloaded constructor internally used that accepts parsed form data.
	 * @param path The server request path.
	 * @param method The HTTP request method.
	 * @param reqHeaders The HTTP request headers.
	 * @param form The HahMap containing the form data.
	 */
	public HttpRequest(String path, String method, HashMap<String, String> reqHeaders, HashMap<String, ParsedFormData> form) {
		this.path = path;
		this.method = method;
		hasFormData = true;
		this.form = form;
		requestHeaders = reqHeaders;
		queryString = null;
	}
	
	
	/**
	 * Internally used to set the parsed query string HashMap.
	 * @param parsedVars The query string parsed by Util
	 * @see io.github.woodenbell.blix.Util#parseQueryString(String)
	 */
	public void setQueryString(HashMap<String, String> parsedVars) {
		if(parsedVars == null) return;
		queryString = new HashMap<>(parsedVars);
	}
	
	/**
	 * Gets the query string value for name.
	 * @param name The name of the query string value.
	 * @return The value or null if there's no query string or
	 * the value with the specified name cannot be found. 
	 */
	
	public String getQueryStringValue(String name) {
		if(queryString == null) return null;
		return queryString.get(name);
	}
	
	/**
	 * Gets the value of the header from the field name.
	 * @param name The filed name used to retrieve it's value.
	 * @return The field's value or null, if not found.
	 */
	
	public String getHeaderValue(String name) {
		return requestHeaders.get(name);
	}

	/**
	 * Returns the String equivalent of the form data of name.
	 * @param name The name that is the key for the data (generally input name attribute).
	 * @return The form data as String, or null if either the form doesn't exist or the data is not found.
	 */
	
	public String getFormValueString(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		int[] intArray = form.get(name).content;
		if(intArray == null) return null;
		return Util.intArrayToStr(form.get(name).content);
	}
	
	/**
	 * Gets the content disposition of the data of name.
	 * @param name The name that is the key for the data (generally the input name attribute).
	 * @return The Content-Disposition string, or null if either the form doesn't exist or the data is not found.
	 */
	
	public String getFormValueContentDisposition(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		if(form.get(name).headers.get("Content-Disposition") == null) return null;
		String cd =  form.get(name).headers.get("Content-Disposition");
		return cd.split(";")[0];
	}
	
	/**
	 * Gets the filename attribute in the Content-Disposition header values, if available.
	 * @param name The name that is the key for the data (generally the input name attribute).
	 * @return The string containing the filename attribute value, or null if either the attribute
	 * doesn't exist, there's no Content-Disposition header or there's no form data.
	 */
	
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
	
	/**
	 * Gets the content type (MIME type) of the form data.
	 * @param name The name that is the key for the data (generally the input name attribute).
	 * @return The string representing the MIME type of the data (if present) or null if either 
	 * the data doesn't have a MIME type specified, the form data doesn't exist or it cannot be found.
	 */
	
	public String getFormValueContentType(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		return form.get(name).headers.get("Content-Type");
	}
	
	/**
	 * Gets the form content as an array of integers. Useful for getting file data.
	 * @param name The name that is the key for the data (generally the input name attribute).
	 * @return An array of integers containing the form data contents for name or null, if either 
	 * the form data doesn't exist or it cannot be found.
	 */
	
	public int[] getFormValueInt(String name) {
		if(form == null) return null;
		if(form.get(name) == null) return null;
		return form.get(name).content;
	}
	
}
