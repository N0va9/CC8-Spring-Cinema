package com.dawan.cinema.mappers;

import org.springframework.stereotype.Component;

import com.dawan.cinema.dtos.RoomDto;
import com.dawan.cinema.entities.Room;

/**
 * Conversion Room <-> RoomDto.
 */
@Component
public class RoomMapper implements Mapper<Room, RoomDto> {

    @Override
    public RoomDto toDto(Room room) {
        return new RoomDto(room.getId(), room.getCinema().getId(), room.getCinema().getNom(),
                room.getNumero(), room.getCapacite(), room.getDateConstr());
    }

    @Override
    public Room toEntity(RoomDto dto) {
        Room room = new Room();
        updateEntityFromDto(room, dto);
        return room;
    }

    @Override
    public void updateEntityFromDto(Room room, RoomDto dto) {
        room.setNumero(dto.numero());
        room.setCapacite(dto.capacite());
        room.setDateConstr(dto.dateConstr());
    }
}