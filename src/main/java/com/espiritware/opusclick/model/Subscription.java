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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="subscription")
@Getter
@Setter
public class Subscription {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_subscription")
	private int id;
	
	@Column(name="last_payment_date")
	private Date lastPaymentDate;
	
	@Column(name="expiration_date")
	private Date expirationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@OneToOne
	@JoinColumn(name="fk_bill$subscription")
	private Bill bill;
	
}
