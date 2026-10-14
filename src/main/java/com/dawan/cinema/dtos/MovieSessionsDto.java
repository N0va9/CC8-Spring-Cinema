package com.dawan.cinema.dtos;

import java.util.List;

public record MovieSessionsDto(MovieDto movie, List<SessionSummaryDto> sessions) {
    
}
