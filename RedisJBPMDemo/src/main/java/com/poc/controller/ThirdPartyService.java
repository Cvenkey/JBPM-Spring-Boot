package com.poc.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.pojos.User;

@RestController
@RequestMapping("/thirdParty")
public class ThirdPartyService {

	private static final Logger logger = LogManager.getLogger(ThirdPartyService.class);
	
	@RequestMapping(value="/getUser/{phone}")
	public String getUserByPhone(@PathVariable(value = "phone") String phone) { 
		logger.info("Check user existence with pbone");
		User user = getUsers().get(phone);
		String status = (user==null? "ACK":"NACK");
		return status;
	}
	
	private Map<String,User> getUsers(){
		Map<String, User> users = new HashMap<>();
		User user1 = new User();
		user1.setPhone("9876543210");
		user1.setFirstName("Robert");
		user1.setAddress("STL");
		
		User user2 = new User();
		user2.setPhone("9876543211");
		user2.setFirstName("James");
		user2.setAddress("STL");
		users.put("9876543210",user2);
		users.put("9876543211",user2);
		return users;
	}
}
