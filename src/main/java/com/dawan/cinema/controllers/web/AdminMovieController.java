package com.dawan.cinema.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawan.cinema.controllers.web.forms.MovieForm;
import com.dawan.cinema.dtos.MovieDto;
import com.dawan.cinema.services.MovieService;

/**
 * Administration des films.
 */
@Controller
@RequestMapping(AdminMovieController.PATH)
public class AdminMovieController extends AdminCrudController<MovieForm, MovieDto> {

    static final String PATH = "/admin/movies";

    public AdminMovieController(MovieService service) {
        super(service, PATH, "admin/movies");
    }

    @Override
    protected MovieForm newForm() {
        return new MovieForm();
    }

    @Override
    protected MovieForm toForm(MovieDto dto) {
        return MovieForm.from(dto);
    }

    @Override
    protected String deleteConflictMessage() {
        return "Impossible de supprimer ce film : des séances sont programmées.";
    }
}