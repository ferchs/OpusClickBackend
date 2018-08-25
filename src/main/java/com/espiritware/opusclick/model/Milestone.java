package com.espiritware.opusclick.model;

import java.util.Date;
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
@Table(name="milestone")
@Getter
@Setter
public class Milestone {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_milestone")
	private int id;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@Column(name="state_changes")
	private String historyStateChanges;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="fk_item$milestone")
    private Item item;
	
}
