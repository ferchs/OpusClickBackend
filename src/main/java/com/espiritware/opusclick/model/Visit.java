package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="visit")
public class Visit implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_visit")
	private String idVisit;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="alternative_date")
	private Date alternativeDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@Column(name="address")
	private String address;
	
	@Column(name="neighborhood")
	private String neighborhood;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_work$visit")
	private Work work;

	public Visit(String idVisit, Date date, Date alternativeDate, State state, String address, String neighborhood,
			String description, Work work) {
		this.idVisit = idVisit;
		this.date = date;
		this.alternativeDate = alternativeDate;
		this.state = state;
		this.address = address;
		this.neighborhood = neighborhood;
		this.description = description;
		this.work = work;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getAlternativeDate() {
		return alternativeDate;
	}

	public void setAlternativeDate(Date alternativeDate) {
		this.alternativeDate = alternativeDate;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}
}
