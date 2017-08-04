package io.github.virtualiceshard.blix;

import java.io.IOException;

public interface RequestHandler {
	public void handleRequest(HttpRequest request, HttpResponse response) throws IOException;
}
