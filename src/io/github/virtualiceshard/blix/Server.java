package io.github.virtualiceshard.blix;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public class Server {
	private ServerSocket server;
	private boolean serverRunning;
	private HashMap<String, RequestHandler> getRoutes;
	private HashMap<String, RequestHandler> postRoutes;
	private static HashMap<String, RequestHandler> errors;
	public Server() {
		int port = ServerConfig.getPort();
		getRoutes = new HashMap<String, RequestHandler>();
		postRoutes = new HashMap<String, RequestHandler>();
		try {
		server = new ServerSocket(port);
		} catch(IOException e) {
			System.out.println("IOException ocurred while starting server, aborting...");
			serverRunning = false;
			System.exit(-1);
		}
		errors = new HashMap<String, RequestHandler>();
		errors.put("404", new RequestHandler() {
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
	}
	public void startServer() {
			System.out.println("Server started at port " + ServerConfig.getPort());
			serverRunning = true;
			handleLoop();
	}
	public void handleNewClient(Socket client) {
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
						errors.get("500").handleRequest(new HttpRequest("", "", new HashMap<String, String>()),
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

	private void handleRequest(String requestData, Socket client, InputStream rd) throws IOException {
		System.out.println("Handling new request");
		HashMap<String, String> reqDict = new HashMap<String, String>();
		String[] reqData = requestData.split("\n");
		for(int i = 0; i < reqData.length; i++) {
			if(i != 0) {
				reqDict.put(reqData[i].split(":")[0], reqData[i].split(":")[1].trim());
			}
		}
			HashMap<String, ArrayBoxing.IntArray> form = null;
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
			String path = parseURLEncoded(reqMethStr[1]);
			HttpRequest req = new HttpRequest(path, method, reqDict, form);
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
								errors.get("404").handleRequest(req, res);
							}
						} else {
							File f3 = new File(ServerConfig.getRootDir() + path + "/index.htm");
							if(f3.exists()) {
								if(f3.isFile()) {
									System.out.println("index.htm found");
									doStaticGet(req, res, "index.htm");
							} else {
								errors.get("404").handleRequest(req, res);
							}
						} else {
							errors.get("404").handleRequest(req, res);
						}
				}
			} 
			if(f.isFile()) {
				System.out.println("Path is file. Doing static request");
				doStaticGet(req, res);
			}
				} else {
					errors.get("404").handleRequest(req, res);
				}
			}
			}
			if(method.equals("POST")) {
				try {
					System.out.println("Post request");
					postRoutes.get(path).handleRequest(req, res);
				} catch(NullPointerException e) {
					e.printStackTrace();
					errors.get("404").handleRequest(req, res);
				}
		}
		
	}
	public void doStaticGet(HttpRequest request, HttpResponse response) throws IOException {
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
	private void showHtmlCode(String file, HttpResponse res) throws IOException {
		byte[] data;
		Path p = Paths.get("./res/HtmlCodePages/" + file);
		data = Files.readAllBytes(p);
		res.sendHeader("Content-Type", ServerConfig.getMimeType(".html"));
		res.sendHeader("Content-Length", data.length + "");
		res.endHeaders();
		res.writeBytes(data);
	}
public void doStaticGet(HttpRequest request, HttpResponse response, String indexName) {
		request.path += indexName;
	try {
			doStaticGet(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addGetRoute(String path, RequestHandler route) {
			getRoutes.put(path, route);
	}
	public void addPostRoute(String path, RequestHandler route) {
			postRoutes.put(path, route);
}
	public static String parseURLEncoded(String url) {
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
