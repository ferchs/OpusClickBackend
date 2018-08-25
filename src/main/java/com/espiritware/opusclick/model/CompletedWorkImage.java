package com.espiritware.opusclick.model;

import java.io.Serializable;
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
@Table(name="completed_work_image")
@Getter
@Setter
public class CompletedWorkImage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
    @Column(name="id_completed_work_image")
	private int completedWorkImageId;
	
	@Column(name="url")
	private String url;
	
	@ManyToOne
	@JoinColumn(name="fk_review$completed_work_image")
	private Review review;
	
	public CompletedWorkImage() {
		
	}
}
