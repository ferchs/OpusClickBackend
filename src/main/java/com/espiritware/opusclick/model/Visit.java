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
import javax.persistence.OneToOne;
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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_work$visit")
	private Work work;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;

	public String getIdVisit() {
		return idVisit;
	}

	public void setIdVisit(String idVisit) {
		this.idVisit = idVisit;
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

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
}
