package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.GlobalRating;

public interface GlobalRatingService {
	
	void create (GlobalRating globalRating);
	
	GlobalRating create (int id, double globalSatisfactionLevel, double globalRecommend, double score);
	
//	void deleteUserById(String email);
	
	void update (GlobalRating globalRating);
	
	void update (int id, double globalSatisfactionLevel, double globalRecommend, double score);
		
}
