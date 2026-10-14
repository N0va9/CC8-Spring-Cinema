package com.dawan.cinema.dtos;

import java.time.LocalDate;
import java.util.List;

public record CinemaProgramDto(CinemaDto cinema, LocalDate date, List<MovieSessionsDto> movies) {
}