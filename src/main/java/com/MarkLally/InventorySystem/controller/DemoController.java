package com.MarkLally.InventorySystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {
	

	@RequestMapping("/helloTest")
	public @ResponseBody String home() {
		return "Hello World";
	}
	
}
