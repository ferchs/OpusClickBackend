package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="provider")
@Getter
@Setter
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Provider implements Serializable, Comparable<Provider>{
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="phone_number")
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(name="availability")
	private Availability availability;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="experience")
	private int experience;
	
	@Column(name="about_me")
	private String aboutMe;
	
	@Column(name="opus_coins")
	private int opusCoins;
	
	@Column(name="work_done")
	private int workDone;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state", nullable = false)
	private State state;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name="fk_profession$provider")
	private Profession profession;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="fk_location$provider")
    private Location location;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="pk_account$provider")
	@MapsId
	private Account account;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="fk_global_rating$provider")
    private GlobalRating globalRating;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="provider", cascade = CascadeType.ALL)
	private Set<Certificate> certificates;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="provider", cascade = CascadeType.ALL)
	private Set<Work> works;
	
	public Provider() {
	}

	@Override
	public int compareTo(Provider o) {
		Double localRating = (this.globalRating.getGlobalRecommend() + this.globalRating.getGlobalSatisfactionLevel())/ 2;
		Double foreingRating = (o.globalRating.getGlobalRecommend() + o.globalRating.getGlobalSatisfactionLevel()) / 2;
		if (localRating > foreingRating) {
			return 1;
		} else if (localRating < foreingRating) {
			return -1;
		} else {
			return 0;
		}
	}
}
