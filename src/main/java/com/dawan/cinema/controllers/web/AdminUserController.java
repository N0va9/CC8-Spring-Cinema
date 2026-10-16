package com.dawan.cinema.controllers.web;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawan.cinema.controllers.web.forms.UserForm;
import com.dawan.cinema.dtos.UserDto;
import com.dawan.cinema.services.UserService;

/**
 * Administration des comptes administrateurs.
 */
@Controller
@RequestMapping(AdminUserController.PATH)
public class AdminUserController extends AdminCrudController<UserForm, UserDto> {

    static final String PATH = "/admin/users";

    public AdminUserController(UserService service) {
        super(service, PATH, "admin/users");
    }

    @Override
    protected UserForm newForm() {
        return new UserForm();
    }

    @Override
    protected UserForm toForm(UserDto dto) {
        return UserForm.from(dto);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by("username");
    }

    @Override
    protected String conflictMessage() {
        return "Cet identifiant ou cet e-mail est déjà utilisé.";
    }
}