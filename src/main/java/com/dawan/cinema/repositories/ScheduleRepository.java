package com.dawan.cinema.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawan.cinema.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByHeureDebutAndHeureFin(LocalDateTime heureDebut, LocalDateTime heureFin);
}