package com.espiritware.opusclick.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.Review;

@Service("reviewService")
@Transactional
public class ReviewServiceImpl implements ReviewService{

	GenericDao< Review,Integer > reviewDao;

	@Autowired
	public void setDao( GenericDao< Review,Integer> daoToSet ){
		reviewDao = daoToSet;
		reviewDao.setEntityClass( Review.class );
	}
	
	@Override
	public void createReview(Review review) {
		reviewDao.create(review);
	}

	@Override
	public void updateReview(Review review) {
		reviewDao.update(review);
	}

	@Override
	public Review findReviewById(int id) {
		return reviewDao.findById(id);
	}

	@Override
	public List<Review> findAllReviewsOfProvider(int idProvider) {
		// TODO Auto-generated method stub
		return null;
	}

}
