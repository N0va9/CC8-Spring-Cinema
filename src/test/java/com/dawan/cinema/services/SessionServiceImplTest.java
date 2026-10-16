package com.dawan.cinema.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dawan.cinema.dtos.SessionDto;
import com.dawan.cinema.entities.Cinema;
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

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    private static final LocalDateTime DEBUT = LocalDateTime.of(2026, 10, 20, 14, 0);
    private static final LocalDateTime FIN = DEBUT.plusMinutes(150);

    @Mock private SessionRepository sessionRepository;
    @Mock private MovieRepository movieRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private ScheduleRepository scheduleRepository;

    private SessionService service;

    @BeforeEach
    void setup() {
        service = new SessionServiceImpl(sessionRepository, new SessionMapper(),
            movieRepository, roomRepository, scheduleRepository);
    }

    // --- Données de tests ---

    private static Movie movie() {
        Movie movie = new Movie();
        movie.setId(1L);movie.setTitre("Dune : Deuxième partie");
        movie.setAnnee(2024);
        movie.setGenre("Science-fiction");
        return movie;
    }

    private static Room room() {
        Cinema cinema = new Cinema();
        cinema.setId(1L);
        cinema.setNom("Le Grand Rex");
        Room room = new Room();
        room.setId(2L);
        room.setNumero(3);
        room.setCinema(cinema);
        return room;
    }

    private static Schedule schedule() {
        Schedule schedule = new Schedule();
        schedule.setId(5L);
        schedule.setHeureDebut(DEBUT);
        schedule.setHeureFin(FIN);
        return schedule;
    }

    private static SessionDto newSessionDto() {
        return new SessionDto(null, 1L, null, 2L, null, null, DEBUT, FIN, new BigDecimal("9.50"));
    }

    private void givenMovieAndRoomExist() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie()));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room()));
    }

    // -- Tests --

    @Test 
    void create_refuse_une_seance_si_la_salle_est_deja_occupee() {
        givenMovieAndRoomExist();
        when(sessionRepository.countOverlapping(2L, DEBUT, FIN, null)).thenReturn(1L);

        assertThatThrownBy(() -> service.create(newSessionDto()))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("déjà occupée");

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void create_reutilise_l_horaire_existant() {
        givenMovieAndRoomExist();
        when(sessionRepository.countOverlapping(2L, DEBUT, FIN, null)).thenReturn(0L);
        when(scheduleRepository.findByHeureDebutAndHeureFin(DEBUT, FIN)).thenReturn(Optional.of(schedule()));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessionDto result = service.create(newSessionDto());

        verify(scheduleRepository, never()).save(any());
        assertThat(result.heureDebut()).isEqualTo(DEBUT);
        assertThat(result.movieTitre()).isEqualTo("Dune : Deuxième partie");
        assertThat(result.cinemaNom()).isEqualTo("Le Grand Rex");
        assertThat(result.roomNumero()).isEqualTo(3);
    }

    @Test
    void create_cree_un_nouvel_horaire_s_il_n_existe_pas() {
        givenMovieAndRoomExist();
        when(sessionRepository.countOverlapping(2L, DEBUT, FIN, null)).thenReturn(0L);
        when(scheduleRepository.findByHeureDebutAndHeureFin(DEBUT, FIN)).thenReturn(Optional.empty());
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.create(newSessionDto());

        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void create_leve_une_ressource_introuvable_si_le_film_n_existe_pas() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(newSessionDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Movie");
    }

    @Test
    void update_exclut_la_seance_modifiee_du_controle_de_chevauchement() {
        Session existing = new Session();
        existing.setId(7L);
        when(sessionRepository.findById(7L)).thenReturn(Optional.of(existing));
        givenMovieAndRoomExist();
        when(sessionRepository.countOverlapping(2L, DEBUT, FIN, 7L)).thenReturn(0L);
        when(scheduleRepository.findByHeureDebutAndHeureFin(DEBUT, FIN)).thenReturn(Optional.of(schedule()));

        service.update(7L, newSessionDto());

        verify(sessionRepository).countOverlapping(2L, DEBUT, FIN, 7L);
    }

}