package com.dawan.cinema.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.SessionDto;
import com.dawan.cinema.services.SessionService;

/** API REST des séances. */
@RestController
@RequestMapping("/api/sessions")
public class SessionController extends CRUDController<SessionDto> {

    public SessionController(SessionService service) {
        super(service);
    }
}