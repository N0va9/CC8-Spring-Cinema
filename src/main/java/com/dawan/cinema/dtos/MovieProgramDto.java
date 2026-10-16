package com.dawan.cinema.dtos;

import java.util.List;

public record MovieProgramDto(MovieDto movie, List<CinemaSessionsDto> cinemas) {
}