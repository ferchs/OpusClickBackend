package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.City;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.User;

@Service("locationService")
@Transactional
public class LocationServiceImpl implements LocationService {

	GenericDao<Location,Long> locationDao;
		
	@Autowired
	public void setDao( GenericDao<Location,Long> daoToSet ){
		locationDao = daoToSet;
		locationDao.setEntityClass( Location.class );
	}
	
	@Override
	public Location createLocation(String address, String zipCode, String zone, City city) {
		Location location= new Location();
		location.setAddress(address);
		location.setZipCode(zipCode);
		location.setZone(zone);
		location.setCity(city);
		locationDao.create(location);
		return location;
	}

	@Override
	public void updateLocation(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location findLocationById(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
