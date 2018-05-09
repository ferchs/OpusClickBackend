package com.espiritware.opusclick.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.City;

@Service("cityService")
@Transactional
public class CityServiceImpl implements CityService{
	
	
	GenericDao< City,Long > cityDao;

	@Autowired
	public void setDao( GenericDao< City,Long > daoToSet ){
		cityDao = daoToSet;
		cityDao.setEntityClass( City.class );
	}
	
	@Override
	public City findCityByName(String name) {
		return cityDao.findByUniquefield("name", name);
	}

	@Override
	public List<City> getAllCities() {
		return cityDao.findAll();
	}
	
}
