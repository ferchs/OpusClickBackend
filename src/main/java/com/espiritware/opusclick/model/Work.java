package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="work")
@Getter
@Setter
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Work implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_work")
	private int id;
	
	@Column(name="work_number")
	private String workNumber;
	
	@Column(name="provider_label")
	private String providerLabel;
	
	@Column(name="user_label")
	private String userLabel;
	
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
	
	@OneToOne(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Contract contract;
	
	@OneToMany(mappedBy="work", cascade = CascadeType.ALL)
	private Set<Visit> visits;
	
	@OneToOne(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private OnlineQuote onlineQuote;
	
	@OneToOne(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private ProviderQuote providerQuote;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="fk_review$work")
	private Review review;
	
	
	public Work() {
		
	}	
}
