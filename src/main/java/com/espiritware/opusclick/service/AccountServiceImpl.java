package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.error.AccountAlreadyExistException;
import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.State;

@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService{
	
	GenericDao< Account > accountDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public void setDao( GenericDao< Account > daoToSet ){
		accountDao = daoToSet;
		accountDao.setEntityClass( Account.class );
	}

	@Override
	public Account registerAccount(String email, String name, String lastname, String password) {
		if (accountExist(email)) {
            throw new AccountAlreadyExistException("Ya existe una cuenta registrada con esta dirección de email: " + email);
        }
		else {
			Account account= new Account();
			account.setEmail(email);
			account.setName(name);
			account.setLastname(lastname);
			account.setPassword(passwordEncoder.encode(password));
			account.setState(State.WAITING_EMAIL_CONFIRMATION);
			accountDao.create(account);
			return account;
		}		
	}

	@Override
	public void updateAccount(Account account) {
		accountDao.update(account);
	}

	@Override
	public Account findAccountById(String email) {
		return accountDao.findById(email);
	}
	
	@Override
	public State getAccountState(String email) {
		return accountDao.findById(email).getState();
	}
	
	@Override
	public void setAccountState(String email, State state) {
		Account account=accountDao.findById(email);
		account.setState(state);
		accountDao.update(account);
	}

	@Override
	public boolean accountExist(String email) {
		return accountDao.findById(email) != null;
	}

}
