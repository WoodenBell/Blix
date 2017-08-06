package io.github.woodenbell.blix;

import io.github.woodenbell.blix.Server;
import io.github.woodenbell.blix.ServerConfig;

/*
      Copyright 2017 WoodenBell
  
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */


/**
 * This is the main class for running a static server.
 * @author WoodenBell
 * @version 0.1
 * 
 */

public class Main {
	
	/**
	 *  The main class that starts the static server.
	 * @param args The arguments received when calling the application. In the first argument you 
	 * can specify the server port. If not specified, the server will use it's default port.
	 */
	
	public static void main(String[] args) {
			System.out.println("Starting default static server");
			int defPort  = 8008;
			if(args.length > 0) {
				defPort = Integer.parseInt(args[0]);
			}
			defaultServerConfigLoader(defPort);
			Server blix = new Server();
			blix.startServer();
	}
	
	/**
	 * Loads the default configuration (without routes) to the server.
	 * @param defPort The port used by the server
	 */
	
	public static void defaultServerConfigLoader(int defPort) {
		ServerConfig.setPort(defPort);
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


