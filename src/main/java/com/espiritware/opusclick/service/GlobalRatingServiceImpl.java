package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.GlobalRating;

@Service("globalRatingService")
@Transactional
public class GlobalRatingServiceImpl implements GlobalRatingService{
	
	GenericDao<GlobalRating,Integer> globalRatingDao;
	
	@Autowired
	public void setDao( GenericDao< GlobalRating,Integer > daoToSet ){
		globalRatingDao = daoToSet;
		globalRatingDao.setEntityClass( GlobalRating.class );
	}

	@Override
	public void create(GlobalRating globalRating) {
		globalRatingDao.create(globalRating);
	}

	@Override
	public GlobalRating create(int id, double globalSatisfactionLevel, double globalRecommend, double score) {
		GlobalRating globalRating= new GlobalRating();
		globalRating.setGlobalRatingId(id);
		globalRating.setGlobalSatisfactionLevel(globalSatisfactionLevel);
		globalRating.setGlobalRecommend(globalRecommend);
		globalRating.setScore(score);
		create(globalRating);
		return globalRating;
	}

	@Override
	public void update(GlobalRating globalRating) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int id, double globalSatisfactionLevel, double globalRecommend, double score) {
		// TODO Auto-generated method stub
		
	}

}
