package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="completed_work_image")
public class CompletedWorkImage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_completed_work_image")
	private int completedWorkImageId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="url")
	private String url;
	
	@ManyToOne
	@JoinColumn(name="fk_review$completed_work_image")
	private Review review;
	
	public CompletedWorkImage() {
		
	}

	public int getCompletedWorkImageId() {
		return completedWorkImageId;
	}

	public void setCompletedWorkImageId(int completedWorkImageId) {
		this.completedWorkImageId = completedWorkImageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

}
