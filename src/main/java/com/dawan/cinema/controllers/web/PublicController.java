package com.dawan.cinema.controllers.web;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawan.cinema.services.CinemaService;
import com.dawan.cinema.services.ProgramService;

/**
 * Pages publiques : liste des cinémas, programme d'un cinéma, recherche de films.
 */
@Controller
public class PublicController {

    private final CinemaService cinemaService;
    private final ProgramService programService;

    public PublicController(CinemaService cinemaService, ProgramService programService) {
        this.cinemaService = cinemaService;
        this.programService = programService;
    }

    /** Accueil : cinémas paginés, filtrés par mot-clé (nom ou ville). */
    @GetMapping("/")
    public String home(@RequestParam(required = false) String keyword,
            @PageableDefault(size = 9, sort = "nom") Pageable pageable, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("cinemas", cinemaService.getAll(keyword, pageable));
        return "public/home";
    }

    /** Programme d'un cinéma pour une date (aujourd'hui par défaut). */
    @GetMapping("/cinemas/{id}")
    public String cinemaProgram(@PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        model.addAttribute("program", programService.getCinemaProgram(id, date));
        return "public/cinema";
    }

    /** Recherche d'un film : séances à venir regroupées par cinéma. */
    @GetMapping("/films")
    public String searchMovies(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("results", programService.findUpcomingSessionsByMovie(keyword));
        return "public/films";
    }
}