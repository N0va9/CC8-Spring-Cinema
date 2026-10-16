package com.dawan.cinema.services;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawan.cinema.dtos.CinemaProgramDto;
import com.dawan.cinema.dtos.CinemaSessionsDto;
import com.dawan.cinema.dtos.MovieProgramDto;
import com.dawan.cinema.dtos.MovieSessionsDto;
import com.dawan.cinema.dtos.SessionSummaryDto;
import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.entities.Movie;
import com.dawan.cinema.entities.Session;
import com.dawan.cinema.exceptions.ResourceNotFoundException;
import com.dawan.cinema.mappers.CinemaMapper;
import com.dawan.cinema.mappers.MovieMapper;
import com.dawan.cinema.mappers.SessionMapper;
import com.dawan.cinema.repositories.CinemaRepository;
import com.dawan.cinema.repositories.SessionRepository;

@Service 
@Transactional(readOnly = true)
public class ProgramServiceImpl implements ProgramService {
    
    private final SessionRepository sessionRepository;
    private final CinemaRepository cinemaRepository;
    private final SessionMapper sessionMapper;
    private final MovieMapper movieMapper;
    private final CinemaMapper cinemaMapper;
    private final Clock clock;

    public ProgramServiceImpl(SessionRepository sessionRepository, CinemaRepository cinemaRepository,
            SessionMapper sessionMapper, MovieMapper movieMapper, CinemaMapper cinemaMapper, Clock clock) {
        this.sessionRepository = sessionRepository;
        this.cinemaRepository = cinemaRepository;
        this.sessionMapper = sessionMapper;
        this.movieMapper = movieMapper;
        this.cinemaMapper = cinemaMapper;
        this.clock = clock;
    }

    @Override
    public CinemaProgramDto getCinemaProgram(Long cinemaId, LocalDate date) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema", cinemaId));
        LocalDate day = (date != null) ? date : LocalDate.now(clock);

        List<Session> sessions = sessionRepository.findByCinemaBetween(
                cinemaId, day.atStartOfDay(), day.plusDays(1).atStartOfDay());

        List<MovieSessionsDto> movies = groupInOrder(sessions, Session::getMovie).entrySet().stream()
                .map(entry -> new MovieSessionsDto(movieMapper.toDto(entry.getKey()), toSummaries(entry.getValue())))
                .toList();

        return new CinemaProgramDto(cinemaMapper.toDto(cinema), day, movies);
    }

    @Override
    public List<MovieProgramDto> findUpcomingSessionsByMovie(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        List<Session> sessions = sessionRepository.findUpcomingByMovieTitle(keyword.trim(), LocalDateTime.now(clock));

        return groupInOrder(sessions, Session::getMovie).entrySet().stream()
                .map(entry -> toMovieProgram(entry.getKey(), entry.getValue()))
                .toList();
    }

    /** Construit le programme d'un film : ses séances regroupées par cinéma. */
    private MovieProgramDto toMovieProgram(Movie movie, List<Session> sessions) {
        List<CinemaSessionsDto> cinemas = groupInOrder(sessions, s -> s.getRoom().getCinema()).entrySet().stream()
                .map(entry -> new CinemaSessionsDto(cinemaMapper.toDto(entry.getKey()), toSummaries(entry.getValue())))
                .toList();
        return new MovieProgramDto(movieMapper.toDto(movie), cinemas);
    }

    /** Regroupe les séances par clé en conservant l'ordre d'arrivée (donc le tri SQL). */
    private static <K> Map<K, List<Session>> groupInOrder(List<Session> sessions, Function<Session, K> key) {
        return sessions.stream()
                .collect(Collectors.groupingBy(key, LinkedHashMap::new, Collectors.toList()));
    }

    private List<SessionSummaryDto> toSummaries(List<Session> sessions) {
        return sessions.stream().map(sessionMapper::toSummaryDto).toList();
    }
}
