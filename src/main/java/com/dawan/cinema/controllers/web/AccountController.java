package com.dawan.cinema.controllers.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dawan.cinema.controllers.web.forms.PasswordChangeForm;
import com.dawan.cinema.exceptions.BusinessException;
import com.dawan.cinema.services.UserService;

import jakarta.validation.Valid;

/**
 * Espace personnel : changement de son propre mot de passe.
 */
@Controller
@RequestMapping("/admin/account/password")
public class AccountController {

    private static final String VIEW = "admin/account/password";

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String form(Model model) {
        model.addAttribute("form", new PasswordChangeForm());
        return VIEW;
    }

    @PostMapping
    public String change(@Valid @ModelAttribute("form") PasswordChangeForm form, BindingResult result,
            Principal principal, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return VIEW;
        }
        try {
            userService.changePassword(principal.getName(), form.getCurrentPassword(), form.getNewPassword());
        } catch (BusinessException ex) {
            result.reject("business", ex.getMessage());
            return VIEW;
        }
        redirect.addFlashAttribute("success", "Mot de passe modifié.");
        return "redirect:/admin/account/password";
    }
}