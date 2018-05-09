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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="pk_account$user")
	private String userId;
	
	@Column(name="identification_number", unique = true)
	private String identificationNumber;
	
	@Column(name="phone_number")
	private String phone;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="opus_coins")
	private int opusCoins;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pk_account$user")
	private Account account;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_user$location")
	private Set<Location> locations;
	
	@OneToMany(mappedBy="user")
	private Set<Work> works;
	
	public User() {
	}
}
