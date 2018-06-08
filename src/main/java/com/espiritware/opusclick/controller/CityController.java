package com.espiritware.opusclick.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.espiritware.opusclick.model.City;
import com.espiritware.opusclick.service.CityService;

@Controller
@RequestMapping("/v1")
public class CityController {

	@Autowired
	private CityService cityService;
	
	@RequestMapping(value = "/cities", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<List<City>> getProvidersByProfessionName(
			@RequestParam(value = "name", required = false) String cityName,
			UriComponentsBuilder uriComponentsBuilder) {
		List<City> cities = new ArrayList<City>();
		if (cityName == null) {
			cities = cityService.getAllCities();
			Collections.sort(cities);
			if (cities.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
			}
		} else {
			City city=cityService.findCityByName(cityName);
			cities.add(city);
			if(cities.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else {
				return new ResponseEntity<List<City>>(cities,HttpStatus.OK);
			}
		}
	}
	
}
