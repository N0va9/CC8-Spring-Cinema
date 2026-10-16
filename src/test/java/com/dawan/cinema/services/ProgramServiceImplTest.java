package com.dawan.cinema.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dawan.cinema.dtos.CinemaProgramDto;
import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.entities.Movie;
import com.dawan.cinema.entities.Room;
import com.dawan.cinema.entities.Schedule;
import com.dawan.cinema.entities.Session;
import com.dawan.cinema.exceptions.ResourceNotFoundException;
import com.dawan.cinema.mappers.CinemaMapper;
import com.dawan.cinema.mappers.MovieMapper;
import com.dawan.cinema.mappers.SessionMapper;
import com.dawan.cinema.repositories.CinemaRepository;
import com.dawan.cinema.repositories.SessionRepository;

@ExtendWith(MockitoExtension.class)
class ProgramServiceImplTest {

    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");
    private static final LocalDate AUJOURD_HUI = LocalDate.of(2026, 10, 20);

    @Mock private SessionRepository sessionRepository;
    @Mock private CinemaRepository cinemaRepository;

    private ProgramServiceImpl service;
    private Cinema cinema;
    private Room room;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-10-20T08:00:00Z"), PARIS);
        service = new ProgramServiceImpl(sessionRepository, cinemaRepository,
                new SessionMapper(), new MovieMapper(), new CinemaMapper(), fixedClock);

        cinema = new Cinema();
        cinema.setId(1L);
        cinema.setNom("Le Grand Rex");
        cinema.setVille("Paris");
        cinema.setRue("Boulevard Poissonnière");
        cinema.setNumero(1);

        room = new Room();
        room.setId(1L);
        room.setNumero(1);
        room.setCinema(cinema);
    }

    private Movie movie(long id, String titre) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitre(titre);
        movie.setAnnee(2024);
        movie.setGenre("Science-fiction");
        return movie;
    }

    private Session session(Movie movie, int heure) {
        Schedule schedule = new Schedule();
        schedule.setHeureDebut(AUJOURD_HUI.atTime(heure, 0));
        schedule.setHeureFin(AUJOURD_HUI.atTime(heure + 2, 30));
        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setSchedule(schedule);
        session.setPrice(new BigDecimal("9.50"));
        return session;
    }

    @Test
    void getCinemaProgram_utilise_la_date_du_jour_et_regroupe_les_seances_par_film() {
        Movie dune = movie(1L, "Dune");
        Movie interstellar = movie(2L, "Interstellar");
        when(cinemaRepository.findById(1L)).thenReturn(Optional.of(cinema));
        // Ordre renvoyé par la requête : par titre puis par heure
        when(sessionRepository.findByCinemaBetween(1L, AUJOURD_HUI.atStartOfDay(), AUJOURD_HUI.plusDays(1).atStartOfDay()))
                .thenReturn(List.of(session(dune, 14), session(dune, 19), session(interstellar, 16)));

        CinemaProgramDto program = service.getCinemaProgram(1L, null);

        assertThat(program.date()).isEqualTo(AUJOURD_HUI);
        assertThat(program.cinema().nom()).isEqualTo("Le Grand Rex");
        assertThat(program.movies()).hasSize(2);
        assertThat(program.movies().get(0).movie().titre()).isEqualTo("Dune");
        assertThat(program.movies().get(0).sessions())
                .extracting(s -> s.heureDebut().getHour())
                .containsExactly(14, 19);
        assertThat(program.movies().get(1).movie().titre()).isEqualTo("Interstellar");
    }

    @Test
    void getCinemaProgram_leve_une_ressource_introuvable_pour_un_cinema_inconnu() {
        when(cinemaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCinemaProgram(99L, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findUpcomingSessionsByMovie_retourne_une_liste_vide_sans_interroger_la_base_si_le_mot_cle_est_vide() {
        assertThat(service.findUpcomingSessionsByMovie("  ")).isEmpty();
        verifyNoInteractions(sessionRepository);
    }

    @Test
    void findUpcomingSessionsByMovie_cherche_a_partir_de_maintenant() {
        when(sessionRepository.findUpcomingByMovieTitle("dune", LocalDateTime.of(2026, 10, 20, 10, 0)))
                .thenReturn(List.of());

        assertThat(service.findUpcomingSessionsByMovie(" dune ")).isEmpty();
    }
}