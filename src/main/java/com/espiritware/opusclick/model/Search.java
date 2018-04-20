package com.espiritware.opusclick.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="search")
public class Search {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_search")
	private int SearchId;
	
	@Column(name="description")
	private String description;

	public Search(int searchId, String description) {
		SearchId = searchId;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
