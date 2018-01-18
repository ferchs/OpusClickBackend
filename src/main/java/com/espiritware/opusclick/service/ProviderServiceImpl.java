package com.espiritware.opusclick.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.dto.ProviderDto;
import com.espiritware.opusclick.error.ProviderAlreadyExistException;
import com.espiritware.opusclick.model.Availability;
import com.espiritware.opusclick.model.City;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.State;

@Service("providerService")
@Transactional
public class ProviderServiceImpl implements ProviderService{

	GenericDao< Provider > providerDao;
	
	@Autowired
	public void setDao( GenericDao< Provider > daoToSet ){
		providerDao = daoToSet;
		providerDao.setEntityClass( Provider.class );
	}
	
	@Override
	public Provider registerProvider(ProviderDto providerDto) {
		if (providerExist(providerDto.getEmail())) {
			throw new ProviderAlreadyExistException(
					"Ya existe una cuenta registrada con esta dirección de email: " + providerDto.getEmail());
		} else {
			Provider provider = new Provider();
			provider.setEmail(providerDto.getEmail());
			provider.setProfession(Profession.valueOf(providerDto.getProfession()));
			provider.setCity(City.valueOf(providerDto.getCity()));
			provider.setOpusCoins(10);
			provider.setState(State.WAITING_EMAIL_CONFIRMATION);
			provider.setAvailability(Availability.AVAILABLE);
			providerDao.create(provider);
			return provider;
		}
	}

	@Override
	public void updateProvider(Provider provider) {
		providerDao.update(provider);
	}

	@Override
	public Provider findProviderById(String email) {
		return providerDao.findById(email);
	}

	@Override
	public List<Provider> findProvidersByProfession(String profession) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Provider> findAllProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean providerExist(String email) {
		return providerDao.findById(email) != null;
	}

	@Override
	public State getProviderState(String email) {
		Provider provider = providerDao.findById(email);
		return provider.getState();
	}

	@Override
	public void setProviderState(String email, State state) {
		Provider provider = providerDao.findById(email);
		provider.setState(state);
		providerDao.update(provider);
	}
	
}
