package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="global_rating")
@Getter
@Setter
public class GlobalRating implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="pk_provider$global_rating")
	private String globalRatingId;
	
	@Column(name="global_satisfaction_level")
	private double globalSatisfactionLevel;
	
	@Column(name="global_recommend")
	private double globalRecommend;
	
	public GlobalRating() {
	}
	
	public GlobalRating(String globalRatingId) {
		this.globalRatingId=globalRatingId;
		this.globalSatisfactionLevel=0.0;
		this.globalRecommend=0.0;
	}
	
	public GlobalRating(String globalRatingId, Double globalSatisfactionLevel, Double globalRecommend) {
		this.globalRatingId=globalRatingId;
		this.globalSatisfactionLevel=globalSatisfactionLevel;
		this.globalRecommend=globalRecommend;
	}

}
