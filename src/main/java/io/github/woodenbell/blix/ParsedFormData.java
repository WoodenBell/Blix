package io.github.woodenbell.blix;

import java.util.HashMap;
/**
 * The class representing each parsed piece of data in the form data body. When the data is parsed, 
 * it's wrapped in this class and stored in a HashMap to be used internally by HttpRequest when needed.
 * to retrieve form data.
 * @author WoodenBell
 * @version 0.2
 */

public class ParsedFormData {
	
	/**
	 * The form data content as an integer array.
	 */
	
	public int[] content;
	
	/**
	 * The name corresponding to the form data.
	 */
	
	public String name;
	
	/**
	 * The headers of the form data (if present).
	 */
	public HashMap<String, String> headers;
	
	/**
	 * The constructor used by the form data parser to store the form data.
	 * @param content The integer array containing the form data.
	 * @param headers The form data headers or null, if not present.
	 */
	
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
