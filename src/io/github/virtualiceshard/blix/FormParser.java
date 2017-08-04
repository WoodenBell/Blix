package io.github.virtualiceshard.blix;

import java.util.HashMap;
import java.util.ArrayList;

public class FormParser {

	public static HashMap<String, ArrayBoxing.IntArray> parseFormURLEncoded(int[] data) {
		
		HashMap<String, ArrayBoxing.IntArray> hm = new HashMap<>();
		StringBuffer fieldName = new StringBuffer();
		ArrayList<Integer> valueList = new ArrayList<Integer>();
		int[] value;
		StringBuffer encodedChar = new StringBuffer();
		char c;
		boolean gettingEncoded = false;
		short encodingCount = 0;
		boolean fieldOrValueRead = true;
		for (int v : data) {
			c = (char) v;
			if (c == '=') {
				fieldOrValueRead = false;
				continue;
			}
			if (c == '&') {
				fieldOrValueRead = true;
				value = new int[valueList.size()];
				for (int i = 0; i < valueList.size(); i++) {
					value[i] = valueList.get(i);
				}
				hm.put(fieldName.toString(), new ArrayBoxing.IntArray(value));
				fieldName.setLength(0);
				valueList.clear();
				continue;
			}
			if (c == '%') {
				gettingEncoded = true;
				encodedChar.append('%');
				encodingCount = 1;
				continue;
			}
			if (gettingEncoded) {
				if (encodingCount == 1) {
					encodedChar.append(c);
					encodingCount = 2;
				} else {
					encodedChar.append(c);
					encodingCount = 0;
					gettingEncoded = false;
					char chr = EncodingReference.urlEncoded.get(encodedChar.toString().toString());
					encodedChar.setLength(0);
					int vl = (int) chr;
					if (fieldOrValueRead) {
						fieldName.append(chr);
					} else {
						valueList.add(vl);
					}
				}
				continue;
			}
			if (c == '+') {
				if (fieldOrValueRead) {
					fieldName.append(' ');
				} else {
					valueList.add((int) ' ');
				}
				continue;
			}
			if (fieldOrValueRead) {
				fieldName.append(c);
			} else {
				valueList.add(v);
			}
		}
		fieldOrValueRead = true;
		value = new int[valueList.size()];
		for (int i = 0; i < valueList.size(); i++) {
			value[i] = valueList.get(i);
		}
		hm.put(fieldName.toString(), new ArrayBoxing.IntArray(value));
		fieldName.setLength(0);
		valueList.clear();
		return hm;
	}

	public static HashMap<String, ArrayBoxing.IntArray> parseTextPlain(int[] data) {
		
		HashMap<String, ArrayBoxing.IntArray> hm = new HashMap<>();
		StringBuffer fieldName = new StringBuffer();
		ArrayList<Integer> valueList = new ArrayList<Integer>();
		int[] value;
		char c;
		boolean fieldOrValueRead = true;
		for (int v : data) {
			c = (char) v;
			if (c == '=') {
				if (fieldOrValueRead) {
					fieldOrValueRead = false;
					continue;
				}
			}
			if (c == '\n') {
				fieldOrValueRead = true;
				value = new int[valueList.size()];
				for (int i = 0; i < valueList.size(); i++) {
					value[i] = valueList.get(i);
				}
				hm.put(fieldName.toString(), new ArrayBoxing.IntArray(value));
				fieldName.setLength(0);
				valueList.clear();
				continue;
			}
			if (fieldOrValueRead) {
				fieldName.append(c);
			} else {
				valueList.add(v);
			}
		}
		fieldOrValueRead = true;
		
		
		value = new int[valueList.size()];
		for (int i = 0; i < valueList.size(); i++) {
			value[i] = valueList.get(i);
		}
		hm.put(fieldName.toString(), new ArrayBoxing.IntArray(value));
		fieldName.setLength(0);
		valueList.clear();
		return hm;
	}

	public static HashMap<String, ArrayBoxing.IntArray> parseMultipart(int[] data, String boundary) {
		System.out.println("Parsing multipart, boudary=" + boundary);
		char c;
		HashMap<String, ArrayBoxing.IntArray> formData = new HashMap<>();
		HashMap<String, String> headers = new HashMap<>();

		boolean firstBoundary = true;
		boolean onHeaders = true;
		boolean lastLineRN = false;
		boolean lastCharR = false;
		String currName  = "";
		ArrayList<Integer> valueAssemble = new ArrayList<>();
		ArrayList<Integer> valueList = new ArrayList<>();
		StringBuffer stringLine = new StringBuffer();
		for (int v : data) {
			c = (char) v;
			if(c == '\r') {
				System.out.print("\\r");
				lastCharR = true;
				continue;
			}
			System.out.print(c);
			if(c == '\n') {
				System.out.print("\\n");
				if(lastCharR) {
					
					if(lastLineRN && onHeaders) {	
						System.out.println("========HEADERS END==========");
						onHeaders = false;
						lastLineRN = false;
						lastCharR = false;
						stringLine.setLength(0);
						continue;
					}
					lastLineRN = true;
					lastCharR = false;
					if(stringLine.toString().equals("--" + boundary) || stringLine.toString().equals("--" + boundary + "--")) {
						System.out.println("========BOUNDARY==========");
						if(firstBoundary) {
							firstBoundary = false;
							stringLine.setLength(0);
							valueAssemble.clear();
							continue;
						}
						valueList.remove(valueList.size() - 1);
						valueList.remove(valueList.size() - 1);
						int[] ia = new int[valueList.size()];
						for(int i = 0; i < ia.length; i++) {
							ia[i] = valueList.get(i);
						}		
						formData.put(currName, new ArrayBoxing.IntArray(ia));
						System.out.println("Added to form data: " + currName + " : " + ia);
						currName = "";
						valueList.clear();
						stringLine.setLength(0);
						onHeaders = true;
						if(stringLine.toString().equals("--" + boundary + "--")) break;
						continue;
					} else {
						if(onHeaders) {
							String field = stringLine.toString().split(":")[0];
							String value = stringLine.toString().split(":")[1].trim();
							headers.put(field, value);
							if(field.equals("Content-Disposition")) {
								for(String el : value.split(";")) {
									if(el.trim().split("=")[0].equals("name")) {
										currName = el.trim().split("=")[1].substring(1, el.trim().split("=")[1].length() - 1);
										break;
									}
								}
							}
						} else {
							System.out.println("========ASSEMBLING==========");
							for(int i : valueAssemble) {
								
								/*
							 	if(c == '\r') {
									System.out.print("\\r");
								} else if(c == '\n') {
									System.out.print("\\n");
								} else {
									System.out.print(c);
								}
								*/
								valueList.add(i);
							}
							valueList.add((int) '\r');
							valueList.add((int) '\n');
							valueAssemble.clear();
						}
					}
						stringLine.setLength(0);
						valueAssemble.clear();
						continue;
				} else {
					stringLine.append('\n');
					if(!onHeaders) {
						System.out.println("Newline feed added");
						valueAssemble.add((int) '\n');
					}
					continue;
				}
			}
			if(c == '\r' || c == '\n') continue;
			if(lastCharR) {
				stringLine.append('\r');
				if(!onHeaders) {
					valueAssemble.add((int) '\r');
				}
			}
			lastCharR = false;
			lastLineRN = false;
			stringLine.append(c);
			if(!onHeaders) {
				valueAssemble.add(v);
			}	
		}
		return formData;
	}
}
