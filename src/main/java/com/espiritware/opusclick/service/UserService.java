package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.User;

public interface UserService {

	void createUser(User user);

	User createUser(String id, String identificationNumber, String phoneNumber, int opusCoins, State state);
		
//	void deleteUserById(String email);
	
	void updateUser(User user);
	
//	List<User> findAllUsers();

	User findUserById(String email);
	
	boolean userExist(String email);
	
	State getUserState(String email);
	
	void setUserState(String email, State state);
	
}
