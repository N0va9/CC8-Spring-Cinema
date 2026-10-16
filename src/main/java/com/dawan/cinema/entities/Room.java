package com.dawan.cinema.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"cinema_id", "numero"}))
public class Room extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

	@Column(nullable = false)
	private int numero;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private LocalDate dateConstr;

    @OneToMany(mappedBy = "room")
    private Set<Session> sessions = new HashSet<>();

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public int getNumero() {
    	return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public LocalDate getDateConstr() {
		return dateConstr;
	}

	public void setDateConstr(LocalDate dateConstr) {
		this.dateConstr = dateConstr;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}
    
    
}
