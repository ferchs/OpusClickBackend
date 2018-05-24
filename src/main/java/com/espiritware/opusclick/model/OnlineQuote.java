package com.espiritware.opusclick.model;

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
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="online_quote")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Getter
@Setter
public class OnlineQuote {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id_quote")
	private long quoteId;
	
	@Type(type = "json")
    @Column(columnDefinition = "json", name="quote_requirements")
	private String requirements;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
	private State state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fk_work$visit")
	private Work work;
}
