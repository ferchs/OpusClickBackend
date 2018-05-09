package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.error.AccountAlreadyExistException;
import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.State;

@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService{
	
	GenericDao<Account,String> accountDao;
	
	@Autowired
	public void setDao( GenericDao< Account,String > daoToSet ){
		accountDao = daoToSet;
		accountDao.setEntityClass( Account.class );
	}
	
	@Override
	public void createAccount(Account account) {
		accountDao.create(account);
	}
	
	
	@Override
	public Account createAccount(String email, String name, String lastname, String password) {
		if (accountExist(email)) {
            throw new AccountAlreadyExistException("Ya existe una cuenta registrada con esta dirección de email: " + email);
        }
		else {
			Account account= new Account();
			account.setEmail(email);
			account.setName(name);
			account.setLastname(lastname);
			account.setPassword(password);
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
	
	@Override
	public boolean accountConfirmed(String email) {
		Account account= new Account();
		account=accountDao.findById(email);
		if(account.getState().equals(State.ACCOUNT_CONFIRMED)) {
			return true;
		}else {
			return false;
		}
	}
}
