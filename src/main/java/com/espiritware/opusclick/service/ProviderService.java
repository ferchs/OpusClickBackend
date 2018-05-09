package com.espiritware.opusclick.service;

import java.util.List;

import com.espiritware.opusclick.dto.OpusClickInformation;
import com.espiritware.opusclick.dto.PersonalInformation;
import com.espiritware.opusclick.model.Availability;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.State;

public interface ProviderService {

	Provider createProvider(String id, PersonalInformation personalInformation, Profession profession,
			Availability availability, OpusClickInformation opusClickInformation, State state, Location location);
	
	void updateProvider(Provider provider);
	
	Provider findProviderById(String email);
		
	List<Provider> findProvidersByProfessionName(String profession);
	
	List<Provider> findAllProviders();
	
	boolean providerExist(String email);
	
	State getProviderState(String email);
	
	void setProviderState(String email, State state);

}
