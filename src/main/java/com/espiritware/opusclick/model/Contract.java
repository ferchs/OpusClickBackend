package com.espiritware.opusclick.model;

import java.util.Date;
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="contract")
@Getter
@Setter
public class Contract {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_contract")
	private int id;
	
	@Column(name="contract_number")
	private String contractNumber;
	
	@Column(name="creation_date")
	private Date creationDate;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="clarifications")
	private String clarifications;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@Column(name="state_changes")
	private String historyStateChanges;
	
	@Column(name="subtotal_value")
	private double subtotal;

	@Column(name="administration_fee")
	private double administrationFee;
	
	@Column(name="total_value")
	private Double totalValue;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contract$milestone", nullable=false)
	private Set<Milestone> milestones;
	
	@OneToOne
    @JoinColumn(name="fk_work$contract")
    private Work work;
}
