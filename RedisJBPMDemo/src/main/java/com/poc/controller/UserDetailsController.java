package com.poc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.jbpm.JBPMStarter;
import com.poc.pojos.User;
import com.poc.service.CacheService;

@Controller
@RequestMapping("/user")
public class UserDetailsController {
    
	private static final Logger logger = LogManager.getLogger(UserDetailsController.class);
	@Autowired
	JBPMStarter jbpmStarter;
	
    @Autowired
    CacheService cacheService;
    
    private User newUser;
    private String processId;
	
    @RequestMapping(value="/userRegister")
	public String userRegister(Model model){
    	logger.info("Display register view");
    	newUser  = new User();
    	model.addAttribute("newUser", newUser);
		return "register.html";
	}
    
	@RequestMapping(value="/validateUser")
	public String validateUser(@ModelAttribute("newUser") User newUser, Model model) { //
		logger.info("Validating user details");
		processId = "validateUserProcessFlow";
		boolean navigateToNext = jbpmStarter.startJbpmProcess(newUser, processId);
		if (navigateToNext)
			return "enterDetails.html";
		else
			return "register.html";
	}
	
	@RequestMapping(value="/updateUser")
	public String updateUser(@ModelAttribute("newUser") User newUser,Model model) { 
		logger.info("prompt for update user details");
		processId = "userDetailsProcessFlow";
	    jbpmStarter.startJbpmProcess(newUser, processId);
	    newUser = new User();
	    model.addAttribute("newUser", newUser);
	    return "register.html";
	}
	
	@RequestMapping(value="/getUser/{phone}")
	public String getUser(@PathVariable("phone") String phone,Model model) { 
		logger.info("User acknowledgement");
		String cacheData = cacheService.getFromCache("com.poc.pojos.User", phone);
		ObjectMapper mapper = new ObjectMapper();
		try {
			User savedUser = mapper.readValue(cacheData, User.class);
			model.addAttribute("savedUser", savedUser);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "acknowledge.html";
	}
}
