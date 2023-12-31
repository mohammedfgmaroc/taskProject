package com.tms.tmsustem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tms.tmsustem.dto.UserDto;
import com.tms.tmsustem.model.User;
import com.tms.tmsustem.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()) , userDto.getRole());
		return userRepository.save(user);
	}
	
	@Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
