package com.espiritware.opusclick.service;

import java.util.List;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.State;

public interface VisitService {
	
	Visit findVisitById(int id);
	
	List<Visit> findAllVisitsOfProvider(int idProvider);
	
	List<Visit> findAllVisitsOfUser(int idUser);

	List<Visit> findVisitsOfProviderByState(int idProvider, State state);

	List<Visit> findVisitsOfUserByState(int idUser, State state);
	
	List<Visit> findVisitsOfProviderByStates(int idProvider, String [] statesNames);

	List<Visit> findVisitsOfUserByStates(int idUser, String [] statesNames);

	List<Visit> findVisitsOfProviderByWork(int idWork, int idProvider);
	
	List<Visit> findVisitsOfUserByWork(int idWork, int idUser);
	
	List<Visit> findVisitsOfUserByWorkAndState(int idWork, int idUser, State state);
	
	List<Visit> findVisitsOfProviderByWorkAndState(int idWork, int idUser, State state);
	
	List<Visit> findVisitsOfUserByWorkAndStates(int idWork, int idUser, State [] states);
	
	List<Visit> findVisitsOfProviderByWorkAndStates(int idWork, int idUser, State [] states);
	
	void updateVisit(Visit visit);

	
}
