
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.woodenbell.blix.HttpRequest;
import io.github.woodenbell.blix.HttpResponse;
import io.github.woodenbell.blix.RequestHandler;
import io.github.woodenbell.blix.Server;
import io.github.woodenbell.blix.ServerConfig;


public class Main {

	public static void main(String[] args) {
			defaultServerConfigLoader();
			Server blix = new Server();
			defaultRouteLoader(blix);
			blix.startServer();
	}
	public static void defaultRouteLoader(Server s) {
		s.addGetRoute("/test", new RequestHandler(){
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				response.sendResponse(200, "OK");
				response.sendHeader("Content-Type", ServerConfig.getMimeType(".html"));
				response.endHeaders();
				response.write("<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "<title>test</title>"
						+ "</head>"
						+ "<body>"
						+ "<p>hello</p>"
						+ "</body>"
						+ "</html>");
				response.endResponse();
			}
		});
		s.addPostRoute("/post_handler", new RequestHandler() {
			public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
				response.sendResponse(200, "OK");
				response.sendHeader("Content-Type", ServerConfig.getMimeType(".html"));
				response.endHeaders();
				response.write("<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "<meta charset=\"UTF-8\" />"
						+ "<title>test</title>"
						+ "</head>"
						+ "<body>"
						+ "<p>Username: " + request.getFormValue("username") + "</p>"
						+ "<p>Password: " + request.getFormValue("pass") + "</p>"
						+ "<p>wow: " + request.getFormValue("wow") + "</p>"
						+ "<p>agree: " + request.getFormValue("agree") + "</p>"
						+ "<p>like potatoes: " + request.getFormValue("potatoes") + "</p>"
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
				response.sendHeader("Content-Type", ServerConfig.getMimeType(".html"));
				response.endHeaders();
				response.write("File uploaded");
				response.write("<br /> Name:" + request.getFormValue("name"));
				response.write("<br /> Name2:" + request.getFormValue("name2"));
				response.write("<br /> Name3:" + request.getFormValue("name3"));
				response.endResponse();
			}
		});
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
