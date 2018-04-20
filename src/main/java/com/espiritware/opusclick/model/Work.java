package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="work")
public class Work implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_work")
	private String idWork;
	
	@Column(name="creation_date")
	private Date creationDate;
	
	@ManyToOne
	@JoinColumn(name="fk_user$work")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="fk_provider$work")
	private Provider provider;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	private Review review;
	
	@OneToMany(mappedBy="work")
	private Set<Visit> visits;
	
	public Work() {
		
	}

	public String getIdWork() {
		return idWork;
	}

	public void setIdWork(String idWork) {
		this.idWork = idWork;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Set<Visit> getVisit() {
		return visits;
	}

	public void setVisit(Set<Visit> visits) {
		this.visits = visits;
	}
	
}
