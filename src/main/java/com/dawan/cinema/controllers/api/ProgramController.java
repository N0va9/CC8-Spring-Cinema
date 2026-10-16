package com.dawan.cinema.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.CinemaProgramDto;
import com.dawan.cinema.dtos.MovieProgramDto;
import com.dawan.cinema.services.ProgramService;

import io.swagger.v3.oas.annotations.Operation;

/** API REST publique de consultation de la programmation. */
@RestController
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/cinemas/{cinemaId}")
    @Operation(summary = "Films et séances d'un cinéma pour une date (aujourd'hui par défaut)")
    public CinemaProgramDto getCinemaProgram(@PathVariable Long cinemaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return programService.getCinemaProgram(cinemaId, date);
    }

    @GetMapping("/movies")
    @Operation(summary = "Séances à venir des films correspondant au mot-clé, par cinéma")
    public List<MovieProgramDto> searchMovies(@RequestParam String keyword) {
        return programService.findUpcomingSessionsByMovie(keyword);
    }
}