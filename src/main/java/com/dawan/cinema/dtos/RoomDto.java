package com.dawan.cinema.dtos;

import java.time.LocalDateTime;

public class RoomDto {
    private int cinemaId;
    private int capacite;
    private LocalDateTime dateConstr;
    
	public int getCinemaId() {
		return cinemaId;
	}
	public void setCinemaId(int cinemaId) {
		this.cinemaId = cinemaId;
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
    
    
}
