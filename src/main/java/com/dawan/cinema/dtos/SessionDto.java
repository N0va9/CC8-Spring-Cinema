package com.dawan.cinema.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SessionDto(
        Long id,
        @NotNull Long movieId,
        String movieTitre,
        @NotNull Long roomId,
        Integer roomNumero,
        String cinemaNom,
        @NotNull LocalDateTime heureDebut,
        @NotNull LocalDateTime heureFin,
        @NotNull @PositiveOrZero @Digits(integer = 4, fraction = 2) BigDecimal price) {

    /** Validation multi-champs : la fin doit être strictement après le début. */
    @JsonIgnore
    @AssertTrue(message = "l'heure de fin doit être après l'heure de début")
    public boolean isPlageHoraireValide() {
        return heureDebut == null || heureFin == null || heureFin.isAfter(heureDebut);
    }
}