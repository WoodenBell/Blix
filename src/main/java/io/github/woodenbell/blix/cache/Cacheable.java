package io.github.woodenbell.blix.cache;

/**
 * Interface to cacheable objects.
 * @author WoodenBell
 * @version 0.3
 * @since 0.3
 */

public interface Cacheable {
	
	/**
	 * Returns If the object has expired or not.
	 * @return If the object has expired.
	 */
	
	public boolean isExpired();
	
	/**
	 * Returns the identifier of the object.
	 * @return The identifier of the object.
	 */
	
	public int getIdentifier();
}
