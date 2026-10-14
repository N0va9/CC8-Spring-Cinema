package com.dawan.cinema.controllers.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.dawan.cinema.controllers.web.forms.AdminForm;
import com.dawan.cinema.exceptions.BusinessException;
import com.dawan.cinema.services.CRUDService;

import jakarta.validation.Valid;

/**
 * Mécanique commune des pages d'administration (liste, création, modification, suppression),
 * selon le modèle POST / redirection / GET avec messages flash.
 * Les sous-classes fixent l'URL avec @RequestMapping et redéfinissent les points d'extension.
 *
 * @param <F> type du formulaire
 * @param <D> type du DTO
 */
public abstract class AdminCrudController<F extends AdminForm<D>, D> {

    protected static final String FORM_ATTRIBUTE = "form";

    protected final CRUDService<D> service;
    private final String basePath;
    private final String viewPrefix;

    /**
     * @param service    service CRUD de l'entité
     * @param basePath   URL de la liste, ex. "/admin/cinemas"
     * @param viewPrefix dossier des templates, ex. "admin/cinemas"
     */
    protected AdminCrudController(CRUDService<D> service, String basePath, String viewPrefix) {
        this.service = service;
        this.basePath = basePath;
        this.viewPrefix = viewPrefix;
    }

    /** Formulaire vide pour la création. */
    protected abstract F newForm();

    /** Formulaire prérempli à partir d'un DTO existant. */
    protected abstract F toForm(D dto);

    /** Message affiché quand la base refuse l'enregistrement (doublon, contrainte). */
    protected String conflictMessage() {
        return "Ces données entrent en conflit avec un élément existant.";
    }

    /** Message affiché quand la suppression est refusée (données rattachées). */
    protected String deleteConflictMessage() {
        return "Suppression impossible : d'autres données y sont rattachées.";
    }

    /** Ajoute au modèle les données des listes déroulantes du formulaire. Rien par défaut. */
    protected void addReferenceData(Model model) {
        // aucune donnée de référence par défaut
    }

    /** Tri appliqué à la liste quand l'URL n'en précise pas. Par id par défaut. */
    protected Sort defaultSort() {
        return Sort.by("id");
    }


    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable, Model model) {
        Pageable sorted = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", service.getAll(keyword, sorted));
        return viewPrefix + "/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(FORM_ATTRIBUTE, newForm());
        return showForm(model);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute(FORM_ATTRIBUTE) F form, BindingResult result,
            Model model, RedirectAttributes redirect) {
        return save(result, model, redirect, () -> service.create(form.toDto()));
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute(FORM_ATTRIBUTE, toForm(service.getById(id)));
        return showForm(model);
    }

    /** L'id de l'URL fait foi, le champ caché ne sert qu'à l'affichage. */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute(FORM_ATTRIBUTE) F form,
            BindingResult result, Model model, RedirectAttributes redirect) {
        form.setId(id);
        return save(result, model, redirect, () -> service.update(id, form.toDto()));
    }

    /** Suppression en POST uniquement. */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("success", "Suppression effectuée.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error", deleteConflictMessage());
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:" + basePath;
    }

    private String save(BindingResult result, Model model, RedirectAttributes redirect, Runnable action) {
        if (!result.hasErrors()) {
            try {
                action.run();
                redirect.addFlashAttribute("success", "Enregistrement effectué.");
                return "redirect:" + basePath;
            } catch (DataIntegrityViolationException ex) {
                result.reject("conflict", conflictMessage());
            } catch (BusinessException ex) {
                result.reject("business", ex.getMessage());
            }
        }
        return showForm(model);
    }

    private String showForm(Model model) {
        addReferenceData(model);
        return viewPrefix + "/form";
    }
}