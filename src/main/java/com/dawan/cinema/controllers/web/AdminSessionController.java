package com.dawan.cinema.controllers.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawan.cinema.controllers.web.forms.SessionForm;
import com.dawan.cinema.dtos.SessionDto;
import com.dawan.cinema.services.MovieService;
import com.dawan.cinema.services.RoomService;
import com.dawan.cinema.services.SessionService;

/**
 * Administration des séances. Le formulaire propose les films et les salles ;
 * le chevauchement de créneaux est signalé par le service (BusinessException).
 */
@Controller
@RequestMapping(AdminSessionController.PATH)
public class AdminSessionController extends AdminCrudController<SessionForm, SessionDto> {

    static final String PATH = "/admin/sessions";

    private final MovieService movieService;
    private final RoomService roomService;

    public AdminSessionController(SessionService service, MovieService movieService, RoomService roomService) {
        super(service, PATH, "admin/sessions");
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @Override
    protected SessionForm newForm() {
        return new SessionForm();
    }

    @Override
    protected SessionForm toForm(SessionDto dto) {
        return SessionForm.from(dto);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by("schedule.heureDebut");
    }

    /** Films triés par titre et salles triées par cinéma puis numéro, pour les menus déroulants. */
    @Override
    protected void addReferenceData(Model model) {
        model.addAttribute("movies",
                movieService.getAll(null, Pageable.unpaged(Sort.by("titre"))).getContent());
        model.addAttribute("rooms",
                roomService.getAll(null, Pageable.unpaged(Sort.by("cinema.nom", "numero"))).getContent());
    }
}