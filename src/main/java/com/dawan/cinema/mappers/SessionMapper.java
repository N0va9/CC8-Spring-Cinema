package com.dawan.cinema.mappers;

import org.springframework.stereotype.Component;

import com.dawan.cinema.dtos.SessionDto;
import com.dawan.cinema.dtos.SessionSummaryDto;
import com.dawan.cinema.entities.Session;

/**
 * Conversion Session <-> SessionDto.
 */
@Component
public class SessionMapper implements Mapper<Session, SessionDto> {

    @Override
    public SessionDto toDto(Session session) {
        return new SessionDto(
                session.getId(),
                session.getMovie().getId(),
                session.getMovie().getTitre(),
                session.getRoom().getId(),
                session.getRoom().getNumero(),
                session.getRoom().getCinema().getNom(),
                session.getSchedule().getHeureDebut(),
                session.getSchedule().getHeureFin(),
                session.getPrice());
    }

    @Override
    public Session toEntity(SessionDto dto) {
        Session session = new Session();
        updateEntityFromDto(session, dto);
        return session;
    }

    @Override
    public void updateEntityFromDto(Session session, SessionDto dto) {
        session.setPrice(dto.price());
    }

    public SessionSummaryDto toSummaryDto(Session session) {
        return new SessionSummaryDto(
            session.getId(),
            session.getSchedule().getHeureDebut(),
            session.getSchedule().getHeureFin(),
            session.getRoom().getNumero(),
            session.getPrice()
        );
    }
}