package io.hh.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.hh.ppmtool.domain.User;
import io.hh.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.hh.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//Username has be unique
			newUser.setUsername(newUser.getUsername());
			//MAke sure that password and conform password match
			
			//We don´t persist or show the confirm password
			newUser.setConfirmPassword(null);
			return userRepository.save(newUser);
		} catch(Exception e) {
			throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
		}
		
	}

}
