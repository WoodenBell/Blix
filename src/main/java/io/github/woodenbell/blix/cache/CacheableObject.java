package io.github.woodenbell.blix.cache;

import java.util.Date;
import java.util.Calendar;

public class CacheableObject<T> implements Cacheable
{
	
	private int id;
	private Date expirationDate;
	private int duration;
	private boolean resetWhenUsed;
	private T ob;
	
	public CacheableObject(int id, T ob) {
		this.id = id;
		expirationDate = null;
		duration = -1;
		resetWhenUsed = false;
		this.ob = ob;
		duration = -1;
	}
	
	public CacheableObject(int id, int duration, T ob) {
		this(id, duration, false, ob);
	}
	
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
	
	
	public boolean shouldReset() {
		return resetWhenUsed;
	}
	
	public void setExpiration(int duration) {
		this.duration = duration;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MILLISECOND, duration);
		expirationDate = cal.getTime();
	}
	
	public void resetExpiration() {
		setExpiration(duration);
	}
	
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
	
	public T get() {
		return ob;
	}
	
	public T set(T val) {
		T prev = ob;
		ob = val;
		return prev;
	}

	public int getIdentifier()
	{
		return id;
	}

}
