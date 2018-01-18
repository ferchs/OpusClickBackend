package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="global_rating")
public class GlobalRating implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="fk_provider$global_rating")
	private String providerId;
	
	@Column(name="global_rating")
	private double globalRating;
	
	@Column(name="global_quality_work")
	private double globalQualityWork;
	
	@Column(name="global_fulfillment")
	private double fullfillment;
	
	@Column(name="global_customer_service")
	private double customerService;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_provider$global_rating")
	private Provider provider;
	
	public GlobalRating() {
	}
	
	public GlobalRating(String providerId) {
		this.providerId=providerId;
		this.globalRating=0.0;
		this.globalQualityWork=0.0;
		this.fullfillment=0.0;
		this.customerService=0.0;
	}
	
	public GlobalRating(String providerId, Double globalRating, Double globalQualityWork, Double fullfillment, Double customerService) {
		this.providerId=providerId;
		this.globalRating=globalRating;
		this.globalQualityWork=globalQualityWork;
		this.fullfillment=fullfillment;
		this.customerService=customerService;
	}

	public String getProviderId() {
		return providerId;
	}

	public double getGlobalRating() {
		return globalRating;
	}

	public void setGlobalRating(double globalRating) {
		this.globalRating = globalRating;
	}

	public double getGlobalQualityWork() {
		return globalQualityWork;
	}

	public void setGlobalQualityWork(double globalQualityWork) {
		this.globalQualityWork = globalQualityWork;
	}

	public double getFullfillment() {
		return fullfillment;
	}

	public void setFullfillment(double fullfillment) {
		this.fullfillment = fullfillment;
	}

	public double getCustomerService() {
		return customerService;
	}

	public void setCustomerService(double customerService) {
		this.customerService = customerService;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
}
