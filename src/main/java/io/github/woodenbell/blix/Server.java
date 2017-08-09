package io.github.woodenbell.blix;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import io.github.woodenbell.blix.cache.MapCacheManager;

import java.nio.file.Files;
import java.io.File;

/**
 * The class that represents the server and is responsible for receiving the requests and passing
 * then to the handlers or static files. Also is responsible for responding with HTML error codes
 * if any of them occur.
 * @author WoodenBell
 * @version 0.3
 */

public class Server {
	
	
	private MapCacheManager<Util.ByteArray> mapCache;
	
	/**
	 * Responsible for allowing or denying static requests. Allows every request by default.
	 */
	private StaticAccessController staticController;
	
	/**
	 * The server configuration object. The object contains the port, root directory and MIME types, that
	 * can be retrieved and set by the user using it's setters and getters.
	 * @see io.github.woodenbell.blix.ServerConfig
	 */
	
	private final ServerConfig config;
	
	/**
	 * The server socket that will accept all client requests. Used internally.
	 */
	
	private ServerSocket server;
	
	/**
	 * Used internally to indicate if the server is running or not. If not, the server loop
	 * is stopped.
	 * @see io.github.woodenbell.blix.Server#handleLoop()
	 */
	
	private boolean serverRunning;
	
	/**
	 * The HashMap containing the GET routes for the server and their respective paths.
	 */
	
	private HashMap<String, RequestHandler> getRoutes;
	
	/**
	 * The HashMap containing the POST routes for the server and their respective paths.
	 */
	
	private HashMap<String, RequestHandler> postRoutes;
	
	/**
	 * The  HashMap containing the HTTP codes and their respective handlers.
	 */
	
	private static HashMap<String, RequestHandler> httpCodes;
	
	/**
	 * The server constructor. Initializes the HTTP codes HashMap that will be used
	 * internally when an error occurs.
	 * @param port The port that will be used by the server.
	 */
	
	public Server(int port) {
		config = new ServerConfig();
		config.setPort(port);
		getRoutes = new HashMap<String, RequestHandler>();
		postRoutes = new HashMap<String, RequestHandler>();
		staticController = new StaticAccessController() {
			public boolean controlStaticRequest(HttpRequest req) {
				return true;
			}
		};
		try {
		server = new ServerSocket(port);
		} catch(IOException e) {
			System.out.println("IOException ocurred while starting server, aborting...");
			e.printStackTrace();
			serverRunning = false;
			System.exit(-1);
		}
		httpCodes = new HashMap<String, RequestHandler>();
		httpCodes.put("403", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) {
				response.sendResponse(403, "Forbidden");
				response.sendHeader("Content-Type", "text/html");
				response.endHeaders();
				response.write("<!DOCTYPE html>\r\n" + 
						"<html>" + 
						"<head>" + 
						"<title>403</title>" + 
						"</head>" + 
						"<body>" + 
						"<h1>403 Forbidden</h1>" + 
						"</body>" + 
						"</html>");
				response.endResponse();
			}
		});
		httpCodes.put("404", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) {
				response.sendResponse(404, "Not Found");
				response.sendHeader("Content-Type", "text/html");
				response.endHeaders();
				response.write("<!DOCTYPE html>\r\n" + 
						"<html>" + 
						"<head>" + 
						"<title>404</title>" + 
						"</head>" + 
						"<body>" + 
						"<h1>404 Not Found</h1>" + 
						"</body>" + 
						"</html>");
				response.endResponse();
			}
		});
		
		httpCodes.put("500", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				response.sendResponse(500, "Internal Server Error");
				response.sendHeader("Content-Type", "text/html");
				response.endHeaders();
				response.write("<!DOCTYPE html>" + 
						"<html>" + 
						"<head>" + 
						"<title>500</title>" + 
						"</head>" + 
						"<body>" + 
						"<h1>500 Internal Server Error</h1>" + 
						"</body>" + 
						"</html>");
				response.endResponse();
			}
			
		});
	}
	
	/**
	 * Sets the new controller for static requests.
	 * @param controller The new static requests controller.
	 */
	
	public void setStaticRequestController(StaticAccessController controller) {
		staticController = controller;
	}
	
	/**
	 * Starts the server loop and prints the used port to the console.
	 * Each new client socket will be accepted in the server loop and handled  in
	 * a separated Thread
	 */
	
	public void startServer() {
			System.out.println("Server started at port " + config.getPort());
			System.out.println("Running at " + Util.getCWD());
			if(config.getUseCache()) mapCache = new MapCacheManager<Util.ByteArray>();
			else mapCache = null;
			serverRunning = true;
			handleLoop();
	}
	
	/**
	 * Sets the handler for a HTTP status code.
	 * @param code An integer representing the HTTP code
	 * @param handler A RequestHandler responsible for responding with the code.
	 */
	
	public void setCodeHandler(int code, RequestHandler handler) {
		httpCodes.put(code + "", handler);
	}
	
	
	/**
	 * Internally used to start a new Thread responsible for handling the client Socket accepted by the 
	 * server loop. Also reads the request body headers and build a string with them.
	 * @see io.github.woodenbell.blix.Server#handleLoop()
	 * @param client The accepted client Socket.
	 */
	
	private void handleNewClient(Socket client) {
		System.out.println("Handling new client");
		Thread t = new Thread() {
			public void run() {
				try {
					InputStream out = client.getInputStream();
				StringBuilder request = new StringBuilder();
				StringBuilder line = new StringBuilder();
					 int c;
					 char ch;
					 boolean lastLineRN = false;
					 while((c = out.read()) > 0) {
						 ch = (char) c;
						 if (ch == '\n') {
							 request.append(line.toString() + "/n");
							 line.setLength(0);
							 continue;
						 }
					        if (ch == '\r') {
					            c = out.read();
					            ch = (char) c;
					            if (ch == '\n' && c > 0 && !lastLineRN) {
					            	lastLineRN = true;
					            	request.append(line.toString() + "\n");
									line.setLength(0);
					            } else {
					            	if(ch == '\n' && lastLineRN) {
					            		request.append("\n");
					            		break;
					            	}
					            line.append('\r');
					            }
					            continue;
					        }
					        lastLineRN = false;
					        line.append(ch);
					 }
					System.out.println(request.toString());
					handleRequest(request.toString(), client, out);
					out.close();
				} catch(IOException ioe) {
					try {
						httpCodes.get("500").handleRequest(new HttpRequest("", "", new HashMap<String, String>()),
								new HttpResponse(client));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println();
			}
		};
		t.start();
	}
	
	/**
	 * Internally used to loop and accept each new client and pass it to the client handler.
	 * @see io.github.woodenbell.blix.Server#handleNewClient(Socket)
	 */
	
	private void handleLoop() {
		Socket client;
		while(serverRunning) {
			try {
			client = server.accept();
			handleNewClient(client);
			} catch(IOException ioe) {
				System.out.println("Error ocurred while acception client");
				ioe.printStackTrace();
			}
		}
	}
	
	/**
	 * Used internally to handle the HTTP request. Finding it's route or file path, and then doing a 
	 * static request or calling the ReqeustHandler if there's one for the request path. 
	 * If none of them are found, the method searches for index.html or index.htm, 
	 * calling the handler for HTTP 404 if both indexes cannot be found.
	 * @param requestData The string containing the request headers.
	 * @param client The client socket that made the request.
	 * @param rd The InputStream that will be read to look for form data present in the request body.
	 * @throws IOException Errors while reading additional data.
	 * @see IOException
	 */
	
	private void handleRequest(String requestData, Socket client, InputStream rd) throws IOException {
		System.out.println("Handling new request");
		HashMap<String, String> reqDict = new HashMap<String, String>();
		String[] reqData = requestData.split("\n");
		for(int i = 0; i < reqData.length; i++) {
			if(i != 0) {
				reqDict.put(reqData[i].split(":")[0], reqData[i].split(":")[1].trim());
			}
		}
			HashMap<String, ParsedFormData> form = null;
			if(reqDict.get("Content-Length") != null) {
				int[] formData = new int[Integer.parseInt(reqDict.get("Content-Length"))];
				int count = 0;
				int c;
				while(rd.available() > 0 && (c = rd.read()) != -1) {
					formData[count] = c;
					count++;
				}
				System.out.print("\n");
				System.out.println("Content-Length found: " + Integer.parseInt(reqDict.get("Content-Length")));
				if(reqDict.get("Content-Type").startsWith("application/x-www-form-urlencoded")) {
				form = FormParser.parseFormURLEncoded(formData);
				}
				if(reqDict.get("Content-Type").startsWith("multipart/form-data")) {
					String boundary = reqDict.get("Content-Type").split(";")[1].trim().split("boundary=")[1];
					form = FormParser.parseMultipart(formData, boundary);
				}
				if(reqDict.get("Content-Type").startsWith("text/plain")) {
					form = FormParser.parseTextPlain(formData);
				}
				System.out.println("form=" + form);
				
			}
			if(requestData.trim().equals("")) {
				System.out.println("Headers not found, returning...");
				return;
			}
			String[] reqMethStr = reqData[0].split(" ");
			String method = reqMethStr[0];
			HashMap<String, String> queryStr = null;
			String path;
			if(reqMethStr[1].contains("?")) {
				String qrStr = reqMethStr[1].split("\\?")[1];
				queryStr = Util.parseQueryString(qrStr);
				path = parseURLEncoded(reqMethStr[1].split("\\?")[0]);
			} else {
				path = parseURLEncoded(reqMethStr[1]);
			}
			
			HttpRequest req = new HttpRequest(path, method, reqDict, form);
			req.setQueryString(queryStr);
			HttpResponse res = new HttpResponse(client);
			
			File f = new File(Util.getCWD() + config.getRootDir() + path);
			System.out.println("Checking for files in " + Util.getCWD() + config.getRootDir() + path);
			if(method.equals("GET")) {
				try {
					System.out.println("Get request");
					getRoutes.get(path).handleRequest(req, res);
				} catch(NullPointerException e) {
					if(f.exists()) {
						if(f.isDirectory()) {
							System.out.println("Path is a directory. Checking for index files");
							File f2 = new File(config.getRootDir() + path + "/index.html");
							if(f2.exists()) {
								if(f2.isFile()) {
									System.out.println("index.html found");
									doStaticGet(req, res, "index.html");
							} else {
								if(config.getShowDirView()) {
									System.out.println("Showing dir view");
									String htmlDirView = Util.htmlDirView(Paths.get(Util.getCWD() +
											config.getRootDir() + path), path, config.getRootDir());
									res.sendResponse(200, "OK");
									res.sendHeader("Content-Type", "text/html");
									res.endHeaders();
									res.write(htmlDirView);
									res.endResponse();
								} else {
									httpCodes.get("403").handleRequest(req, res);
								}
							}
						} else {
							File f3 = new File(config.getRootDir() + path + "/index.htm");
							if(f3.exists()) {
								if(f3.isFile()) {
									System.out.println("index.htm found");
									doStaticGet(req, res, "index.htm");
							} else {
								if(config.getShowDirView()) {
									System.out.println("Showing dir view");
									String htmlDirView = Util.htmlDirView(Paths.get(Util.getCWD() +
											config.getRootDir() + path), path, config.getRootDir());
									res.sendResponse(200, "OK");
									res.sendHeader("Content-Type", "text/html");
									res.endHeaders();
									res.write(htmlDirView);
									res.endResponse();
								} else {
									httpCodes.get("403").handleRequest(req, res);
								}		
							}
						} else {
							if(config.getShowDirView()) {
								System.out.println("Showing dir view");
								String htmlDirView = Util.htmlDirView(Paths.get((Util.getCWD() +
										config.getRootDir() + path)), path, config.getRootDir());
								res.sendResponse(200, "OK");
								res.sendHeader("Content-Type", "text/html");
								res.endHeaders();
								res.write(htmlDirView);
								res.endResponse();
							} else {
								httpCodes.get("403").handleRequest(req, res);
							}
						}
				}
			} 
			if(f.isFile()) {
				System.out.println("Path is file. Doing static request");
				try {
				doStaticGet(req, res);
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
				} else {
					httpCodes.get("404").handleRequest(req, res);
				}
			}
			}
			if(method.equals("POST")) {
				try {
					System.out.println("Post request");
					if(postRoutes.get(path) == null) {
						httpCodes.get("404").handleRequest(req, res);
					} else {
						postRoutes.get(path).handleRequest(req, res);
					}
				} catch(Exception e) {
						e.printStackTrace();
						httpCodes.get("500").handleRequest(req, res);
				} 
		}
		
	}
	
	/**
	 * Internally used to respond with static content. If forbidden, send 403 response.
	 * @param request The object representing the HTTPRequest. made by the client.
	 * @param response The object representing the HHTPResponse used to write the response.
	 * @throws IOException Errors while reading the file data.
	 * @see IOException.
	 */
	
	private void doStaticGet(HttpRequest request, HttpResponse response) throws IOException {
		System.out.println("Receiving static request for " + Util.getCWD() + 
				config.getRootDir() + request.path);
		if(!staticController.controlStaticRequest(request)) {
			httpCodes.get("403").handleRequest(request, response);
		}
		
		byte[] data;
		
		if(mapCache.isEmpty(request.path)) {
			Path p = Paths.get(Util.getCWD() + config.getRootDir() + request.path);
			data = Files.readAllBytes(p);
			mapCache.put(request.path, new Util.ByteArray(data), 0x0, config.getCacheTime());
		} else {
			data = mapCache.get(request.path) == null ? null : mapCache.get(request.path).content;
			if(data == null) {
				Path p = Paths.get(Util.getCWD() + config.getRootDir() + request.path);
				data = Files.readAllBytes(p);
				mapCache.put(request.path, new Util.ByteArray(data), 0x0, config.getCacheTime());
			} else System.out.println("Returning cache content for " + request.path);			
		}
		Path p = Paths.get(Util.getCWD() + config.getRootDir() + request.path);
		data = Files.readAllBytes(p);
		response.sendResponse(200, "OK");
		String mimeType;
		String pathFile = request.path.split("/")[request.path.split("/").length - 1];
		String fileExtension = pathFile.split("\\.")[pathFile.split("\\.").length - 1];
		System.out.println("File of extension " + fileExtension);
		mimeType = config.getMimeType("." + fileExtension);
		response.sendHeader("Content-Type", mimeType);
		response.sendHeader("Content-Length", data.length + "");
		response.endHeaders();
		response.writeBytes(data);
		response.endResponse();
	}
	
	
	/**
	 * Overloaded static get method to add the index HTML file at the end of the request path
	 * and call the static request. Used internally.
	 * @param request The object representing the HTTPRequest. made by the client.
	 * @param response The object representing the HHTPResponse used to write the response.
	 * @param indexName The HTML index file filename.
	 */
	
	public void doStaticGet(HttpRequest request, HttpResponse response, String indexName) {
		request.path += indexName;
	try {
			doStaticGet(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a route for GET requests using a handler.
	 * @param path The server path for the route.
	 * @param handler A class implementing RequestHandler which will handle the request.
	 */
	
	public void addGetRoute(String path, RequestHandler handler) {
			getRoutes.put(path, handler);
	}
	
	/**
	 * Adds a route for POST requests using a handler.
	 * @param path The server path for the route.
	 * @param handler A class implementing RequestHandler which will handle the request.
	 */
	
	public void addPostRoute(String path, RequestHandler handler) {
			postRoutes.put(path, handler);
	}
	
	/**
	 * Internally used to parse URLEncoded data in the URL.
	 * @param url The URL string.
	 * @return The decoded URL.
	 */
	
	private static String parseURLEncoded(String url) {
		StringBuffer finalURL = new StringBuffer();
		StringBuffer encodedChar = new StringBuffer();
		boolean gettingEncoded = false;
		short encodingCount = 0;
		for(char c : url.toCharArray()) {
			if(c == '%') {
				gettingEncoded = true;
				encodedChar.append('%');
				encodingCount = 1;
				continue;
			}
			if(gettingEncoded) {
				if(encodingCount == 1){
					encodedChar.append(c);
					encodingCount = 2;
				} else {
					encodedChar.append(c);
					encodingCount = 0;
					gettingEncoded = false;
					char chr = URLEncodingReference.urlEncoded.get(encodedChar.toString().toString());
					encodedChar.setLength(0);
					finalURL.append(chr);
				}
				continue;
			}
			if(c == '+') {
				finalURL.append(' ');
				continue;
			}
			finalURL.append(c);
		}
		return finalURL.toString();
	}
	
	/**
	 * Gets the server configuration object.
	 * @return The server configuration object.
	 * @see io.github.woodenbell.blix.ServerConfig
	 */
	
	public ServerConfig getConfig() {
		return config;
	}
}
