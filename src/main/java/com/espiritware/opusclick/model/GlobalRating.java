package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="global_rating")
public class GlobalRating implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private String providerId;
	
	@Column(name="global_satisfaction_level")
	private double globalSatisfactionLevel;
	
	@Column(name="global_recommend")
	private double globalRecommend;
	
	public GlobalRating() {
	}
	
	public GlobalRating(String providerId) {
		this.providerId=providerId;
		this.globalSatisfactionLevel=0.0;
		this.globalRecommend=0.0;
	}
	
	public GlobalRating(String providerId, Double globalSatisfactionLevel, Double globalRecommend) {
		this.providerId=providerId;
		this.globalSatisfactionLevel=globalSatisfactionLevel;
		this.globalRecommend=globalRecommend;
	}

	public String getProviderId() {
		return providerId;
	}

	public double getGlobalSatisfactionLevel() {
		return globalSatisfactionLevel;
	}

	public void setGlobalRecommend(double globalRecommend) {
		this.globalRecommend = globalRecommend;
	}	
}
