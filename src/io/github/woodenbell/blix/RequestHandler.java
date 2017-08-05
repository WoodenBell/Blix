package io.github.woodenbell.blix;

import java.io.IOException;

public interface RequestHandler {
	public void handleRequest(HttpRequest request, HttpResponse response) throws IOException;
}
