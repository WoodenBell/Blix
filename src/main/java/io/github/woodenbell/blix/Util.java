package io.github.woodenbell.blix;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.*;

/**
 * Internal utilities class.
 * @author WoodenBell
 * @version 0.3
 * @since 0.3
 */

public class Util {
	
	/**
	 * Converts a string object to an array of integers.
	 * @param str The string to be converted.
	 * @return An array of integers representing the string.
	 */
	
	public static int[] strToIntArray(String str) {
		int[] intArray = new int[str.length()];
		for(int i = 0; i < intArray.length; i ++) {
			intArray[i] = str.charAt(i);
		}
		return intArray;
	}
	
	/**
	 * URLEncodes the essential characters string (excluding a-z, A-Z, 0-9 and other characters).
	 * @param UrlStr The string to be URLEncoded
	 * @return The URLEncoded String
	 */
	
	static String basicUrlEncode(String urlStr) {
		StringBuilder bd = new StringBuilder();
		for(char c : urlStr.toCharArray()) {
			if(URLEncodingReference.urlDecoded.containsKey(c)) bd.append(URLEncodingReference.urlDecoded.get(c));
			else bd.append(c);
		}
		return bd.toString();
	}
	
	
	/**
	 * Decodes an URL encoded string.
	 * @param urlEncStr The URL encoded string.
	 * @return The decoded string.
	 */
	
	public static String decodeURLEncoded(String urlEncStr) {
		Matcher m = Pattern.compile("%[0-9a-f-A-F]{2}").matcher(urlEncStr);
		String decStr = urlEncStr;
		String curr;
		while(m.find()) {
			curr = m.group();
			System.out.print(curr + ":::" + URLEncodingReference.urlEncoded.get(curr.toUpperCase()) + "\n");
			decStr = decStr.replaceAll(curr, "" + URLEncodingReference.urlEncoded.get(curr.toUpperCase()));
		}
		return decStr;
	}
	
	/**
	 * Converts an array of integers to a string.
	 * @param intArray The array of integers to be converted.
	 * @return The string.
	 */
	
	public static String intArrayToStr(int[] intArray) {
		StringBuilder builder = new StringBuilder();
		for(int i : intArray) builder.append((char) i);
		return builder.toString();
	}
	
	public static HashMap<String, String> parseQueryString(String queryString) {
		HashMap<String, String> parsed = new HashMap<String, String>();
		String[] kv;
		if(queryString.contains("\n")) {
			for(String s : queryString.split("\\n")) {
				kv = s.split("=");
				parsed.put(decodeURLEncoded(kv[0]), decodeURLEncoded(kv[1]));
			}
		} else {
			kv = queryString.split("\\=");
			parsed.put(decodeURLEncoded(kv[0]), decodeURLEncoded(kv[1]));
		}
		return parsed;
	}
	
	/**
	 * Gets the current working directory.
	 * @return The current working directory.
	 */
	
	public static String getCWD() {
		return Paths.get("").toAbsolutePath().toString().replaceAll("\\\\", "/");
	}

	/**
	 * Creates a HTML page for directory view.
	 * @param p The path that will be viewed.
	 * @param serverPath The requested  URL.
	 * @param rootDir The root directory of the server (used to fix path relativity).
	 * @return The string containing the HTML for the directory view.
	 * @throws IOException Errors when creating a new DirectoryStream
	 * @see DirectoryStream
	 */
	
	public static String htmlDirView(Path p, String serverPath, String rootDir) throws IOException {
		DirectoryStream<Path> dirStream = Files.newDirectoryStream(p);
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<!DOCTYPE html\r\n" +
		"<html>\r\n" +
		"<head>\r\n" +
		"<title>\r\n" +
		p.getFileName().toString() +
		"</title>\r\n" +
		"</head>\r\n" +
		"<body>\r\n" +
		"<b>\r\n" +
		p.toAbsolutePath().toString().replaceAll("\\\\", "/").replaceAll("\\.", "") + "\r\n" +
		"</b>\r\n" +
		"<br />\r\n"
		);
		if(serverPath.split("\\/").length > 1) {
			htmlBuilder.append("<a href=..>..</a>\r\n<br />\r\n");
		}
		for(Path entry : dirStream) {
			System.out.println("File name: " + entry.getFileName().toString().replace(rootDir.replaceAll("[\\.\\/]", ""), ""));
			htmlBuilder.append("<a href=" + 
		basicUrlEncode(p.getFileName().toString().replace(rootDir.replaceAll("[\\.\\/]", ""), "") + "/" + 
		entry.getFileName().toString()) + ">" +  
					entry.getFileName().toString() + "</a>\r\n" +
		"<br />\r\n");
		}
		htmlBuilder.append("</body>\r\n" +
		"</html>");
		return htmlBuilder.toString();
	}
	
	/**
	 * Wrapper for an array of bytes used to store it's content in a HashMap.
	 * @author WoodenBell
	 * @version 0.3
	 * @since 0.3
	 */
	
	public static class ByteArray {
		public byte[] content;
		
		public ByteArray(byte[] content) {
			this.content = content;
		}
	}
	
	
	/**
	 * Reverses the keys and values of the HashMap.
	 * @param hm The HashMap of Strings and Characters to have it's keys and values reversed.
	 * @return The HashMap with reversed keys and values.
	 */
	
	public static HashMap<Character, String> reverseKeyVals(HashMap<String, Character> hm) {
		HashMap<Character, String> rhm = new HashMap<>();
		Iterator<String> iter = hm.keySet().iterator();
		String curr = null;
		while(iter.hasNext()) {
			curr = iter.next();
			rhm.put(hm.get(curr), curr);
		}
		return rhm;
	}
	
	/**
	 * Checks if the HTTP request is valid.
	 * @param request THe HTTP request body.
	 * @return If the request is valid.
	 */
	
	public static boolean checkValidHTTPRequest(String request) {
		
		if(request.trim().equals("")) return false;
		
		String[] headers = request.split("\n");
		String[] pathAndMethod = headers[0].split(" ");
		
		if(pathAndMethod.length != 3) return false;
		
		if(!pathAndMethod[0].equals("GET") &&
				!pathAndMethod[0].equals("POST") &&
				!pathAndMethod[0].equals("HEAD") &&
				!pathAndMethod[0].equals("PUT") &&
				!pathAndMethod[0].equals("DELETE") &&
				!pathAndMethod[0].equals("CONNECT") &&
				!pathAndMethod[0].equals("OPTIONS") &&
				!pathAndMethod[0].equals("TRACE") &&
				!pathAndMethod[0].equals("PATCH")) return false;
		
		if(!pathAndMethod[1].startsWith("/")) return false;
		
		if(!pathAndMethod[2].startsWith("HTTP/1.1") &&
		!pathAndMethod[2].startsWith("HTTP/1.0")) return false;
		
		if(!pathAndMethod[2].startsWith("HTTP/1.1")) {
			boolean foundHost = false;
			
			if(headers.length == 1) return false;
			
			for(int i = 1; i < headers.length; i++) {
				if(headers[i].startsWith("Host")) foundHost = true;
			}
			
			if(!foundHost) return false;
		}
		return true;
	}
	
}
