package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="work")
@Getter
@Setter
public class Work implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_work")
	private int workId;
	
	@Column(name="work_number")
	private String workNumber;
	
	@Column(name="creation_date")
	private Date creationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@Column(name="state_changes")
	private String historyStateChanges;
	
	@Column(name="comment")
	private String comment;
	
	@ManyToOne
	@JoinColumn(name="fk_user$work")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="fk_provider$work")
	private Provider provider;
	
	@OneToMany(mappedBy="work", cascade = CascadeType.ALL)
	private Set<Visit> visits;
	
	@OneToMany(mappedBy="work", cascade = CascadeType.ALL)
	private Set<OnlineQuote> quotes;
	
	@OneToMany(mappedBy="work", cascade = CascadeType.ALL)
	private Set<Review> reviews;
	
	public Work() {
		
	}	
}
