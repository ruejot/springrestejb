package org.wildfly.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {

	@RequestMapping("/")
	public String HelloRoot() {
		return "Open main page";
	}
	
	@GetMapping("/testing")
	public String getTestEJBReloadMemoryCenter() throws Exception {
		return "helloworld_springrestejb";
	}
	
	@RequestMapping("/MyPage")
	public String showPage(@RequestParam(name="title", required=false,
	defaultValue="MyPage")String title, Model model) {
		model.addAttribute("name", title);
		return "index";
	}
}
