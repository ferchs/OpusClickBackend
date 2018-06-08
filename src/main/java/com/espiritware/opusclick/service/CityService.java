package com.espiritware.opusclick.service;

import java.util.List;

import com.espiritware.opusclick.model.City;

public interface CityService {
	
	City findCityByName(String name);
	
	List<City> getAllCities();
}
