package com.dawan.cinema.seed;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.entities.Movie;
import com.dawan.cinema.entities.Room;
import com.dawan.cinema.entities.Schedule;
import com.dawan.cinema.entities.Session;
import com.dawan.cinema.repositories.CinemaRepository;
import com.dawan.cinema.repositories.MovieRepository;
import com.dawan.cinema.repositories.RoomRepository;
import com.dawan.cinema.repositories.ScheduleRepository;
import com.dawan.cinema.repositories.SessionRepository;

/**
 * Remplit une base vide avec un jeu de démonstration au démarrage.
 * Actif uniquement en profil "dev" ET si app.seed.enabled=true (SEED_DATA dans .env).
 * Ne fait rien si des cinémas existent déjà.
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final int DAYS = 30;
    private static final List<LocalTime> SLOTS = List.of(
            LocalTime.of(14, 0), LocalTime.of(16, 45), LocalTime.of(19, 30), LocalTime.of(22, 15));
    private static final Duration SESSION_LENGTH = Duration.ofMinutes(150);
    private static final LocalTime EVENING = LocalTime.of(17, 0);
    private static final BigDecimal DAY_PRICE = new BigDecimal("7.90");
    private static final BigDecimal EVENING_PRICE = new BigDecimal("11.50");
    private static final int[] CAPACITIES = {320, 210, 150, 120, 90};

    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;
    private final SessionRepository sessionRepository;
    private final Clock clock;

    public DataSeeder(CinemaRepository cinemaRepository, RoomRepository roomRepository,
            MovieRepository movieRepository, ScheduleRepository scheduleRepository,
            SessionRepository sessionRepository, Clock clock) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.sessionRepository = sessionRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (cinemaRepository.count() > 0) {
            log.info("Jeu de démonstration ignoré : la base contient déjà des données.");
            return;
        }
        List<Movie> movies = movieRepository.saveAll(SeedData.MOVIES.stream().map(this::toMovie).toList());
        List<Room> rooms = createCinemasAndRooms();
        int sessionCount = createSessions(movies, rooms);

        log.info("Jeu de démonstration créé : {} cinémas, {} salles, {} films, {} séances sur {} jours.",
                SeedData.CINEMAS.size(), rooms.size(), movies.size(), sessionCount, DAYS);
    }

    /** Crée chaque cinéma et ses salles numérotées à partir de 1. */
    private List<Room> createCinemasAndRooms() {
        List<Room> rooms = new ArrayList<>();
        for (SeedData.CinemaSeed seed : SeedData.CINEMAS) {
            Cinema cinema = cinemaRepository.save(toCinema(seed));
            for (int numero = 1; numero <= seed.salles(); numero++) {
                rooms.add(roomRepository.save(toRoom(cinema, numero)));
            }
        }
        return rooms;
    }

    /**
     * Pour chaque jour et chaque créneau, crée un horaire partagé puis une séance par salle.
     * Les créneaux sont espacés de 2h45 pour 2h30 de séance : aucun chevauchement possible.
     */
    private int createSessions(List<Movie> movies, List<Room> rooms) {
        LocalDate firstDay = LocalDate.now(clock);
        List<Session> sessions = new ArrayList<>();

        for (int day = 0; day < DAYS; day++) {
            LocalDate date = firstDay.plusDays(day);
            for (int slot = 0; slot < SLOTS.size(); slot++) {
                LocalTime time = SLOTS.get(slot);
                Schedule schedule = scheduleRepository.save(toSchedule(date.atTime(time)));
                BigDecimal price = time.isBefore(EVENING) ? DAY_PRICE : EVENING_PRICE;

                for (int r = 0; r < rooms.size(); r++) {
                    Movie movie = movies.get((r + day + slot) % movies.size());
                    sessions.add(toSession(movie, rooms.get(r), schedule, price));
                }
            }
        }
        sessionRepository.saveAll(sessions);
        return sessions.size();
    }

    // --- Construction des entités ---

    private Movie toMovie(SeedData.MovieSeed seed) {
        Movie movie = new Movie();
        movie.setTitre(seed.titre());
        movie.setAnnee(seed.annee());
        movie.setGenre(seed.genre());
        return movie;
    }

    private Cinema toCinema(SeedData.CinemaSeed seed) {
        Cinema cinema = new Cinema();
        cinema.setNom(seed.nom());
        cinema.setNumero(seed.numero());
        cinema.setRue(seed.rue());
        cinema.setVille(seed.ville());
        return cinema;
    }

    private Room toRoom(Cinema cinema, int numero) {
        Room room = new Room();
        room.setCinema(cinema);
        room.setNumero(numero);
        room.setCapacite(CAPACITIES[(numero - 1) % CAPACITIES.length]);
        room.setDateConstr(LocalDate.of(2005 + numero, 9, 1));
        return room;
    }

    private Schedule toSchedule(LocalDateTime debut) {
        Schedule schedule = new Schedule();
        schedule.setHeureDebut(debut);
        schedule.setHeureFin(debut.plus(SESSION_LENGTH));
        return schedule;
    }

    private Session toSession(Movie movie, Room room, Schedule schedule, BigDecimal price) {
        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setSchedule(schedule);
        session.setPrice(price);
        return session;
    }
}