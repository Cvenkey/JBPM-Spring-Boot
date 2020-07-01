package com.poc.service;

public interface CacheService {

	boolean addToCache(String tableName, String hashKey, Object hashValue, Long minutesToLive);
	
	void updateCache(String tableName, String hashKey, Object hashValue);

	String getFromCache(String tableName, String hashKey);

	String getFromCachet(String tableName, String hashKey);

}
