package io.github.woodenbell.blix;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
		p.toAbsolutePath().toString().replaceAll("\\\\", "/") + "\r\n" +
		"</b>\r\n" +
		"<br />\r\n"
		);
		if(serverPath.split("\\/").length > 1) {
			htmlBuilder.append("<a href=..>..</a>\r\n<br />\r\n");
		}
		for(Path entry : dirStream) {
			htmlBuilder.append("<a href=" + 
		p.getFileName().toString().replace(rootDir.replaceAll("[\\.\\/]", ""), "") + "/" + 
		entry.getFileName().toString() + ">" +  
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
	
}
