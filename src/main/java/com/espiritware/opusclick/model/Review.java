package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="review")
public class Review implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="fk_work$review")
	private String idReview;
	
	@Column(name="datetime")
	private Date datetime;
	
	@Column(name="quality_work")
	private Double qualityWork;
	
	@Column(name="fulfillment")
	private Double fulfillment;

	@Column(name="customer_service")
	private Double customerService;
	
	@Column(name="commentary")
	private Double commentary;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_work$review")
	private Work work;
	
	@OneToMany(mappedBy="review")
	private Set<CompletedWorkImage> completedWorkImages;
	
	public Review() {
	}

	public String getIdReview() {
		return idReview;
	}

	public void setIdReview(String idReview) {
		this.idReview = idReview;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Double getQualityWork() {
		return qualityWork;
	}

	public void setQualityWork(Double qualityWork) {
		this.qualityWork = qualityWork;
	}

	public Double getFulfillment() {
		return fulfillment;
	}

	public void setFulfillment(Double fulfillment) {
		this.fulfillment = fulfillment;
	}

	public Double getCustomerService() {
		return customerService;
	}

	public void setCustomerService(Double customerService) {
		this.customerService = customerService;
	}

	public Double getCommentary() {
		return commentary;
	}

	public void setCommentary(Double commentary) {
		this.commentary = commentary;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public Set<CompletedWorkImage> getCompletedWorkImages() {
		return completedWorkImages;
	}

	public void setCompletedWorkImages(Set<CompletedWorkImage> completedWorkImages) {
		this.completedWorkImages = completedWorkImages;
	}
	
}
