package com.espiritware.opusclick.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="milestone")
@Getter
@Setter
public class Milestone {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_contract")
	private long idMilestone;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="description")
	private String description;
	
	@Column(name="value")
	private double value;
	
	@Column(name="exp_guarantee")
	private Date expirationGuarantee;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@ManyToOne
	@JoinColumn(name="fk_contract$milestone")
	private Contract contract;

}
