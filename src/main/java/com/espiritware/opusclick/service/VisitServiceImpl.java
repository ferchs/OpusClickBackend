package com.espiritware.opusclick.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;

@Service("visitService")
@Transactional
public class VisitServiceImpl implements VisitService{

	@Autowired
	private WorkService workService;
	
	GenericDao<Visit, Integer> visitDao;
	
	@Autowired
	public void setDao( GenericDao< Visit, Integer> daoToSet ){
		visitDao = daoToSet;
		visitDao.setEntityClass( Visit.class );
	}

	@Override
	public Visit findVisitById(int id) {
		return visitDao.findById(id);
	}
	
	@Override
	public List<Visit> findAllVisitsOfProvider(int idProvider) {
		return null;
	}

	@Override
	public List<Visit> findAllVisitsOfUser(int idUser) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Visit> findVisitsOfProviderByState(int idProvider, State state) {
		List<Visit> providerVisits = new ArrayList<Visit>();
		if (state!=null) {
			List<Work> providerWorks = workService.findProviderWorks(idProvider);
			for (Work work : providerWorks) {
				for (Visit visit : work.getVisits()) {
					if (visit.getState().equals(state)) {
						providerVisits.add(visit);
					}
				}
			}
			return providerVisits;
		} else {
			List<Work> providerWorks = workService.findProviderWorks(idProvider);
			for (Work work : providerWorks) {
				for (Visit visit : work.getVisits()) {
					providerVisits.add(visit);
				}
			}
			return providerVisits;
		}
	}

	@Override
	public List<Visit> findVisitsOfUserByState(int idUser, State state) {
		List<Visit> userVisits = new ArrayList<Visit>();
		if (state!=null) {
			List<Work> userWorks = workService.findUserWorks(idUser);
			for (Work work : userWorks) {
				for (Visit visit : work.getVisits()) {
					if (visit.getState().equals(state)) {
						userVisits.add(visit);
					}
				}
			}
			return userVisits;
		} else {
			List<Work> userWorks = workService.findUserWorks(idUser);
			for (Work work : userWorks) {
				for (Visit visit : work.getVisits()) {
					userVisits.add(visit);
				}
			}
			return userVisits;
		}
	}
	
	@Override
	public List<Visit> findVisitsOfProviderByStates(int idProvider, String[] statesNames) {
		List<Visit> filteredVisits=getAllVisitsByStates("state", statesNames);
		List<Visit> providerVisits = new ArrayList<Visit>();
		if(filteredVisits!=null) {
			for(Visit visit : filteredVisits) {
				if(visit.getWork().getProvider().getId()==idProvider) {
					providerVisits.add(visit);
				}
			}
		}
		return providerVisits;
	}

	@Override
	public List<Visit> findVisitsOfUserByStates(int idUser, String [] statesNames) {
		List<Visit> filteredVisits= getAllVisitsByStates("state",statesNames);
		List<Visit> userVisits = new ArrayList<Visit>();
		if(filteredVisits!=null) {
			for(Visit visit : filteredVisits) {
				int userId=visit.getWork().getUser().getId();
				if(visit.getWork().getUser().getId()==idUser) {
					userVisits.add(visit);
				}
			}
		}
		return userVisits;
	}
	
	private List<Visit> getAllVisitsByStates(String fieldName, String [] statesNames){
		State [] states = new State[statesNames.length];
		for (int i = 0; i < statesNames.length; i++) {
			states[i] = State.valueOf(statesNames[i]);
		}
		
		return visitDao.getCurrentSession()
				.createQuery("from " + "Visit" + " where " + fieldName + " in (:fieldValues)")
				.setParameterList("fieldValues", states).list();
	}

	@Override
	public List<Visit> findVisitsOfProviderByWork(int idWork, int idProvider) {
		List<Visit> visits = visitDao.findAllByField("fk_work$visit", idWork + "");
		List<Visit> providerVisits = visits;
		for (int i = 0; i < visits.size(); i++) {
			if (visits.get(i).getWork() != null) {
				if (visits.get(i).getWork().getProvider().getId() != idProvider) {
					providerVisits.remove(i);
				}
			}
		}
		return providerVisits;
	}
	


	@Override
	public List<Visit> findVisitsOfUserByWork(int idWork, int idUser) {
		List<Visit> visits = visitDao.findAllByField("fk_work$visit", idWork + "");
		List<Visit> userVisits = visits;
		for (int i = 0; i < visits.size(); i++) {
			if (visits.get(i).getWork() != null) {
				if (visits.get(i).getWork().getProvider().getId() != idUser) {
					userVisits.remove(i);
				}
			}
		}
		return userVisits;
	}

	@Override
	public void updateVisit(Visit visit) {
		visitDao.update(visit);
	}

	@Override
	public List<Visit> findVisitsOfUserByWorkAndState(int idWork, int idUser, State state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Visit> findVisitsOfProviderByWorkAndState(int idWork, int idUser, State state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Visit> findVisitsOfUserByWorkAndStates(int idWork, int idUser, State[] states) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Visit> findVisitsOfProviderByWorkAndStates(int idWork, int idUser, State[] states) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
