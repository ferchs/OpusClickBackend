package com.espiritware.opusclick.service;

import java.util.List;
import com.espiritware.opusclick.model.Review;

public interface ReviewService {
	
	void createReview(Review review);
	
	void updateReview(Review review);
	
	Review findReviewById(int id);
	
	List<Review> findAllReviewsOfProvider(int idProvider);
}
