package com.dawan.cinema.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
		
	@GetMapping({"","/"})
	public String Accueil() {
		return "index";
	}
		

}
