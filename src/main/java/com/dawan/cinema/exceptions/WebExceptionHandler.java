package com.dawan.cinema.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Gestion des erreurs des pages web : renvoie des pages HTML
 * (l'API REST garde son propre gestionnaire JSON).
 */
@ControllerAdvice(basePackages = "com.dawan.cinema.controllers.web")
public class WebExceptionHandler {

    /** Ressource introuvable : page 404 avec le message. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        ModelAndView view = new ModelAndView("error/404");
        view.addObject("message", ex.getMessage());
        view.setStatus(HttpStatus.NOT_FOUND);
        return view;
    }
}