package com.espiritware.opusclick.service;

import java.util.List;

import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;

public interface WorkService {

	void createWork(Work work);
			
	void updateWork(Work user);
	
	Work findWorkById(int id);
	
	List<Work> findUserWorks(int idUser);
	
	List<Work> findProviderWorks(int idProvider);

	boolean workExist(int id);
	
	State getWorkState(int id);
	
	void setWorkState(int id, State state);
}
