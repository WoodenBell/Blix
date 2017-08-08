package io.github.woodenbell.blix;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * The class representing the HTTPResponse to the client, used to write the response and send the headers
 * and status code.
 * @author WoodenBell
 * @version 0.3
 */

public class HttpResponse {
	
	/**
	 * The output to the client socket internally used to write responses.
	 */
	private OutputStream out;
	
	/**
	 * The HTTP response constructor internally used to create a response object.
	 * @param client The client socket that will have it's OutputStream used to write responses.
	 */
	public HttpResponse(Socket client) {
		try {
			out = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the status code followed by a message.
	 * @param code The HTTP status code of the response.
	 * @param msg The message of the HTTP status code.
	 * @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html">HTTP Status Codes</a>
	 */
	
	public void sendResponse(int code, String msg) {
			try {
				out.write(("HTTP/1.1 " + code + " " + msg +"\r\n").getBytes());
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
			System.out.println("HTTP/1.1 " + code + " " + msg);
	}
	
	/**
	 * Sends the header field and value.
	 * @param name The header field name.
	 * @param val The header field value.
	 */
	
	public void sendHeader(String name, String val) {
		    try {
				out.write((name + ": " + val + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Ends the headers and starts the response body.
	 */
	
	public void endHeaders() {
		    try {
				out.write("\r\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Writes to the response body.
	 * @param data A string that will be written in the response body.
	 */
	
	public void write(String data) {
		if(data == null) return;
		try {
			out.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes to the response body.
	 * @param data The byte array that will be written in the response body.
	 */
	
	public void writeBytes(byte[] data) {
		try {
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ends the response, closing the the OutputStream that was being used to write the response.
	 */
	
	public void endResponse() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
