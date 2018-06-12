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
	
	Provider findProviderById(int id);
			
	List<Provider> findProvidersByProfessionName(String profession);
	
	List<Provider> findAllProviders();
	
	boolean providerExist(int id);
	
	boolean phoneExist(String phoneNumber);
	
	State getProviderState(int id);
	
	void setProviderState(int id, State state);

}
