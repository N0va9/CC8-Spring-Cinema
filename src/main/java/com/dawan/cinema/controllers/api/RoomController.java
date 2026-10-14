package com.dawan.cinema.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.RoomDto;
import com.dawan.cinema.services.RoomService;

/** API REST des salles. */
@RestController
@RequestMapping("/api/rooms")
public class RoomController extends CRUDController<RoomDto> {

    public RoomController(RoomService service) {
        super(service);
    }
    
}
