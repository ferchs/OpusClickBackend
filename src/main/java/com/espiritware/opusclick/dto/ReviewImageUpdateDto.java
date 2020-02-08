package com.espiritware.opusclick.dto;

import java.util.Date;
import javax.persistence.Id;
import com.espiritware.opusclick.model.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewImageUpdateDto {
	
	@Id
	private int reviewId;
	
	private Date datetime;
	
	private double satisfactionLevel;
	
	private Type type;
	
	private String comment;
	
	private String image;
	
	private boolean recommend;

}
