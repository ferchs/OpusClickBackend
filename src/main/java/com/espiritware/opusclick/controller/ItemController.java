package com.espiritware.opusclick.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ItemUpdateDto;
import com.espiritware.opusclick.model.Item;
import com.espiritware.opusclick.service.ItemService;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	
	@RequestMapping(value = "/items", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateProvider(@DTO(ItemUpdateDto.class) Item item,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
			itemService.updateItem(item);
			return new ResponseEntity<>(HttpStatus.OK);
	}
	
//	@RequestMapping(value = "/items", method = RequestMethod.PUT, headers = "Accept=application/json")
//	@ResponseBody
//	@Transactional
//	public ResponseEntity<?> updateProvider(@DTO(ItemUpdateDto.class) Item[] items,
//			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
//		if(items!=null) {
//			for(int i=0; i<items.length;i++) {
//				itemService.updateItem(items[i]);
//			}
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
//		else {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
	
}
