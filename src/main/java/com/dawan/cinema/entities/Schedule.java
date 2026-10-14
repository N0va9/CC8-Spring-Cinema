package com.dawan.cinema.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Schedule extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime heureDebut;

    @Column(nullable = false)
    private LocalDateTime heureFin;

    @OneToMany(mappedBy = "schedule")
    private Set<Session> sessions = new HashSet<>();

	public LocalDateTime getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(LocalDateTime heureDebut) {
		this.heureDebut = heureDebut;
	}

	public LocalDateTime getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(LocalDateTime heureFin) {
		this.heureFin = heureFin;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}
    
    
}
