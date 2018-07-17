package com.espiritware.opusclick.service;

import java.util.List;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;

public interface WorkService {

	void createWork(Work work);
			
	void updateWork(Work user);
	
	Work findWorkById(int id);
	
	List<Work> findAllWorksOfProvider(int idProvider);
	
	List<Work> findAllWorksOfUser(int idUser);

	List<Work> findWorksOfProviderByState(int idProvider, State state);

	List<Work> findWorksOfUserByState(int idUser, State state);
	
	List<Work> findWorksOfProviderByStates(int idProvider, String [] statesNames);

	List<Work> findWorksOfUserByStates(int idUser, String [] statesNames);

	boolean workExist(int id);
	
	State getWorkState(int id);
	
	void setWorkState(int id, State state);
}
