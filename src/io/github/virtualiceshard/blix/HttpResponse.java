package io.github.virtualiceshard.blix;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpResponse {
	private OutputStream out;
	public HttpResponse(Socket client) {
		try {
			out = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendResponse(int code, String msg) {
			try {
				out.write(("HTTP/1.1 " + code + " " + msg +"\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("HTTP/1.1 " + code + " " + msg);
	}
	public void sendResponse(int code, String msg, String httpVer) {
		try {
			out.write(("HTTP/" + httpVer + " " + code + " " + msg +"\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("HTTP/" + httpVer + " " + code + " " + msg);
}

	public void sendHeader(String name, String val) {
		    try {
				out.write((name + ": " + val + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public void endHeaders() {
		    try {
				out.write("\r\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public void write(String s) {
		if(s == null) return;
		try {
			out.write(s.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void endResponse() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeBytes(byte[] data) {
		try {
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
