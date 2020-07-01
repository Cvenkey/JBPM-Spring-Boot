package com.poc.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CacheServiceImpl implements CacheService{

    private static final Logger logger = LogManager.getLogger(CacheServiceImpl.class);
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    private HashOperations<String,Object,Object> hashOperations;

    @PostConstruct
    public void initHashOperations(){
    	logger.info("Initialize Hash Operations");
    	//redisTemplate.exp
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public String getFromCache(String tableName, String hashKey){
        String cacheData = null;
        try {
            if (hashOperations.hasKey(tableName, hashKey)) {
                Object cacheObject = hashOperations.get(tableName, hashKey);
                ObjectMapper objectMapper = new ObjectMapper();
                cacheData = objectMapper.writeValueAsString(cacheObject);
                logger.info("Data from cache :"+cacheData);
            }else{
                logger.warn("No data found in cache for table"+tableName+" and key "+hashKey);

            }
        }catch (Exception ex){
            logger.error(ex.getClass().getName()+" raised while getting cache data");
            ex.printStackTrace();
        }
        return  cacheData;
    }
    
    @Override
    public String getFromCachet(String tableName, String hashKey){
    	String cacheData = null;
        try {
            if (hashOperations.hasKey(tableName, hashKey)) {
               cacheData = (String) hashOperations.get(tableName, hashKey);
                logger.info("Data from cache :"+cacheData);
            }else{
                logger.warn("No data found in cache for table"+tableName+" and key "+hashKey);

            }
        }catch (Exception ex){
            logger.error(ex.getClass().getName()+" raised while getting cache data");
            ex.printStackTrace();
        }
        return  cacheData;
    }

    @Override
	public boolean addToCache(String tableName, String hashKey, Object hashValue, Long minutesToLive) {
    	boolean resultFlag;
    	resultFlag =hashOperations.putIfAbsent(tableName, hashKey, hashValue);
    	redisTemplate.expire(hashKey, 45, TimeUnit.SECONDS);
		return resultFlag;
	}
       
    @Override
    public void updateCache(String tableName, String hashKey, Object hashValue){
        hashOperations.put(tableName,hashKey,hashValue);
        redisTemplate.expire(hashKey, 45, TimeUnit.SECONDS);
    }
}
