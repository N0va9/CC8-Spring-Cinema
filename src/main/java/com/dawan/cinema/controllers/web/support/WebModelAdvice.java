package com.dawan.cinema.controllers.web.support;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Ajoute à toutes les pages web l'information "loggedIn" (utilisée par la barre de navigation).
 */
@ControllerAdvice(basePackages = "com.dawan.cinema.controllers.web")
public class WebModelAdvice {

    @ModelAttribute("loggedIn")
    public boolean loggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}