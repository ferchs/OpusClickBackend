package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="review")
public class Review implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private String idReview;
	
	@Column(name="datetime")
	private Date datetime;
	
	@Column(name="satisfaction_level")
	private Double satisfactionLevel;
	
	@Column(name="recommend")
	private Boolean recommend;
		
	@OneToMany(mappedBy="review")
	private Set<CompletedWorkImage> completedWorkImages;
	
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	private Comment comment;
	
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

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public Set<CompletedWorkImage> getCompletedWorkImages() {
		return completedWorkImages;
	}

	public void setCompletedWorkImages(Set<CompletedWorkImage> completedWorkImages) {
		this.completedWorkImages = completedWorkImages;
	}
	
}
