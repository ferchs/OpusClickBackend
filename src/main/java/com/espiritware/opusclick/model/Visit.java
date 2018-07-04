package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="visit")
@Getter
@Setter
public class Visit implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_visit")
	private int id;
	
	@Column(name="code")
	private String code;
	
	@Column(name="security_pin")
	private String securityPin;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="alternative_date")
	private Date alternativeDate;
	
	@Column(name="address")
	private String address;
	
	@Column(name="neighborhood")
	private String neighborhood;
	
	@Column(name="description")
	private String description;
	
	@Column(name="lower_limit")
	private Time lowerLimit;
	
	@Column(name="alternative_lower_limit")
	private Time alternativeLowerLimit;
	
	@Column(name="upper_limit")
	private Time upperLimit;
	
	@Column(name="alternative_upper_limit")
	private Time alternativeUpperLimit;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@Column(name="previous_state")
	private State previousState;
	
	@Column(name="state_changes")
	private String historyStateChanges;
	
	@Column(name="breach_description")
	private String breachDescription;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="fk_work$visit")
	private Work work;
	
}
