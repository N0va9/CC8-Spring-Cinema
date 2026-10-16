package com.dawan.cinema.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawan.cinema.dtos.UserDto;
import com.dawan.cinema.services.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private IUserService userService;
	

	@GetMapping(produces = "application/json")
	public List<UserDto> getAll() throws Exception{
		return userService.getAll();
	}
	
	@GetMapping(value = "/{id}", produces = "application/json")
	public UserDto getById(@PathVariable Long id) throws Exception{
		return userService.getById(id);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<String> delete(@PathVariable Long id) throws Exception{
		userService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body("User with id = "+id+" deleted.");
	}
	
	@PostMapping(value={"/save"},consumes = "application/json" , produces = "application/json")
	public ResponseEntity<UserDto> save(@RequestBody UserDto u) throws Exception{
		UserDto savedUser = userService.save(u);
		return ResponseEntity.status(HttpStatus.OK).body(savedUser);
		
	}
	
	@PostMapping(value={"/update"},consumes = "application/json" , produces = "application/json")
	public ResponseEntity<UserDto> update(@RequestBody UserDto u) throws Exception{
		UserDto savedUser = userService.update(u);
		return ResponseEntity.status(HttpStatus.OK).body(savedUser);
		
	}

}
