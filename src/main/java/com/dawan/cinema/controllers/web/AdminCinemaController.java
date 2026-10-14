package com.dawan.cinema.controllers.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dawan.cinema.controllers.web.forms.CinemaForm;
import com.dawan.cinema.services.CinemaService;

import jakarta.validation.Valid;

/**
 * Administration des cinémas : liste, création, modification, suppression.
 * Suit le modèle POST / redirection / GET avec messages flash.
 */
@Controller
@RequestMapping("/admin/cinemas")
public class AdminCinemaController {

    private static final String LIST_VIEW = "admin/cinemas/list";
    private static final String FORM_VIEW = "admin/cinemas/form";
    private static final String REDIRECT_LIST = "redirect:/admin/cinemas";
    private static final String FORM_ATTRIBUTE = "cinemaForm";

    private final CinemaService cinemaService;

    public AdminCinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    /** Liste paginée avec recherche. */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "nom") Pageable pageable, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("cinemas", cinemaService.getAll(keyword, pageable));
        return LIST_VIEW;
    }

    /** Formulaire vide de création. */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(FORM_ATTRIBUTE, new CinemaForm());
        return FORM_VIEW;
    }

    /** Création : réaffiche le formulaire en cas d'erreur, sinon redirige vers la liste. */
    @PostMapping
    public String create(@Valid @ModelAttribute(FORM_ATTRIBUTE) CinemaForm form, BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return FORM_VIEW;
        }
        try {
            cinemaService.create(form.toDto());
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue("nom", "duplicate", "Un cinéma porte déjà ce nom.");
            return FORM_VIEW;
        }
        redirect.addFlashAttribute("success", "Cinéma créé.");
        return REDIRECT_LIST;
    }

    /** Formulaire prérempli de modification. */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute(FORM_ATTRIBUTE, CinemaForm.from(cinemaService.getById(id)));
        return FORM_VIEW;
    }

    /** Modification : l'id de l'URL fait foi (le champ caché n'est utilisé que pour l'affichage). */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute(FORM_ATTRIBUTE) CinemaForm form,
            BindingResult result, RedirectAttributes redirect) {
        form.setId(id);
        if (result.hasErrors()) {
            return FORM_VIEW;
        }
        try {
            cinemaService.update(id, form.toDto());
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue("nom", "duplicate", "Un cinéma porte déjà ce nom.");
            return FORM_VIEW;
        }
        redirect.addFlashAttribute("success", "Cinéma modifié.");
        return REDIRECT_LIST;
    }

    /** Suppression (POST uniquement). Refusée si des salles sont rattachées. */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            cinemaService.delete(id);
            redirect.addFlashAttribute("success", "Cinéma supprimé.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error",
                    "Impossible de supprimer ce cinéma : des salles y sont encore rattachées.");
        }
        return REDIRECT_LIST;
    }
}