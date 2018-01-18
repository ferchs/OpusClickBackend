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

@Entity
@Table(name="provider")
public class Provider implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="account_email")
	private String email;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="phone_number")
	private String phone;
	
	@Column(name="experience")
	private int experience;
	
	@Column(name="about_me")
	private String aboutMe;
	
	@Column(name="work_done")
	private int workDone;
	
	@Enumerated(EnumType.STRING)
	@Column(name="city")
	private City city;
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name="opus_coins")
	private int opusCoins;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="account_email")
	private Account account;
	
	@Enumerated(EnumType.STRING)
	@Column(name="availability")
	private Availability availability;
	
	@Enumerated(EnumType.STRING)
	@Column(name="profession")
	private Profession profession;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@OneToMany(mappedBy="provider")
	private Set<Certificate> certificates;
	
	@OneToMany(mappedBy="provider")
	private Set<Work> works;
	
	@OneToOne(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private GlobalRating globalRating;
	
	public Provider() {
	}

	public Provider(String identificationNumber, String email, String phone, Profession profession,
			GlobalRating globalRating, State state) {
		this.identificationNumber = identificationNumber;
		this.email = email;
		this.phone = phone;
		this.profession = profession;
		this.globalRating = globalRating;
		this.state=state;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public int getWorkDone() {
		return workDone;
	}

	public void setWorkDone(int workDone) {
		this.workDone = workDone;
	}

	public int getOpusCoins() {
		return opusCoins;
	}

	public void setOpusCoins(int opusCoins) {
		this.opusCoins = opusCoins;
	}

	public Availability getAvailability() {
		return availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public Profession getProfession() {
		return profession;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public Set<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(Set<Certificate> certificates) {
		this.certificates = certificates;
	}

	public Set<Work> getWorks() {
		return works;
	}

	public void setWorks(Set<Work> works) {
		this.works = works;
	}

	public GlobalRating getGlobalRating() {
		return globalRating;
	}

	public void setGlobalRating(GlobalRating globalRating) {
		this.globalRating = globalRating;
	}

}
