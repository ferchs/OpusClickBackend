package com.espiritware.opusclick.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.dto.OpusClickInformation;
import com.espiritware.opusclick.dto.PersonalInformation;
import com.espiritware.opusclick.model.Availability;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.util.ReverseSortProviderByReputation;

@Service("providerService")
@Transactional
public class ProviderServiceImpl implements ProviderService{
	
	GenericDao< Provider,Integer > providerDao;
	
	@Autowired
	public void setDao( GenericDao< Provider,Integer > daoToSet ){
		providerDao = daoToSet;
		providerDao.setEntityClass( Provider.class );
	}

	@Override
	public Provider createProvider(String id, PersonalInformation personalInformation, Profession profession,
			Availability availability, OpusClickInformation opusClickInformation, State state, Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProvider(Provider provider) {
		providerDao.update(provider);		
	}

	@Override
	public Provider findProviderById(int id) {
		return providerDao.findById(id);
	}
	
	
	@Override
	public List<Provider> findProvidersByProfessionId(int id) {
		List<Provider> providers=providerDao.findAll();
		ArrayList professionals= new ArrayList();
		for(Provider provider:providers) {
			if(provider.getProfession().getProfessionId()==id) {
				professionals.add(provider);
			}
		}
		professionals.sort(new ReverseSortProviderByReputation());
		return professionals;
	}

	/*@Override
	public List<Provider> findProvidersByProfessionName(String profession) {
		List<Provider> providers=providerDao.findAll();
		ArrayList professionals= new ArrayList();
		for(Provider provider:providers) {
			if(provider.getProfession().getName().equalsIgnoreCase(profession)) {
				professionals.add(provider);
			}
		}
		return professionals;
	}*/

	@Override
	public List<Provider> findAllProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean providerExist(int id) {
		return providerDao.findById(id) != null;
	}

	@Override
	public State getProviderState(int id) {
		Provider provider = providerDao.findById(id);
		return provider.getState();
	}

	@Override
	public void setProviderState(int id, State state) {
		Provider provider = providerDao.findById(id);
		provider.setState(state);
		providerDao.update(provider);
	}

	@Override
	public boolean phoneExist(String phoneNumber) {
		return providerDao.findByField("phone", phoneNumber) != null;
	}


//	@Override
//	public Provider createProvider(String email, PersonalInformation personalInformation, Profession profession,
//			Availability availability, OpusClickInformation opusClickInformation, State state, Location location) {
//
//		if (providerExist(email)) {
//			throw new ProviderAlreadyExistException(
//					"Ya existe una cuenta registrada con esta dirección de email: " + email);
//		} else {
//			
//			Provider provider = new Provider();
//			provider.setProviderId(id);
//			if (personalInformation.getIdentificationNumber() != null) {
//				provider.setIdentificationNumber(personalInformation.getIdentificationNumber().toString());
//			} else {
//				provider.setIdentificationNumber(null);
//			}
//			if (personalInformation.getPhoneNumber() != null) {
//				provider.setPhone(personalInformation.getPhoneNumber().toString());
//			} else {
//				provider.setPhone(null);
//			}
//			if (personalInformation.getExperience() != null) {
//				provider.setExperience(personalInformation.getExperience().getExperienceData());
//			} else {
//				provider.setExperience(0);
//			}
//			if (personalInformation.getExperience() != null) {
//				provider.setAboutMe(personalInformation.getAboutMe().toString());
//			} else {
//				provider.setAboutMe(null);
//			}
//			if (opusClickInformation.getOpuscoins() != null) {
//				provider.setOpusCoins(opusClickInformation.getOpuscoins().getOpusCoinsAmount());
//			} else {
//				provider.setOpusCoins(0);
//			}
//			if (opusClickInformation.getWorkdones() != null) {
//				provider.setWorkDone(opusClickInformation.getWorkdones().getWorkDoneNumber());
//			} else {
//				provider.setWorkDone(0);
//			}
//			provider.setAvailability(availability);
//			provider.setState(state);
//			provider.setProfession(profession);
//			provider.setLocation(location);
//			providerDao.create(provider);
//			return provider;
//		}
//	}
//
//	@Override
//	public void updateProvider(Provider provider) {
//		providerDao.update(provider);
//	}
//
//	@Override
//	public Provider findProviderById(int email) {
//		return providerDao.findById(email);
//	}
//
//	@Override
//	public List<Provider> findProvidersByProfessionName(String profession) {
//		SessionFactory sf = (SessionFactory) providerDao.getSessionFactory();
//		return sf.getCurrentSession().createQuery(
//				"SELECT prov from Provider as prov INNER JOIN prov.profession as prof WHERE prof.name = :professionName")
//				.setParameter("professionName", profession).list();
//	}
//
//	@Override
//	public List<Provider> findAllProviders() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean providerExist(int email) {
//		return providerDao.findById(email) != null;
//	}
//
//	@Override
//	public State getProviderState(int email) {
//		Provider provider = providerDao.findById(email);
//		return provider.getState();
//	}
//
//	@Override
//	public void setProviderState(int email, State state) {
//		Provider provider = providerDao.findById(email);
//		provider.setState(state);
//		providerDao.update(provider);
//	}
	
}
