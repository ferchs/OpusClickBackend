package com.espiritware.opusclick.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="item")
@Getter
@Setter
public class Item {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_item")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private double value;
	
	@Column(name="duration_value")
	private double durationValue;
	
	@Column(name="duration_time")
	private String durationTime;
	
	@Column(name="warranty_value")
	private double warrantyValue;
	
	@Column(name="warranty_time")
	private String warrantyTime;
	
	@Column(name="work_description")
	private String workDescription;
	
	@Column(name="warranty_description")
	private String warrantyDescription;
	
	@Column(name="image_quote")
	private String imageQuote;
	
	@Column(name="image_contract")
	private String imageContract;
	
	@Column(name="comment_quote")
	private String commentQuote;
	
	@Column(name="comment_contract")
	private String commentContract;
	
}
