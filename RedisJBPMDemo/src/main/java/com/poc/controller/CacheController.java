package com.poc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.pojos.User;
import com.poc.service.CacheService;

@RestController
@RequestMapping("/cache")
public class CacheController {
	private static final Logger logger = LogManager.getLogger(CacheController.class);
	
    @Autowired
    CacheService cacheService;
     
    @RequestMapping(value="/getCacheData/{tableName}/{customerId}")
    public String getCacheData(@PathVariable(value = "tableName")  String tableName, @PathVariable(value = "customerId")  String customerId) throws ParseException{
        logger.info("Calling cache service for key "+customerId);
        return  cacheService.getFromCache(tableName,customerId);
    }
    
    @RequestMapping(value="/getCacheDatat/{tableName}/{aadhaar}")
    public Object getCacheDatat(@PathVariable(value = "tableName")  String tableName, @PathVariable(value = "aadhaar")  String aadhaar) throws ParseException{
        logger.info("Calling cache service for key "+aadhaar);
          return cacheService.getFromCachet(tableName,aadhaar);
    }
    
    @RequestMapping(value = "/addToCache", method = RequestMethod.POST)
	public String addToCache(@RequestBody User user) {
		logger.info("Service call to store user details");
		String resultJson = null;
		try {
			boolean resultFlag = cacheService.addToCache(user.getClass().getName(), user.getPhone(), user, 30l);
			ObjectMapper objectMapper = new ObjectMapper();
			if (resultFlag)
				user.setResponse("ack");
			else
				user.setResponse("nack");
			resultJson = objectMapper.writeValueAsString(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}
	
    @RequestMapping(value = "/addToCachet", method = RequestMethod.POST)
	public String addToCachet(@RequestBody String requestBody) {
		logger.info("Service call to store Customer details");
		String result = "ack";
		try {
			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject) parser. parse(requestBody);
            String hasKey = (String)json.get("aadhaar");
			boolean resultFlag = cacheService.addToCache("CustomerData", hasKey, requestBody, 30l);
			if (!resultFlag)
				result = "nack";

		} catch (Exception e) {
			result = "nack";
			e.printStackTrace();
		}
		return result;
	}
    
	@RequestMapping(value = "/updateCache/{hashkey}", method = RequestMethod.POST)
	public String updateCache(@PathVariable(value = "hashkey") String hashkey, @RequestBody User user) {
		logger.info("Service call to store updated details");
		String resultJon = null;
		ObjectMapper objectMapper = null;
		try {
			cacheService.updateCache(user.getClass().getName(), hashkey, user);
			objectMapper = new ObjectMapper();
			user.setResponse("ack");
			resultJon = objectMapper.writeValueAsString(user);
		} catch (Exception e) {
			user.setResponse("nack");
			e.printStackTrace();
		}

		return resultJon;
	}
	
	@RequestMapping(value = "/updateCachet/{hashkey}", method = RequestMethod.POST)
	public String updateCachet(@PathVariable(value = "hashkey") String hashkey, @RequestBody String requestBody) {
		logger.info("Service call to store updated details");
		String result = "ack";
		try {
			cacheService.updateCache("CustomerData", hashkey, requestBody);
		} catch (Exception e) {
			result = "nack";
			e.printStackTrace();
		}
		return result;
	}
}
