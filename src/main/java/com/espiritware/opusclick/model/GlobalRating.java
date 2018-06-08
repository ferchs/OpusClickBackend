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
	private int globalRatingId;
	
	@Column(name="global_satisfaction_level")
	private double globalSatisfactionLevel;
	
	@Column(name="global_recommend")
	private double globalRecommend;
	
	@Column(name="score")
	private double score;
	
	
	public GlobalRating() {
	}
}
