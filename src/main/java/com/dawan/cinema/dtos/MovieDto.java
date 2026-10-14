package com.dawan.cinema.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MovieDto(
        Long id,
        @NotBlank String titre,
        @Min(1895) int annee,
        @NotBlank String genre) {
}