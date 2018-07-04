package com.espiritware.opusclick.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;

@Service("workService")
@Transactional
public class WorkServiceImpl implements WorkService{

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
	public List<Work> findUserWorks(int idUser) {
		return workDao.findAllByField("fk_user$work", idUser+"");
	}

	@Override
	public List<Work> findProviderWorks(int idProvider) {
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

}
