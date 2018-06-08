package com.espiritware.opusclick.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.SearchRegistrationDto;
import com.espiritware.opusclick.model.Search;
import com.espiritware.opusclick.service.SearchService;

@Controller
@RequestMapping("/v1")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value = "/searches", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> registerSearch(@Valid @DTO(SearchRegistrationDto.class) Search search,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		searchService.createSearch(search);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
}
