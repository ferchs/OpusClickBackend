package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.InvalidTransaction;

@Service("invalidTransactionService")
@Transactional
public class InvalidTransactionServiceImpl implements InvalidTransactionService{

	GenericDao< InvalidTransaction,Integer > invalidTransactionDao;

	@Autowired
	public void setDao( GenericDao< InvalidTransaction,Integer> daoToSet ){
		invalidTransactionDao = daoToSet;
		invalidTransactionDao.setEntityClass( InvalidTransaction.class );
	}
	
	@Override
	public InvalidTransaction createInvalidTransaction(InvalidTransaction invalidTransaction) {
		return invalidTransactionDao.create(invalidTransaction);
	}

	@Override
	public InvalidTransaction findInvalidTransactionById(int id) {
		return invalidTransactionDao.findById(id);
	}

	@Override
	public InvalidTransaction updateInvalidTransaction(InvalidTransaction invalidTransaction) {
		return invalidTransactionDao.update(invalidTransaction);
	}

}
