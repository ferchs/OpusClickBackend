package com.espiritware.opusclick.service;

import com.espiritware.opusclick.dto.UserDto;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.User;

public interface UserService {

	User registerUser(UserDto userDto);
	
//	void deleteUserById(String email);
	
	void updateUser(User user);
	
//	List<User> findAllUsers();

	User findUserById(String email);
	
	boolean userExist(String email);
	
	State getUserState(String email);
	
	void setUserState(String email, State state);
	
}
