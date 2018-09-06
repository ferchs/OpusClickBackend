package com.espiritware.opusclick.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderGetContractReviewDto {
	
	private String contractName;
	
	private String userName;
	
	private String userLastname;
	
	private Date date;
	
	private double reviewSatisfactionLevel;
	
	private String reviewComment;
	
	private String reviewImage;
	
}
