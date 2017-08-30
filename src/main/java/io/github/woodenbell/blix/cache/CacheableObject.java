package io.github.woodenbell.blix.cache;

import java.util.Date;
import java.util.Calendar;

/**
 * Wrapper for cached objects.
 * @author WoodenBell
 * @param <T> The type of the wrapped object.
 * @version 0.3
 * @since 0.3
 */

public class CacheableObject<T> implements Cacheable
{
	
	/**
	 * The id of the object (can be used by the user).
	 */
	
	private int id;
	
	/**
	 * The date of the expiration of the object. 
	 * Null if the object is permanent.
	 */
	
	private Date expirationDate;
	
	/**
	 * The duration of the object in milliseconds.
	 */
	
	private int duration;
	
	/**
	 * If the expiration time must be reset when the object is accessed.
	 */
	
	private boolean resetWhenUsed;
	
	/**
	 * The wrapped object.
	 */
	
	private T ob;
	
	
	/**
	 *Constructor for permanent objects in cache.
	 * @param id The object's identifier.
	 * @param ob The object to be wrapped.
	 */
	
	public CacheableObject(int id, T ob) {
		this.id = id;
		expirationDate = null;
		duration = -1;
		resetWhenUsed = false;
		this.ob = ob;
		duration = -1;
	}
	
	/**
	 * Constructor with duration of the object in cache.
	 * @param id The object's identifier.
	 * @param duration The duration of the object in milliseconds.
	 * @param ob The object to be wrapped.
	 */
	
	public CacheableObject(int id, int duration, T ob) {
		this(id, duration, false, ob);
	}
	
	
	/**
	 * Constructor with duration and specification to reset or not the expiration time when object is accessed.
	 * @param id The object's identifier.
	 * @param duration The duration of the object in milliseconds.
	 * @param resetWhenUsed If the duration should or not reset when the object is accessed.
	 * @param ob The object to be wrapped.
	 */
	
	public CacheableObject(int id, int duration, boolean resetWhenUsed, T ob) {
		this.id = id;
		this.ob = ob;
		this.duration = duration;
		this.resetWhenUsed = resetWhenUsed;
		expirationDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(expirationDate);
		cal.add(Calendar.MILLISECOND, duration);
		expirationDate = cal.getTime();
	}
	
	/**
	 * Returns if object's expiration time should or not reset when accessed.
	 * @return If the expiration time should be reset.
	 */
	
	public boolean shouldReset() {
		return resetWhenUsed;
	}
	
	/**
	 * Sets the expiration time of the object.
	 * @param duration The duration of the object in milliseconds.
	 */
	
	public void setExpiration(int duration) {
		this.duration = duration;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MILLISECOND, duration);
		expirationDate = cal.getTime();
	}
	
	/**
	 * Resets the expiration time of the object.
	 */
	
	public void resetExpiration() {
		setExpiration(duration);
	}
	
	/**
	 * Returns if the object has expired or not.
	 * @return If the object has expired.
	 */
	
	public boolean isExpired() {
		if(expirationDate == null) {
			return false;
		} else {
			if(new Date().before(expirationDate)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Returns the object.
	 * @return The wrapped object.
	 */
	
	public T get() {
		return ob;
	}
	
	/**
	 * Sets the object.
	 * @param val The new wrapped object.
	 * @return The previous wrapped object.
	 */
	
	public T set(T val) {
		T prev = ob;
		ob = val;
		return prev;
	}

	/**
	 * Returns the object identifier.
	 * @return The wrapped object identifier.
	 */
	
	public int getIdentifier()
	{
		return id;
	}

}
