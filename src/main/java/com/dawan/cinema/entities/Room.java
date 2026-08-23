package com.dawan.cinema.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public class Room extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private LocalDateTime dateConstr;

    @OneToMany(mappedBy = "room")
    private Set<Session> sessions = new HashSet<>();

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public LocalDateTime getDateConstr() {
		return dateConstr;
	}

	public void setDateConstr(LocalDateTime dateConstr) {
		this.dateConstr = dateConstr;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}
    
    
}
