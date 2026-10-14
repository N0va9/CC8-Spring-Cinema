package com.dawan.cinema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.services.CinemaServiceImpl;

@RestController
@RequestMapping("/cinema")
public class CinemaController extends CRUDController<Cinema, CinemaDto, Long>{
	public CinemaController(CinemaServiceImpl service) {
		super(service);
	}
}
