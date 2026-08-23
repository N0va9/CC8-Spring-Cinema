package com.dawan.cinema.dtos;

public class LoginResponseDto {
	
	private String token;
	
	public LoginResponseDto(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}