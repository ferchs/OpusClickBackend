package com.espiritware.opusclick.service;

import org.hibernate.Session;
import com.espiritware.opusclick.model.Contract;

public interface ContractService {

	Contract createContract(Contract contract);

	Contract findContractById(int id);

	Contract updateContract(Contract contract);
	
	void deleteContract(Contract contract);
	
	//List<Contract> findAllContractsOfUser(int idUser);
		
	Session getCurrentSession();
}
