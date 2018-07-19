package com.espiritware.opusclick.dto;

public class ProviderGetByProfessionDto {
	
	public int id;
	
	public String accountName;
	
	public String accountLastname;

	public String photo;
	
	public double globalRatingGlobalSatisfactionLevel;
	
	public double globalRatingGlobalRecommend;
	
	public double globalNoRecommend=100-globalRatingGlobalRecommend;
}
