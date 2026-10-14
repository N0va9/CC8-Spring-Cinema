package com.dawan.cinema.services;

import java.util.List;

import com.dawan.cinema.dtos.LoginDto;
import com.dawan.cinema.dtos.LoginResponseDto;
import com.dawan.cinema.dtos.UserDto;

public interface IUserService {
	
	List<UserDto> getAll() throws Exception;
	UserDto findByUsername(String username) throws Exception;
	void deleteById(Long id) throws Exception;
	UserDto getById(Long id) throws Exception;
	LoginResponseDto checkLogin(LoginDto loginDto) throws Exception;
	UserDto save(UserDto userDto) throws Exception;
	UserDto update(UserDto userDto) throws Exception;

}

