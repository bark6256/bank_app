package com.tencoding.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	// localhost:80/main-page
	@GetMapping("main-page")
	public String mainPage() {
		return "layout/main";
	}
}
