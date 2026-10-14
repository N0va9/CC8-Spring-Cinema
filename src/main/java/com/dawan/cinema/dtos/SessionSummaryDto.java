package com.dawan.cinema.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionSummaryDto(
    Long id,
    LocalDateTime heureDebut,
    LocalDateTime heureFin,
    int roomNumero,
    BigDecimal price
) {

}

