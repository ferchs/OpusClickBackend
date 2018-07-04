package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.User;

public interface UserService {

	void createUser(User user);

	User createUser(int id, String identificationNumber, String phoneNumber, int opusCoins, State state);
		
//	void deleteUserById(String email);
	
	void updateUser(User user);
	
//	List<User> findAllUsers();

	User findUserById(int id);
		
	boolean userExist(int id);
	
	State getUserState(int id);
	
	void setUserState(int id, State state);
	
}
