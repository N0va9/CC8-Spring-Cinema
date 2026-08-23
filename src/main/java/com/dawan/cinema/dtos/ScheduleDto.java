package com.dawan.cinema.dtos;

import java.time.LocalDateTime;

public class ScheduleDto {
    private LocalDateTime heureDebut;
    private LocalDateTime heureFin;
    
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
    
    

}
