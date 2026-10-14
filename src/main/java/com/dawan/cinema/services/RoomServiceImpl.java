package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dawan.cinema.dtos.RoomDto;
import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.entities.Room;
import com.dawan.cinema.exceptions.ResourceNotFoundException;
import com.dawan.cinema.mappers.RoomMapper;
import com.dawan.cinema.repositories.CinemaRepository;
import com.dawan.cinema.repositories.RoomRepository;

@Service 
public class RoomServiceImpl extends CRUDServiceImpl<Room, RoomDto, RoomRepository> implements RoomService {

    private final CinemaRepository cinemaRepository;

    public RoomServiceImpl(RoomRepository repository, RoomMapper mapper, CinemaRepository cinemaRepository) {
        super(repository, mapper, Room.class);
        this.cinemaRepository = cinemaRepository;
    }
    
    @Override
    protected Page<Room> searchByKeyword(String keyword, Pageable pageable) {
        return repository.search(keyword, pageable);
    }

    @Override 
    protected void resolveRelations(Room room, RoomDto dto) {
        Cinema cinema = cinemaRepository.findById(dto.cinemaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cinema", dto.cinemaId()));
        room.setCinema(cinema);
    }
}
