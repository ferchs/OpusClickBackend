package com.espiritware.opusclick.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="bill")
@Getter
@Setter
public class Bill {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_bill")
	private int id;
	
	@Column(name="bill_number")
	private String billNumber;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="payment_description")
	private String paymentDescription;
	
	@Column(name="value")
	private double value;
	
	@Enumerated(EnumType.STRING)
	@Column(name="transaction_state")
	private State transactionState;
	
	@Column(name="authorization_code")
	private String authorizationCode;
	
	@Column(name="transaction_number")
	private String transactionNumber;
	
	@Column(name="payment_method")
	private String paymentMethod;
	
}
