package com.espiritware.opusclick.model;

import java.io.Serializable;
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
@Table(name="review")
@Getter
@Setter
public class Review implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_review")
	private int id;
	
	@Column(name="datetime")
	private Date datetime;
	
	@Column(name="satisfaction_level")
	private double satisfactionLevel;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private Type type;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="image")
	private String image;
	
	@Column(name="recommend")
	private boolean recommend;
	
	
	public Review() {
	}
}
