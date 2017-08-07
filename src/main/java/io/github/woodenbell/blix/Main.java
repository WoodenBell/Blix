package io.github.woodenbell.blix;


import io.github.woodenbell.blix.Server;


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
 * @version 0.2
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
			final int defPort  = 8008;
			final String defDir = "";
			int port = defPort;
			String dir = defDir;
			if(args.length > 0) {
				port = Integer.parseInt(args[0]);
				if(args.length > 1) {
					dir = args[1];
				}
			}
			Server blix = new Server(port);
			blix.getConfig().setRootDir(dir);
			defaultServerMIMETypeLoader(blix);
			blix.startServer();
	}
	
	/**
	 * Loads the default configuration MIME types to the server.
	 * @param server The server that will load the MIME type configuration.
	 */
	
	public static void defaultServerMIMETypeLoader(Server server) {
		server.getConfig().setMimeType(".html", "text/html");
		server.getConfig().setMimeType(".css", "text/css");
		server.getConfig().setMimeType(".txt", "text/plain");
		server.getConfig().setMimeType(".js", "text/javascript");
		server.getConfig().setMimeType(".json", "application/json");
		server.getConfig().setMimeType(".jpg", "image/jgp");
		server.getConfig().setMimeType(".png", "image/png");
		server.getConfig().setMimeType(".ico", "image/x-icon");
	}
	//
}


