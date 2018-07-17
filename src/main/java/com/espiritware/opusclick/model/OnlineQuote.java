package com.espiritware.opusclick.model;

import java.util.Date;
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
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="online_quote")
//@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Getter
@Setter
public class OnlineQuote {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_online_quote")
	private int id;
	
	@Column(name="quotation_number")
	private String quotationNumber;
	
	@Column(name="date")
	private Date date;
	
	@Type(type = "json")
    @Column(columnDefinition = "json", name="quote_requirements")
	private JsonNode requirements;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="fk_work$online_quote")
	private Work work;
}
