package com.dawan.cinema.services;

import java.time.LocalDate;
import java.util.List;

import com.dawan.cinema.dtos.CinemaProgramDto;
import com.dawan.cinema.dtos.MovieProgramDto;

/** Consultation publique de la programmation */
public interface ProgramService {

    /**
     * Films projetés dans un cinéma à une date, avec leurs séances
     * 
     * @param cinemaId id du cinéma
     * @param date     date souhaitée, ou null pour aujourd'hui
     */
    CinemaProgramDto getCinemaProgram(Long cinemaId, LocalDate date);

    /**
     * Films dont le titre contient le mot-clé, avec leurs séances à venir par cinéma.
     * 
     * @param keyword mot-clé ; liste vidde s'il est null ou vide
     */
    List<MovieProgramDto> findUpcomingSessionsByMovie(String keyword);
}