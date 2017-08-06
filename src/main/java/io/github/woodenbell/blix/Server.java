package io.github.woodenbell.blix;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

/**
 * The class that represents the server and is responsible for receiving the requests and passing
 * then to the handlers or static files. Also is responsible for responding with HTML error codes
 * if any of them occur.
 * @author WoodenBell
 * @version 0.1
 */

public class Server {
	
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
	
	private static HashMap<String, RequestHandler> htmlCodes;
	
	/**
	 * The server constructor. Initializes the HTTP codes HashMap that will be used
	 * internally when an error occurs.
	 */
	
	public Server() {
		int port = ServerConfig.getPort();
		getRoutes = new HashMap<String, RequestHandler>();
		postRoutes = new HashMap<String, RequestHandler>();
		try {
		server = new ServerSocket(port);
		} catch(IOException e) {
			System.out.println("IOException ocurred while starting server, aborting...");
			e.printStackTrace();
			serverRunning = false;
			System.exit(-1);
		}
		htmlCodes = new HashMap<String, RequestHandler>();
		htmlCodes.put("404", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) {
				response.sendResponse(404, "Not Found");
				try {
					showHtmlCode("404.html", response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				response.endResponse();
			}
		});
		
		htmlCodes.put("500", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				response.sendResponse(500, "Internal Server Error");
				try {
					showHtmlCode("500.html", response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				response.endResponse();
			}
			
		});
	}
	
	/**
	 * Starts the server loop and prints the used port to the console.
	 * Each new client socket will be accepted in the server loop and handled  in
	 * a separated Thread
	 */
	
	public void startServer() {
			System.out.println("Server started at port " + ServerConfig.getPort());
			serverRunning = true;
			handleLoop();
	}
	
	/**
	 * Sets the handler for a HTTP status code.
	 * @param code An integer representing the HTTP code
	 * @param handler A RequestHandler responsible for responding with the code.
	 */
	
	public void setCodeHandler(int code, RequestHandler handler) {
		htmlCodes.put(code + "", handler);
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
						htmlCodes.get("500").handleRequest(new HttpRequest("", "", new HashMap<String, String>()),
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
					//System.out.print((char) c);
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
			HashMap<String, ParsedFormData> getURLVars = null;
			String path;
			if(reqMethStr[1].contains("?")) {
				String urlVars = reqMethStr[1].split("\\?")[1];
				int[] tempData = new int[reqMethStr[1].split("\\?")[1].length()];
				for(int i = 0; i < tempData.length; i++) {
					tempData[i] = (int) urlVars.charAt(i);
				}
				getURLVars = FormParser.parseFormURLEncoded(tempData);
				path = parseURLEncoded(reqMethStr[1].split("\\?")[0]);
			} else {
				path = parseURLEncoded(reqMethStr[1]);
			}
			
			HttpRequest req = new HttpRequest(path, method, reqDict, form);
			req.setUrlData(getURLVars);
			HttpResponse res = new HttpResponse(client);
			
			File f = new File(ServerConfig.getRootDir() + path);
			if(method.equals("GET")) {
				try {
					System.out.println("Get request");
					getRoutes.get(path).handleRequest(req, res);
				} catch(NullPointerException e) {
					if(f.exists()) {
						if(f.isDirectory()) {
							System.out.println("Path is a directory. Checking for index files");
							File f2 = new File(ServerConfig.getRootDir() + path + "/index.html");
							if(f2.exists()) {
								if(f2.isFile()) {
									System.out.println("index.html found");
									doStaticGet(req, res, "index.html");
							} else {
								htmlCodes.get("404").handleRequest(req, res);
							}
						} else {
							File f3 = new File(ServerConfig.getRootDir() + path + "/index.htm");
							if(f3.exists()) {
								if(f3.isFile()) {
									System.out.println("index.htm found");
									doStaticGet(req, res, "index.htm");
							} else {
								htmlCodes.get("404").handleRequest(req, res);
							}
						} else {
							htmlCodes.get("404").handleRequest(req, res);
						}
				}
			} 
			if(f.isFile()) {
				System.out.println("Path is file. Doing static request");
				doStaticGet(req, res);
			}
				} else {
					htmlCodes.get("404").handleRequest(req, res);
				}
			}
			}
			if(method.equals("POST")) {
				try {
					System.out.println("Post request");
					if(postRoutes.get(path) == null) {
						htmlCodes.get("404").handleRequest(req, res);
					} else {
						postRoutes.get(path).handleRequest(req, res);
					}
				} catch(Exception e) {
						e.printStackTrace();
						htmlCodes.get("500").handleRequest(req, res);
				} 
		}
		
	}
	
	/**
	 * Internally used to respond with static content.
	 * @param request The object representing the HTTPRequest. made by the client.
	 * @param response The object representing the HHTPResponse used to write the response.
	 * @throws IOException Errors while reading the file data.
	 * @see IOException.
	 */
	
	private void doStaticGet(HttpRequest request, HttpResponse response) throws IOException {
		byte[] data;
		Path p = Paths.get(ServerConfig.getRootDir() + request.path);
		data = Files.readAllBytes(p);
		response.sendResponse(200, "OK");
		String mimeType;
		String pathFile = request.path.split("/")[request.path.split("/").length - 1];
		String fileExtension = pathFile.split("\\.")[pathFile.split("\\.").length - 1];
		System.out.println("File of extension " + fileExtension);
		mimeType = ServerConfig.getMimeType("." + fileExtension);
		response.sendHeader("Content-Type", mimeType);
		response.sendHeader("Content-Length", data.length + "");
		response.endHeaders();
		response.writeBytes(data);
		response.endResponse();
	}
	
	/**
	 * Internally used to respond with a HTTP code response message.
	 * @param file The HTML file name.
	 * @param res The HTTPResponse object used to write the file content.
	 * @throws IOException Either errors while reading the file or errors while writing the response.
	 * @see IOException
	 */
	private void showHtmlCode(String file, HttpResponse res) throws IOException {
		byte[] data;
		Path p = Paths.get("src/main/java/io/github/woodenbell/blix/" + file);
		data = Files.readAllBytes(p);
		res.sendHeader("Content-Type", ServerConfig.getMimeType(".html"));
		res.sendHeader("Content-Length", data.length + "");
		res.endHeaders();
		res.writeBytes(data);
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
					char chr = EncodingReference.urlEncoded.get(encodedChar.toString().toString());
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
}
