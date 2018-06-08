package com.espiritware.opusclick.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.Profession;

@Service("professionService")
@Transactional
public class ProfessionServiceImpl implements ProfessionService {

	GenericDao<Profession,Long> professionDao;
	
	@Autowired
	public void setDao( GenericDao<Profession,Long> daoToSet ){
		professionDao = daoToSet;
		professionDao.setEntityClass( Profession.class );
	}

	@Override
	public Profession findProfessionById(long id) {
		return professionDao.findById(id);
	}

	@Override
	public Profession findProfessionByName(String professionName) {
		return professionDao.findByField("name",professionName);
	}

	@Override
	public List<Profession> getAllProfessions() {
		return professionDao.findAll();
	}

}
