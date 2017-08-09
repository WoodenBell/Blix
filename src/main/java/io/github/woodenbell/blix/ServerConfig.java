package io.github.woodenbell.blix;

import java.util.HashMap;

/**
 * This class contains the basic server configurations.
 * @author WoodenBell
 * @version 0.3
 */

public class ServerConfig {
	
	private int port;
	private String rootDir;
	private HashMap<String, String> mimeTypes;
	private boolean useCache;
	private int cacheTime;
	private boolean debugMode;
	private boolean showDirView;
	
	
	/**
	 *  Initializes the default values.
	 */
	
	public ServerConfig() {
		port = 8008;
		rootDir = "";
		useCache = false;
		cacheTime = 0;
		mimeTypes  = new HashMap<String, String>();
		debugMode = false;
		showDirView = true;
	}
	
	/**
	 * This method sets the root directory for static requests.
	 * @param newRootDir The directory that will be set as root.
	 */
	public void setRootDir(String newRootDir) {
		rootDir = newRootDir;
	}
	
	/**
	 * This method sets the server port.
	 * @param newPort The port that will be used by the server.
	 */
	
	public void setPort(int newPort) {
		port = newPort;
	}
	
	/**
	 * This method returns the server root directory.
	 * @return The server directory used for static requests.
	 */
	
	public String getRootDir() {
		return rootDir;
	}
	
	/**
	 * This method returns the server port.
	 * @return The port used by the server.
	 */
	
	public int getPort() {
		return port;
	}
	
	/**
	 * This method adds a MIME type for a file extension.
	 * @param fileType The file extension (starting with a dot).
	 * @param mimeType The MIME type.
	 */
	
	public void setMimeType(String fileType, String mimeType) {
		mimeTypes.put(fileType, mimeType);
	}
	
	/**
	 * This method returns a MIME type for the specified fileType.
	 * @param fileType The file type (starting with a dot).
	 * @return The MIME type for the specified file extension, or null if not found.
	 */
	
	public String getMimeType(String fileType) {
		return mimeTypes.get(fileType);
	}
	
	/**
	 * Sets the cache data time to expire.
	 * @param cacheTime The time in milliseconds until the cached data expires.
	 */
	
	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}
	
	/**
	 * Gets the cached data time until it expires.
	 * @return The time in milliseconds until the cached data expires.
	 */
	
	public int getCacheTime() {
		return cacheTime;
	}
	
	/**
	 * Sets if the cache should be used or not.
	 * @param useCache If the cache should be used or not.
	 * @see io.github.woodenbell.blix.cache.MapCacheManager
	 */
	
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	
	
	/**
	 * Returns if the cache should be used or not.
	 * @return If the cache should be used.
	 * @see io.github.woodenbell.blix.cache.MapCacheManager
	 */
	
	public boolean getUseCache() {
		return useCache;
	}
	
	/**
	 * Sets debug mode on/off.
	 * @param debugMode If the debug mode 
	 */
	
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	/**
	 * Returns if debug mode is on
	 * @return If debug mode is on
	 */
	
	public boolean getDebugMode() {
		return debugMode;
	}
	
	public void setShowDirView(boolean showDirView) {
		this.showDirView = showDirView;
	}
	
	public boolean getShowDirView() {
		return showDirView;
	}
}

