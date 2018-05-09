package com.espiritware.opusclick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.espiritware.opusclick.service.UserService;

@Controller
@RequestMapping("/v1")
public class UserController {

	@Autowired
	private UserService userService;
	

}
