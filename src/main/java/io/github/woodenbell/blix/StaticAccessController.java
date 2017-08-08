package io.github.woodenbell.blix;

/**
 * Interface for the static request controller.
 * @author WoodenBell
 * @version 0.3
 * @since 0.3
 */

public interface StaticAccessController {
	
	/**
	 * Controls the static request.
	 * @param req The HttpRequest object representing the static request.
	 * @see io.github.woodenbell.blix.HttpRequest
	 * @return True if access is allowed, or false if the access is forbidden.
	 */
	
	public boolean controlStaticRequest(HttpRequest req);
}
