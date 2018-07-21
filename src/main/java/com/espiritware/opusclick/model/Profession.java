package com.espiritware.opusclick.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="profession")
@Getter
@Setter
public class Profession implements Serializable, Comparable<Profession> {
	
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_profession")
	private int professionId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="form_id")
	private String formId;
	
	@Override
	public int compareTo(Profession o) {
		return this.getName().compareTo(o.getName());
	}
}
