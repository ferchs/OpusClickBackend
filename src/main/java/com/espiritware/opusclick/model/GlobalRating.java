package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="global_rating")
@Getter
@Setter
public class GlobalRating implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_global_rating")
	private int id;
	
	@Column(name="global_satisfaction_level")
	private double globalSatisfactionLevel;
	
	@Column(name="global_recommend")
	private double globalRecommend;
	
	@Column(name="works_done")
	private int worksDone;
	
	@Column(name="score")
	@JsonIgnore
	private double score;
	
	
	public GlobalRating() {
	}
}
