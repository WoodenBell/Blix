package io.github.woodenbell.blix;

import java.util.HashMap;

/**
 * This class contains the basic server configurations.
 * @author WoodenBell
 * @version 0.1
 */

public class ServerConfig {
	
	public static String rootDir;
	public static int port;
	public static HashMap<String, String> mimeTypes = new HashMap<String, String>();
	
	
	/**
	 * This method sets the root directory for static requests.
	 * @param newRootDir The directory that will be set as root.
	 */
	public static void setRootDir(String newRootDir) {
		rootDir = newRootDir;
	}
	
	/**
	 * This method sets the server port.
	 * @param newPort The port that will be used by the server.
	 */
	
	public static void setPort(int newPort) {
		port = newPort;
	}
	
	/**
	 * This method returns the server root directory.
	 * @return The server directory used for static requests.
	 */
	
	public static String getRootDir() {
		return rootDir;
	}
	
	/**
	 * This method returns the server port.
	 * @return The port used by the server.
	 */
	
	public static int getPort() {
		return port;
	}
	
	/**
	 * This method adds a MIME type for a file extension.
	 * @param fileType The file extension (starting with a dot).
	 * @param mimeType The MIME type.
	 */
	
	public static void setMimeType(String fileType, String mimeType) {
		mimeTypes.put(fileType, mimeType);
	}
	
	/**
	 * This method returns a MIME type for the specified fileType.
	 * @param fileType The file type (starting with a dot).
	 * @return The MIME type for the specified file extension, or null if not found.
	 */
	
	public static String getMimeType(String fileType) {
		return mimeTypes.get(fileType);
	}
}

