package io.github.woodenbell.blix;

import io.github.woodenbell.blix.Server;
import io.github.woodenbell.blix.ServerConfig;

public class Main {

	public static void main(String[] args) {
			System.out.println("Starting default static server");
			defaultServerConfigLoader();
			Server blix = new Server();
			blix.startServer();
	}
	
	public static void defaultServerConfigLoader() {
		ServerConfig.setPort(8008);
		ServerConfig.setRootDir("./ServerDir");
		ServerConfig.setMimeType(".html", "text/html");
		ServerConfig.setMimeType(".css", "text/css");
		ServerConfig.setMimeType(".txt", "text/plain");
		ServerConfig.setMimeType(".js", "text/javascript");
		ServerConfig.setMimeType(".json", "application/json");
		ServerConfig.setMimeType(".jpg", "image/jgp");
		ServerConfig.setMimeType(".png", "image/png");
		ServerConfig.setMimeType(".ico", "image/x-icon");
	}
	//
}


