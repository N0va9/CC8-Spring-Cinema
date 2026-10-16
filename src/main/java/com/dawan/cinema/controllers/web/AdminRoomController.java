package com.dawan.cinema.controllers.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawan.cinema.controllers.web.forms.RoomForm;
import com.dawan.cinema.dtos.RoomDto;
import com.dawan.cinema.services.CinemaService;
import com.dawan.cinema.services.RoomService;

/**
 * Administration des salles. Le formulaire propose la liste des cinémas.
 */
@Controller
@RequestMapping(AdminRoomController.PATH)
public class AdminRoomController extends AdminCrudController<RoomForm, RoomDto> {

    static final String PATH = "/admin/rooms";

    private final CinemaService cinemaService;

    public AdminRoomController(RoomService service, CinemaService cinemaService) {
        super(service, PATH, "admin/rooms");
        this.cinemaService = cinemaService;
    }

    @Override
    protected RoomForm newForm() {
        return new RoomForm();
    }

    @Override
    protected RoomForm toForm(RoomDto dto) {
        return RoomForm.from(dto);
    }

    @Override
    protected String conflictMessage() {
        return "Ce cinéma possède déjà une salle avec ce numéro.";
    }

    @Override
    protected String deleteConflictMessage() {
        return "Impossible de supprimer cette salle : des séances y sont programmées.";
    }

    /** Liste des cinémas, triés par nom, pour le menu déroulant. */
    @Override
    protected void addReferenceData(Model model) {
        model.addAttribute("cinemas",
                cinemaService.getAll(null, Pageable.unpaged(Sort.by("nom"))).getContent());
    }
}