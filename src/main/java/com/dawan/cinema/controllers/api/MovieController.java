package com.dawan.cinema.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.MovieDto;
import com.dawan.cinema.services.MovieService;

/** API REST des films. */
@RestController
@RequestMapping("/api/movies")
public class MovieController extends CRUDController<MovieDto> {

    public MovieController(MovieService service) {
        super(service);
    }
}