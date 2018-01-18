package com.espiritware.opusclick.service;

import java.util.List;
import com.espiritware.opusclick.dto.ProviderDto;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.State;

public interface ProviderService {

	Provider registerProvider(ProviderDto userDto);
	
	void updateProvider(Provider provider);
	
	Provider findProviderById(String email);
		
	List<Provider> findProvidersByProfession(String profession);
	
	List<Provider> findAllProviders();
	
	boolean providerExist(String email);
	
	State getProviderState(String email);
	
	void setProviderState(String email, State state);

}
