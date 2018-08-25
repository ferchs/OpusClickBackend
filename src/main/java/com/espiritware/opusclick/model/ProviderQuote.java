package com.espiritware.opusclick.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="provider_quote")
@Getter
@Setter
public class ProviderQuote {

	@Id
	private int id;
	
	@Column(name="quotation_number")
	private String number;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="clarifications")
	private String clarifications;
	
	@Column(name="subtotal")
	private double subtotal;

	@Column(name="administration_fee")
	private double administrationFee;
	
	@Column(name="total")
	private double total;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name="fk_provider_quote$item", nullable=false)
	private Set<Item> items;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="pk_work$provider_quote")
	@MapsId
	private Work work;
	
}
