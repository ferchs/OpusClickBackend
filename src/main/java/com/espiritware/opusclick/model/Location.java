package com.espiritware.opusclick.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="location")
@Getter
@Setter
public class Location implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_location")
	private int id;
	
	@Column(name="address")
	private String address;
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name="zone")
	private String zone;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="fk_city$location")
	private City city;
	
	public Location () {
		
	}
}
