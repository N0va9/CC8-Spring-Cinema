package com.dawan.cinema.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawan.cinema.entities.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Override
    @EntityGraph(attributePaths = {"movie", "room", "room.cinema", "schedule"})
    Page<Session> findAll(Pageable pageable);

    /**
     * Recherche sur le titre du film ou le nom du cinéma.
     *
     * @param keyword  texte recherché (correspondance partielle)
     * @param pageable page, taille et tri demandés
     */
    @EntityGraph(attributePaths = {"movie", "room", "room.cinema", "schedule"})
    @Query("""
           SELECT s FROM Session s
           WHERE LOWER(s.movie.titre)      LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(s.room.cinema.nom)  LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Session> search(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Compte les séances d'une salle dont le créneau chevauche [debut, fin[.
     *
     * @param excludeId id de la séance à ignorer (cas de la modification), ou null
     */
    @Query("""
           SELECT COUNT(s) FROM Session s
           WHERE s.room.id = :roomId
             AND s.schedule.heureDebut < :fin
             AND s.schedule.heureFin   > :debut
             AND (:excludeId IS NULL OR s.id <> :excludeId)
           """)
    long countOverlapping(@Param("roomId") Long roomId,
                          @Param("debut") LocalDateTime debut,
                          @Param("fin") LocalDateTime fin,
                          @Param("excludeId") Long excludeId);

    /**
     * Séances d'un cinéma dont le début est dans [start, end[, triées par film puis par heure.
     */
    @EntityGraph(attributePaths = {"movie", "room", "schedule"})
    @Query("""
        SELECT s FROM Session s
        WHERE s.room.cinema.id = :cinemaId
            AND s.schedule.heureDebut >= :start
            AND s.schedule.heureDebut <  :end
        ORDER BY s.movie.titre, s.schedule.heureDebut
        """)
    List<Session> findByCinemaBetween(@Param("cinemaId") Long cinemaId,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    /**
     * Séances à venir des films dont le titre contient le mot-clé,
     * triées par film, puis cinéma, puis heure.
     */
    @EntityGraph(attributePaths = {"movie", "room", "room.cinema", "schedule"})
    @Query("""
        SELECT s FROM Session s
        WHERE LOWER(s.movie.titre) LIKE LOWER(CONCAT('%', :keyword, '%'))
            AND s.schedule.heureDebut >= :from
        ORDER BY s.movie.titre, s.room.cinema.nom, s.schedule.heureDebut
        """)
    List<Session> findUpcomingByMovieTitle(@Param("keyword") String keyword,
                                        @Param("from") LocalDateTime from);
}