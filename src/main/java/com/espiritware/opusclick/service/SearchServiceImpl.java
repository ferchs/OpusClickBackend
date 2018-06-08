package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.Search;

@Service("searchService")
@Transactional
public class SearchServiceImpl implements SearchService{
	
	GenericDao< Search,Long > searchDao;

	@Autowired
	public void setDao( GenericDao< Search,Long > daoToSet ){
		searchDao = daoToSet;
		searchDao.setEntityClass( Search.class );
	}
	
	@Override
	public void createSearch(Search search) {
		searchDao.create(search);
	}
	
}
