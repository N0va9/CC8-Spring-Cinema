package com.dawan.cinema.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dawan.cinema.dtos.SessionDto;
import com.dawan.cinema.entities.Movie;
import com.dawan.cinema.entities.Room;
import com.dawan.cinema.entities.Schedule;
import com.dawan.cinema.entities.Session;
import com.dawan.cinema.exceptions.BusinessException;
import com.dawan.cinema.exceptions.ResourceNotFoundException;
import com.dawan.cinema.mappers.SessionMapper;
import com.dawan.cinema.repositories.MovieRepository;
import com.dawan.cinema.repositories.RoomRepository;
import com.dawan.cinema.repositories.ScheduleRepository;
import com.dawan.cinema.repositories.SessionRepository;

@Service
public class SessionServiceImpl extends CRUDServiceImpl<Session, SessionDto, SessionRepository>
        implements SessionService {

    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;

    public SessionServiceImpl(SessionRepository repository, SessionMapper mapper,
            MovieRepository movieRepository, RoomRepository roomRepository,
            ScheduleRepository scheduleRepository) {
        super(repository, mapper, Session.class);
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    protected Page<Session> searchByKeyword(String keyword, Pageable pageable) {
        return repository.search(keyword, pageable);
    }

    @Override
    protected void resolveRelations(Session session, SessionDto dto) {
        Movie movie = movieRepository.findById(dto.movieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", dto.movieId()));
        Room room = roomRepository.findById(dto.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", dto.roomId()));

        checkRoomAvailability(room, dto.heureDebut(), dto.heureFin(), session.getId());

        session.setMovie(movie);
        session.setRoom(room);
        session.setSchedule(findOrCreateSchedule(dto.heureDebut(), dto.heureFin()));
    }

    /** Lève BusinessException si la salle a déjà une séance sur ce créneau. */
    private void checkRoomAvailability(Room room, LocalDateTime debut, LocalDateTime fin, Long excludeId) {
        if (repository.countOverlapping(room.getId(), debut, fin, excludeId) > 0) {
            throw new BusinessException("La salle n°" + room.getNumero()
                    + " est déjà occupée sur ce créneau.");
        }
    }

    /** Réutilise le créneau existant ou en crée un nouveau. */
    private Schedule findOrCreateSchedule(LocalDateTime debut, LocalDateTime fin) {
        return scheduleRepository.findByHeureDebutAndHeureFin(debut, fin)
                .orElseGet(() -> {
                    Schedule schedule = new Schedule();
                    schedule.setHeureDebut(debut);
                    schedule.setHeureFin(fin);
                    return scheduleRepository.save(schedule);
                });
    }
}