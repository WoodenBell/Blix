package io.github.virtualiceshard.blix;

import java.util.HashMap;

public class ServerConfig {
	public static String rootDir;
	public static int port;
	public static HashMap<String, String> mimeTypes = new HashMap<String, String>();
	
	public static void setRootDir(String newRootDir) {
		rootDir = newRootDir;
	}
	
	public static void setPort(int newPort) {
		port = newPort;
	}
	
	public static String getRootDir() {
		return rootDir;
	}
	
	public static int getPort() {
		return port;
	}
	
	public static void setMimeType(String fileType, String mimeType) {
		mimeTypes.put(fileType, mimeType);
	}
	
	public static String getMimeType(String fileType) {
		return mimeTypes.get(fileType);
	}
}

