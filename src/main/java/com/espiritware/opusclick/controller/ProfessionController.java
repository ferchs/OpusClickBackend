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
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.service.ProfessionService;

@Controller
@RequestMapping("/v1")
public class ProfessionController {

	@Autowired
	private ProfessionService professionService;
	
	@RequestMapping(value = "/professions", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<List<Profession>> getProvidersByProfessionName(
			@RequestParam(value = "name", required = false) String professionName,
			UriComponentsBuilder uriComponentsBuilder) {
		List<Profession> professions = new ArrayList<Profession>();
		if (professionName == null) {
			professions = professionService.getAllProfessions();
			Collections.sort(professions);
			if (professions.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<Profession>>(professions, HttpStatus.OK);
			}
		} else {
			Profession profession=professionService.findProfessionByName(professionName);
			professions.add(profession);
			if(professions.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else {
				return new ResponseEntity<List<Profession>>(professions,HttpStatus.OK);
			}
		}
	}
}
