package io.github.woodenbell.blix.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author WoodenBell
 * @version 0.3
 * @param <T> The type of the cached object.
 */

public class MapCacheManager<T>
{
	/**
	 * The HashMap cache object.
	 */
	private HashMap<String, CacheableObject<T>> mapCache;
	
	/**
	 * If the expired cleaner thread must run.
	 */
	
	private boolean runCleaner;
	
	
	/**
	 * Creates an empty HashMap cache and starts the thread responsible for deleting expired objects.
	 */
	
	public MapCacheManager() {
		runCleaner = true;
		mapCache = new HashMap<>();
		Thread cleaner = new Thread(new Runnable() {
			public void run() {
				while(runCleaner) {
					if(mapCache.isEmpty()) continue;
					Set<String> keySet = mapCache.keySet();
					Iterator<String> iter = keySet.iterator();
					while(iter.hasNext()) {
						Object ob = iter.next();
						Cacheable c =  mapCache.get((String) ob);
						if(c.isExpired()) mapCache.remove((String) ob);
					}
				}
			}
		});
		cleaner.start();
	}
	
	protected void finalize() {
		runCleaner = false;
	}
	
	/**
	 * Removes the cached object by it's key.
	 * @param key The key of the object.
	 * @return The removed object.
	 */
	
	public CacheableObject<T> remove(String key) {
		return mapCache.remove(key);
	}
	
	/**
	 * Caches the object with an id, key and duration.
	 * @param key The object cache key.
	 * @param val The object to be stored.
	 * @param id The object id (for utility purposes, but can be anything).
	 * @param duration The duration of the object in the cache.
	 * @return The previous cache value.
	 */
	
	public CacheableObject<T> put(String key, T val, int id, int duration) {
		return mapCache.put(key, new CacheableObject<T>(id, duration, val));
	}
	
	/**
	 * Caches the object with an id, key and duration, a1lso specifies if the expiration time
	 * will be reset every time the object is accessed in the cache.
	 * @param key The object cache key.
	 * @param val The object to be stored.
	 * @param id The object id (for utility purposes, but can be anything).
	 * @param duration The duration of the object in the cache.
	 * @param resetWhenUsed If the expiration time should reset when the object is accessed.
	 * @return The previous cache value.
	 */
	
	public CacheableObject<T> put(String key, T val, int id, int duration, boolean resetWhenUsed) {
		return mapCache.put(key, new CacheableObject<T>(id, duration, resetWhenUsed, val));
	}
	
	/**
	 * Puts a permanent cache in the slot (never expires).
	 * @param key The key of the slot.
	 * @param val The object.
	 * @param id The object id (for utility purposes, can be anything).
	 * @return The previous object.
	 */
	
	public CacheableObject<T> putPermanent(String key, T val, int id) {
		return mapCache.put(key, new CacheableObject<T>(id, val));
	}
	
	
	/**
	 * Returns the cached object corresponding to the slot of the given key, or null if empty or expired.
	 * @param key The key of the slot.
	 * @return The object or null if empty or expired.
	 */
	
	public T get(String key) {
		CacheableObject<T> result = mapCache.get(key);
		if(result == null) {
			return null;
		} else {
			if(result.isExpired()) {
				mapCache.remove(key);
				return null;
			} else {
				if(result.shouldReset()) result.resetExpiration();
				return result.get();
			}
		}
		
	}
	
	/**
	 * Returns the id of the object stored in the key.
	 * @param key The key of the object which id will be returned.
	 * @return The object id or 0, if not found.
	 */
	
	public int getId(String key) {
		CacheableObject<T> r = mapCache.get(key);
		if(r == null) return 0x0;
		return r.getIdentifier();
	}
	
	/**
	 * Returns whether or not the slot of key is empty.
	 * @param key The key of the slot.
	 * @return If the slot is empty.
	 */
	
	public boolean isEmpty(String key) {
		return mapCache.get(key) == null;
	}
	
}
