package io.github.woodenbell.blix;

import java.io.IOException;

/**
 * The interface for the request handler. Any class that want to handle the requests received by the server
 * must implement this interface.
 * @author WoodenBell
 * @version 0.3
 */

public interface RequestHandler {
	
	/**
	 * The method that receives the request and response objects and is responsible for handling the 
	 * request and responding to it properly.
	 * @param request The object representing the client HTTPRequest.
	 * @see io.github.woodenbell.blix.HttpRequest
	 * @param response The object representing the HTTPResponse.
	 * @see io.github.woodenbell.blix.HttpResponse
	 * @throws IOException Any exceptions that may occur while writing a response.
	 * @see IOException
	 */
	
	public void handleRequest(HttpRequest request, HttpResponse response) throws IOException;
}
