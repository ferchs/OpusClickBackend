package com.espiritware.opusclick.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="online_quote")
@Getter
@Setter
public class OnlineQuote {
	
	@Id
	private int id;
	
	@Column(name="quotation_number")
	private String quotationNumber;
	
	@Column(name="date")
	private Date date;
	
    @Column(name="quote_requirements")
	private String requirements;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="pk_work$online_quote")
	@MapsId
	private Work work;
	
}
