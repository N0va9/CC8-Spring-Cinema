package com.dawan.cinema.dtos;

import java.util.List;

public record CinemaSessionsDto(CinemaDto cinema, List<SessionSummaryDto> sessions) {
}