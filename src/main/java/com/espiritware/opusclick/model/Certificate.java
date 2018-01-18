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

@Entity 
@Table(name="certificate")
public class Certificate implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_certificate")
	private int idCertificate;
	
	@Column(name="name")
	private String name;
	
	@Column(name="url")
	private String url;
	
	@ManyToOne
	@JoinColumn(name="fk_provider$certificate")
	private Provider provider;
	
	public Certificate() {
		
	}

	public int getIdCertificate() {
		return idCertificate;
	}

	public void setIdCertificate(int idCertificate) {
		this.idCertificate = idCertificate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

}
