import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.woodenbell.blix.HttpRequest;
import io.github.woodenbell.blix.HttpResponse;
import io.github.woodenbell.blix.RequestHandler;
import io.github.woodenbell.blix.Server;



public class Main {

	public static void main(String[] args) {
			final int port = 8008;
			Server blix = new Server(port);
			defaultServerConfigLoader(blix);
			defaultRouteLoader(blix);
			blix.startServer();
	}
	public static void defaultRouteLoader(Server s) {
		s.addGetRoute("/test", new RequestHandler(){
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				response.sendResponse(200, "OK");
				response.sendHeader("Content-Type", s.getConfig().getMimeType(".html"));
				response.endHeaders();
				response.write("<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "<title>test</title>"
						+ "</head>"
						+ "<body>"
						+ "<p>hello</p>"
						+ (request.getQueryStringValue("hello") != null ? request.getQueryStringValue("hello") : "")
						+ "<br />"
						+ (request.getQueryStringValue("there") != null ? request.getQueryStringValue("there") : "")
						+ "</body>"
						+ "</html>");
				response.endResponse();
			}
		});
		s.addPostRoute("/post_handler", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				response.sendResponse(200, "OK");
				response.sendHeader("Content-Type", s.getConfig().getMimeType(".html"));
				response.endHeaders();
				response.write("<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "<meta charset=\"UTF-8\" />"
						+ "<title>test</title>"
						+ "</head>"
						+ "<body>"
						+ "<p>Username: " + request.getFormValueString("username") + "</p>"
						+ "<p>Password: " + request.getFormValueString("pass") + "</p>"
						+ "<p>wow: " + request.getFormValueString("wow") + "</p>"
						+ "<p>agree: " + request.getFormValueString("agree") + "</p>"
						+ "<p>like potatoes: " + request.getFormValueString("potatoes") + "</p>"
						+ "</body>"
						+ "</html>");
				response.endResponse();
			}
		});
		s.addPostRoute("/post_handler2", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				int[] fileData = request.getFormValueInt("myfile");
				File f = new File("./ServerDir/upload/" + request.getFormValueFileName("myfile"));
				System.out.println("Uploading file: " + request.getFormValueFileName("myfile"));
				System.out.println("Content type: " + request.getFormValueContentType("myfile"));
				f.createNewFile();
				FileOutputStream fout = new FileOutputStream(f);
					for(int i : fileData) {
						fout.write(i);
					}
				System.out.println("File sucefully uploaded");
				fout.close();
				response.sendResponse(200, "OK");
				response.sendHeader("Content-Type", s.getConfig().getMimeType(".html"));
				response.sendHeader("Connection", "closed");
				response.endHeaders();
				response.write("File uploaded");
				response.write("<br /> Name:" + request.getFormValueString("name"));
				response.write("<br /> Name2:" + request.getFormValueString("name2"));
				response.endResponse();
			}
		});
	}
	public static void defaultServerConfigLoader(Server s) {
		s.getConfig().setUseCache(true);
		s.getConfig().setCacheTime(10000);
		s.getConfig().setRootDir("./ServerDir");
		s.getConfig().setMimeType(".html", "text/html");
		s.getConfig().setMimeType(".css", "text/css");
		s.getConfig().setMimeType(".txt", "text/plain");
		s.getConfig().setMimeType(".js", "text/javascript");
		s.getConfig().setMimeType(".json", "application/json");
		s.getConfig().setMimeType(".jpg", "image/jgp");
		s.getConfig().setMimeType(".png", "image/png");
		s.getConfig().setMimeType(".ico", "image/x-icon");
	}
	//
}
