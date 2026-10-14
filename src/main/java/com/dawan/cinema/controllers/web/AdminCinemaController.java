package com.dawan.cinema.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawan.cinema.controllers.web.forms.CinemaForm;
import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.services.CinemaService;

/**
 * Administration des cinémas. Toute la mécanique est dans AdminCrudController.
 */
@Controller
@RequestMapping(AdminCinemaController.PATH)
public class AdminCinemaController extends AdminCrudController<CinemaForm, CinemaDto> {

    static final String PATH = "/admin/cinemas";

    public AdminCinemaController(CinemaService service) {
        super(service, PATH, "admin/cinemas");
    }

    @Override
    protected CinemaForm newForm() {
        return new CinemaForm();
    }

    @Override
    protected CinemaForm toForm(CinemaDto dto) {
        return CinemaForm.from(dto);
    }

    @Override
    protected String conflictMessage() {
        return "Un cinéma porte déjà ce nom.";
    }

    @Override
    protected String deleteConflictMessage() {
        return "Impossible de supprimer ce cinéma : des salles y sont encore rattachées.";
    }
}