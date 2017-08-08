package io.github.woodenbell.blix.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MapCacheManager<T>
{
	private HashMap<String, CacheableObject<T>> mapCache;
	
	private boolean runCleaner;
	
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
	
	public CacheableObject<T> remove(String key) {
		return mapCache.remove(key);
	}
	
	public CacheableObject<T> put(String key, T val, int id, int duration) {
		return mapCache.put(key, new CacheableObject<T>(id, duration, val));
	}
	
	public CacheableObject<T> put(String key, T val, int id, int duration, boolean resetWhenUsed) {
		return mapCache.put(key, new CacheableObject<T>(id, duration, resetWhenUsed, val));
	}
	
	public CacheableObject<T> putPermanent(String key, T val, int id) {
		return mapCache.put(key, new CacheableObject<T>(id, val));
	}
	
	
	
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
	
	
	
	public int getId(String key) {
		CacheableObject<T> r = mapCache.get(key);
		if(r == null) return -1;
		return r.getIdentifier();
	}
	
	public boolean isEmpty(String key) {
		return mapCache.get(key) == null;
}
}
