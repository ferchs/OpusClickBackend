package com.espiritware.opusclick.dto;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.annotations.PasswordMatches;
import com.espiritware.opusclick.annotations.ValidEmail;
import com.espiritware.opusclick.model.Availability;
import com.espiritware.opusclick.model.City;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class ProviderRegistrationDto {
	
	@ValidEmail
	@NotNull
    @NotEmpty
	private String email;
	
	@NotNull
    @NotEmpty
	private String name;
	
	@NotNull
    @NotEmpty
	private String lastname;
	
	@NotNull
    @NotEmpty
	private String password;
	private String matchingPassword;
	
	@NotNull
    @NotEmpty
    private String providerPhone;
	
	@NotNull
	private Profession profession;
	
	@NotNull
	private City city;
	
	@JsonIgnore
    private final State state = State.WAITING_EMAIL_CONFIRMATION;
	
	@JsonIgnore
    private final Availability providerAvailability=Availability.AVAILABLE;
	
	@JsonIgnore
	private String photo="https://s3-sa-east-1.amazonaws.com/opusclick.com/provider-profile-images/default-profile-photo.png";
	
	@JsonIgnore
    private final int providerOpusCoins=0;

	@JsonIgnore
    private final double providerGlobalRatingGlobalSatisfactionLevel=100;
	
	@JsonIgnore
    private final double providerGlobalRatingGlobalRecommend=100;
	
	@JsonIgnore
    private final int providerGlobalRatingWorkDone=0;
	
	@JsonIgnore
    private final double providerGlobalRatingScore=0;
	
	@JsonIgnore
	private final Date providerSubscriptionLastPaymentDate= new Date();
	
	@JsonIgnore
	private final Date providerSubscriptionExpirationDate=getExpirationDate();
	
	@JsonIgnore
	private final State subscriptionState= State.ACTIVE;
	
	private Date getExpirationDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2020, 12, 31);
		return calendar.getTime();
	}
	
}
