package com.espiritware.opusclick.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="contract")
public class Contract {

	@Id
	@JoinColumn(name="fk_work_contract")
	private String idContract;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="total_value")
	private Double totalValue;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;

	public Contract(String idContract, Date startDate, Date endDate, Double totalValue, State state) {
		super();
		this.idContract = idContract;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalValue = totalValue;
		this.state = state;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
