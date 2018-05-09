package com.espiritware.opusclick.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
@Table(name="comment")
@Getter
@Setter
public class Comment {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_comment")
	private long commentId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private Type type;
	
	@Column(name="description")
	private String description;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_review$comment")
	private Review review;
	
	public Comment() {
		
	}
}
