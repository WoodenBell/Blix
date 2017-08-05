package io.github.woodenbell.blix;

import java.util.HashMap;

public class ParsedFormData {
	public int[] content;
	public String name;
	public HashMap<String, String> headers;
	
	
	public ParsedFormData(int[] content, HashMap<String, String> headers) {
		this.content = content;
		try {
		this.headers = headers;
		} catch (ClassCastException e) {
			e.printStackTrace();
			this.headers = null;
		}
	}
}
