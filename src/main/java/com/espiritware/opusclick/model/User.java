package com.espiritware.opusclick.model;

import java.io.Serializable;
import java.util.Set;
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

@Entity
@Table(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="account_email")
	private String email;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="address")
	private String address;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="opus_coins")
	private int opusCoins;
	
	@Column(name="phone_number")
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(name="city")
	private City city;
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="account_email")
	private Account account;
	
	@OneToMany(mappedBy="user")
	private Set<Work> works;
	
	
	public User() {
	}
	
	public User(String email, String address, String password, 
			String phone, int opusCoins, State state) {
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.opusCoins = opusCoins;
		this.state=state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getOpusCoins() {
		return opusCoins;
	}

	public void setOpusCoins(int opusCoins) {
		this.opusCoins = opusCoins;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Set<Work> getWorks() {
		return works;
	}

	public void setWorks(Set<Work> works) {
		this.works = works;
	}
}
