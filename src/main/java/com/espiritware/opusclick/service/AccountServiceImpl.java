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
	
	GenericDao<Account, Integer> accountDao;
	
	@Autowired
	public void setDao( GenericDao< Account,Integer> daoToSet ){
		accountDao = daoToSet;
		accountDao.setEntityClass( Account.class );
	}
	
	@Override
	public Account createAccount(Account account) {
		return accountDao.create(account);
	}
	
	
	@Override
	public Account createAccount(int id,String email, String name, String lastname, String password) {
		if (accountExist(id)) {
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
	public Account updateAccount(Account account) {
		return accountDao.update(account);
	}

	@Override
	public Account findAccountById(int id) {
		return accountDao.findById(id);
	}
	
	@Override
	public Account findAccountByEmail(String email) {
		return accountDao.findByField("email", email);
	}
	
	@Override
	public State getAccountState(String email) {
		return accountDao.findByField("email", email).getState();
	}
	
	@Override
	public State getAccountState(int id) {
		return accountDao.findById(id).getState();
	}
	
	@Override
	public void setAccountState(String email, State state) {
		Account account=accountDao.findByField("email", email);
		account.setState(state);
		accountDao.update(account);
	}
	
	@Override
	public void setAccountState(int id, State state) {
		Account account=accountDao.findById(id);
		account.setState(state);
		accountDao.update(account);
	}

	@Override
	public boolean accountExist(String email) {
		return accountDao.findByField("email", email) != null;
	}
	
	@Override
	public boolean accountExist(int id) {
		return accountDao.findById(id) != null;
	}
	
	@Override
	public boolean accountConfirmed(String email) {
		Account account= new Account();
		account=accountDao.findByField("email", email);
		if(account.getState().equals(State.ACCOUNT_CONFIRMED)) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public boolean accountConfirmed(int id) {
		Account account= new Account();
		account=accountDao.findById(id);
		if(account.getState().equals(State.ACCOUNT_CONFIRMED)) {
			return true;
		}else {
			return false;
		}
	}
}
