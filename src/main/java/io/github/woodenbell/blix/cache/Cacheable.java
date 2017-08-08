package io.github.woodenbell.blix.cache;

public interface Cacheable {
	public boolean isExpired();
	public int getIdentifier();
}
