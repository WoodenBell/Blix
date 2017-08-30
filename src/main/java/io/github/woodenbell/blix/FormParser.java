package io.github.woodenbell.blix;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Internally used class that parses form data found in the request body.
 * @author WoodenBell
 * @version 0.3
 */

public class FormParser {

	/**
	 * Internally used to parse URLEncoded form data.
	 * @param data An integer array containing the form data.
	 * @return A HashMap of String and ParsedFormData containing each of the parsed elements.
	 * @see io.github.woodenbell.blix.ParsedFormData
	 */
	
	public static HashMap<String, ParsedFormData> parseFormURLEncoded(int[] data) {
		
		HashMap<String, ParsedFormData> hm = new HashMap<>();
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
				hm.put(fieldName.toString(), new ParsedFormData(value, null));
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
					char chr = URLEncodingReference.urlEncoded.get(encodedChar.toString().toString());
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
		hm.put(fieldName.toString(), new ParsedFormData(value, null));
		fieldName.setLength(0);
		valueList.clear();
		return hm;
	}

	/**
	 * Internally used to parse plain text form data.
	 * @param data An integer array containing the form data.
	 * @return A HashMap of String and ParsedFormData containing each of the parsed elements.
	 * @see io.github.woodenbell.blix.ParsedFormData
	 */
	
	public static HashMap<String, ParsedFormData> parseTextPlain(int[] data) {
		
		HashMap<String, ParsedFormData> hm = new HashMap<>();
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
				hm.put(fieldName.toString(), new ParsedFormData(value, null));
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
		hm.put(fieldName.toString(), new ParsedFormData(value, null));
		fieldName.setLength(0);
		valueList.clear();
		return hm;
	}

	/**
	 * Internally used to parse multipart form data.
	 * @param data An integer array containing the form data.
	 * @param boundary The boundary of the multipart body.
	 * @return A HashMap of String and ParsedFormData containing each of the parsed elements.
	 * @see io.github.woodenbell.blix.ParsedFormData
	 */
	
	public static HashMap<String, ParsedFormData> parseMultipart(int[] data, String boundary) {
		//System.out.println("Parsing multipart, boudary=" + boundary);
		char c;
		HashMap<String, ParsedFormData> formData = new HashMap<>();
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
			System.out.print(c);
			if(c == '\r') {
				//System.out.println("\\r");
				if(lastCharR) {
					stringLine.append('\r');
					if(!onHeaders) {
						valueAssemble.add((int) '\r');
					}
				}
				lastCharR = true;
				continue;
			}

			
			if(c == '\n') {
				//System.out.print("\\n");
				if(lastCharR) {
					
					if(lastLineRN && onHeaders) {	
						//System.out.println("========HEADERS END==========");
						onHeaders = false;
						lastLineRN = false;
						lastCharR = false;
						stringLine.setLength(0);
						continue;
					}
					lastLineRN = true;
					lastCharR = false;
					if(stringLine.toString().equals("--" + boundary) || stringLine.toString().equals("--" + boundary + "--")) {
						//System.out.println("========BOUNDARY==========");
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
						formData.put(currName, new ParsedFormData(ia, new HashMap<String, String>(headers)));
						//System.out.println("Added to form data: " + currName);
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
							//System.out.println("Added to headers: {" + field + " : " + value + "}");
							headers.put(field, value);
							if(field.trim().equals("Content-Disposition")) {
								for(String s : value.trim().split(";")) {
									if(s.split("=")[0].trim().equals("name")) {
										currName = s.split("=")[1].trim().replaceAll("^\"", "")
												.replaceAll("\"$", "");
										//System.out.println("Found name: " + currName);
									}
								}
							}
						} else {
							//System.out.println("========ASSEMBLING==========");
							for(int i : valueAssemble) {
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
						//System.out.println("Newline feed added");
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
		if (valueList.size() > 0) {
			int[] ia = new int[valueList.size()];
			for(int i = 0; i < ia.length; i++) {
				ia[i] = valueList.get(i);
			}		
			formData.put(currName, new ParsedFormData(ia, new HashMap<String, String>(headers)));
			//System.out.println("Added to form data: " + currName);
		}
		return formData;
	}
}
