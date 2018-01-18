package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.State;

public interface AccountService {
	
	Account registerAccount (String email, String name, String lastname, String password);
	
//	void deleteUserById(String email);
	
	void updateAccount(Account account);
	
//	List<User> findAllUsers();

	Account findAccountById(String email);
	
	void setAccountState(String email, State state);
	
	State getAccountState(String email);
	
	boolean accountExist(String email);
		
}
