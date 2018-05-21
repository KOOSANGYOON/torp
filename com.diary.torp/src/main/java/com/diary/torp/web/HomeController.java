package com.diary.torp.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.diary.torp.service.UserService;
import com.diary.torp.web.HomeController;

@Controller
public class HomeController {
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Resource(name = "userService")
	private UserService userService;
	
	@GetMapping("/")
	public String home() {
		log.debug("home controller in");
		return "main";
	}
}
