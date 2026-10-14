package com.dawan.cinema.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.services.CinemaService;


@RestController
@RequestMapping("/api/cinemas")
public class CinemaController extends CRUDController<CinemaDto> {

    public CinemaController(CinemaService service) {
        super(service);
    }
}
