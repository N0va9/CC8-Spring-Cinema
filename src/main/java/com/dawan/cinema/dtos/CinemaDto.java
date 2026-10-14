package com.dawan.cinema.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CinemaDto (
	Long id,
	@NotBlank String nom,
	@NotBlank String ville,
	@NotBlank String rue,
	@Positive int numero) {
}
