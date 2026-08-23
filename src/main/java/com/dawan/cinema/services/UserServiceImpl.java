package com.dawan.cinema.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawan.cinema.entities.User;
import com.dawan.cinema.exceptions.GlobalException;
import com.dawan.cinema.repositories.UserRepository;
import com.dawan.cinema.tools.DtoTool;
import com.dawan.cinema.tools.JwtTokenTool;
import com.dawan.cinema.tools.TokenSaver;
import com.dawan.cinema.dtos.LoginDto;
import com.dawan.cinema.dtos.LoginResponseDto;
import com.dawan.cinema.dtos.UserDto;

@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenTool tokenTool;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public List<UserDto> getAll() throws Exception {
		List<UserDto> result = new ArrayList<>();
		List<User> users = userRepository.findAll();
		for(User u : users) {
			result.add(DtoTool.convert(u, UserDto.class));
		}
		
		return result;
	}
	
	@Async
	public CompletableFuture< List<UserDto>> getAllAsync() throws Exception {
		List<UserDto> result = new ArrayList<>();
		List<User> users = userRepository.findAll();
		for(User u : users) {
			result.add(DtoTool.convert(u, UserDto.class));
		}
		
		return CompletableFuture.completedFuture(result);
	}

	@Override
	public UserDto findByUsername(String username) throws Exception {
		User user = userRepository.findByUsername(username).get();
		if(user != null) {
			return DtoTool.convert(user, UserDto.class);
		}
		return null;
	}

	@Override
	public void deleteById(int id) throws Exception {
		userRepository.deleteById(id);
		
	}

	@Override
	public UserDto getById(int id) throws Exception {
		
		Optional<User> optional = userRepository.findById(id);		
		if(optional.isPresent()) {
			return DtoTool.convert(optional.get(), UserDto.class);
		}
		return null;
	}
	
	@Override
	public UserDto save(UserDto userDto) throws Exception {
		User user = DtoTool.convert(userDto, User.class);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new GlobalException("Username déjà utilisé: " + userDto.getUsername());
        }
		user.setId(0);
		user.setPassword(encoder.encode(user.getPassword()));
		User savedUser = userRepository.saveAndFlush(user);
		
		return DtoTool.convert(savedUser, UserDto.class);
	}
	
	@Override
	public UserDto update(UserDto userDto) throws Exception {
		
		User user = DtoTool.convert(userDto, User.class);
		if(user.getId() == 0 || !userRepository.existsById(user.getId())) {
            throw new GlobalException("Utilisateur non-existant avec id: " + userDto.getId());
		}
		
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new  GlobalException("Username déjà utilisé: " + userDto.getUsername());
        }

		User userDB = userRepository.findById(user.getId()).get();
		if(!userDB.getPassword().equals(user.getPassword())) {
			user.setPassword(encoder.encode(user.getPassword()));
		}
		
		User updateUser = userRepository.saveAndFlush(user);
		
		return DtoTool.convert(updateUser, UserDto.class);
	}
	
	@Override
	public LoginResponseDto checkLogin(LoginDto loginDto) throws Exception {
		
		User user = userRepository.findByUsername(loginDto.getUsername()).get();
		
		if(user != null && encoder.matches(loginDto.getPassword(), user.getPassword())) {
			
			LoginResponseDto responseDto = DtoTool.convert(user, LoginResponseDto.class);

			Map<String, Object> claims = new HashMap<>();
			claims.put("userId", user.getId());
			claims.put("fullName", user.getFirstName()+" "+user.getLastName());
			
			String token =  tokenTool.doGenerateToken(claims, user.getUsername());
			responseDto.setToken(token);
			
			TokenSaver.tokenByUsername.put(user.getUsername(), token);
			
			return responseDto;
		}
		
		
		throw new GlobalException("Echec connexion............");
	}

}
