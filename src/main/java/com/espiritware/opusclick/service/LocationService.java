package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.City;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.User;

public interface LocationService {

	Location createLocation (String address, String zipCode, String zone, City city);
		
	void updateLocation(Location location);
	
	Location findLocationById(long id);
	
}
