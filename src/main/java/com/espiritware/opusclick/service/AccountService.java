package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.State;

public interface AccountService {
	
	Account createAccount (Account account);
	
	Account createAccount (int id,String email, String name, String lastname, String password);
	
//	void deleteUserById(String email);
	
	Account updateAccount(Account account);
	
//	List<User> findAllUsers();

	Account findAccountById(int id);
	
	Account findAccountByEmail(String email);
	
	void setAccountState(String email, State state);
	
	void setAccountState(int id, State state);
	
	State getAccountState(String email);

	State getAccountState(int id);
	
	boolean accountExist(String email);
	
	boolean accountExist(int id);
	
	boolean accountConfirmed(String email);
	
	boolean accountConfirmed(int id);

		
}
