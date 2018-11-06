package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.Contract;

@Service("contractService")
@Transactional
public class ContractServiceImpl implements ContractService {

	GenericDao<Contract,Integer> contractDao;
	
	@Autowired
	public void setDao( GenericDao<Contract,Integer> daoToSet ){
		contractDao = daoToSet;
		contractDao.setEntityClass( Contract.class );
	}

	@Override
	public Contract createContract(Contract contract) {
		// TODO Auto-generated method stub
		return contractDao.create(contract);
	}

	@Override
	public Contract findContractById(int id) {
		// TODO Auto-generated method stub
		return contractDao.findById(id);
	}

	@Override
	public Contract updateContract(Contract contract) {
		// TODO Auto-generated method stub
		return contractDao.update(contract);
	}

	@Override
	public Session getCurrentSession() {
		return contractDao.getCurrentSession();
	}

	@Override
	public void deleteContract(Contract contract) {
		// TODO Auto-generated method stub
		 contractDao.delete(contract);
	}
}
