package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="location")
public class Location implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="fk_provider$location")
	private String providerId;
	
	@Column(name="city")
	private String city;
	
	@Column(name="zone")
	private String zone;
	
	@Column(name="zip")
	private String zip;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_provider$location")
	private Provider provider;
	
	public Location () {
		
	}

	public String getProviderId() {
		return providerId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	
}
