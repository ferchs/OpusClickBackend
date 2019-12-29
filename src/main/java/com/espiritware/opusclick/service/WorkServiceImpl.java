package com.espiritware.opusclick.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.util.ReverseSortWorkByDate;

@Service("workService")
@Transactional
public class WorkServiceImpl implements WorkService{

	@Autowired
	private WorkService workService;
	
	GenericDao<Work, Integer> workDao;
	
	@Autowired
	public void setDao( GenericDao< Work,Integer> daoToSet ){
		workDao = daoToSet;
		workDao.setEntityClass( Work.class );
	}

	@Override
	public void createWork(Work work) {
		workDao.create(work);
	}

	@Override
	public void updateWork(Work work) {
		workDao.update(work);
	}

	@Override
	public Work findWorkById(int id) {
		return workDao.findById(id);
	}
	
	@Override
	public List<Work> findAllWorksOfUser(int idUser) {
		return workDao.findAllByField("fk_user$work", idUser+"");
	}

	@Override
	public List<Work> findAllWorksOfProvider(int idProvider) {
		return workDao.findAllByField("fk_provider$work", idProvider+"");
	}

	@Override
	public boolean workExist(int id) {
		return workDao.findById(id) != null;
	}

	@Override
	public State getWorkState(int id) {
		Work work = workDao.findById(id);
		return work.getState();
	}

	@Override
	public void setWorkState(int id, State state) {
		Work work = workDao.findById(id);
		work.setState(state);
		workDao.update(work);
	}

	@Override
	public List<Work> findWorksOfProviderByState(int idProvider, State state) {
		List<Work> providerWorksFiltered = new ArrayList<Work>();
		if (state != null) {
			List<Work> providerWorks = workService.findAllWorksOfProvider(idProvider);
			for (Work work : providerWorks) {
				if(work.getState().equals(state)) {
					providerWorksFiltered.add(work);
				}
			}
			providerWorksFiltered.sort(new ReverseSortWorkByDate());
			return providerWorksFiltered;
		}
		else {
			providerWorksFiltered.sort(new ReverseSortWorkByDate());
			return providerWorksFiltered;
		}
	}

	@Override
	public List<Work> findWorksOfUserByState(int idUser, State state) {
		List<Work> userWorksFiltered = new ArrayList<Work>();
		if (state != null) {
			List<Work> userWorks = workService.findAllWorksOfUser(idUser);
			for (Work work : userWorks) {
				if(work.getState().equals(state)) {
					userWorksFiltered.add(work);
				}
			}
			userWorksFiltered.sort(new ReverseSortWorkByDate());
			return userWorksFiltered;
		}
		else {
			userWorksFiltered.sort(new ReverseSortWorkByDate());
			return userWorksFiltered;
		}
	}

	@Override
	public List<Work> findWorksOfProviderByStates(int idProvider, String[] statesNames) {
		List<Work> filteredWorks=getAllWorksByStates("state", statesNames);
		List<Work> providerWorks = new ArrayList<Work>();
		if(filteredWorks!=null) {
			for(Work work : filteredWorks) {
				if(work.getProvider().getId()==idProvider) {
					providerWorks.add(work);
				}
			}
		}
		providerWorks.sort(new ReverseSortWorkByDate());
		return providerWorks;
	}

	@Override
	public List<Work> findWorksOfUserByStates(int idUser, String[] statesNames) {
		List<Work> filteredWorks= getAllWorksByStates("state",statesNames);
		List<Work> userWorks = new ArrayList<Work>();
		if(filteredWorks!=null) {
			for(Work work : filteredWorks) {
				if(work.getUser().getId()==idUser) {
					userWorks.add(work);
				}
			}
		}
		userWorks.sort(new ReverseSortWorkByDate());
		return userWorks;
	}
	
	
	private List<Work> getAllWorksByStates(String fieldName, String [] statesNames){
		State [] states = new State[statesNames.length];
		for (int i = 0; i < statesNames.length; i++) {
			states[i] = State.valueOf(statesNames[i]);
		}
		return workDao.getCurrentSession()
				.createQuery("from " + "Work" + " where " + fieldName + " in (:fieldValues)")
				.setParameterList("fieldValues", states).list();
	}

}
