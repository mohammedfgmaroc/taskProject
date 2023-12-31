package com.tms.tmsustem.service;

import com.tms.tmsustem.dto.UserDto;
import com.tms.tmsustem.model.User;

public interface UserService {
	
	User save(UserDto userDto);
	
	// find a user by username
    User findByUsername(String username);
}
