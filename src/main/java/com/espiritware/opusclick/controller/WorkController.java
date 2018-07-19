package com.espiritware.opusclick.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.VisitGetDto;
import com.espiritware.opusclick.dto.VisitScheduleDto;
import com.espiritware.opusclick.dto.VisitUpdateDto;
import com.espiritware.opusclick.dto.WorkGetDto;
import com.espiritware.opusclick.dto.WorkUpdateDto;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.WorkService;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class WorkController {

	@Autowired
	private WorkService workService;
	
	@Autowired
	private ModelMapper modelMapper;
	
//	@Autowired
//	private Publisher publisher;
	
	
	@RequestMapping(value = "/works", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<?> getVisits(@RequestParam(value = "rol", required = true) String rolName,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "state", required = false) String state, UriComponentsBuilder uriComponentsBuilder) {
	
		if (rolName.equalsIgnoreCase("user")) {
			if (state != null) {
				String [] statesName=state.split("\\,");
				if(statesName.length>1) {
					List<WorkGetDto> workDtos =new ArrayList<WorkGetDto>();
					workDtos =generateWorkDtos(workService.findWorksOfUserByStates(id,statesName));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}else {
					List<WorkGetDto> workDtos = generateWorkDtos(workService.findWorksOfUserByState(id, State.valueOf(state)));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}
			} else {
				List<WorkGetDto> worksDto = generateWorkDtos(workService.findAllWorksOfUser(id));
				return new ResponseEntity<>(worksDto, HttpStatus.OK);
			}
		} else if (rolName.equalsIgnoreCase("provider")) {
			if (state != null) {
				String [] statesName=state.split("\\,");
				if(statesName.length>1) {
					List<WorkGetDto> workDtos =new ArrayList<WorkGetDto>();
					workDtos =generateWorkDtos(workService.findWorksOfProviderByStates(id,statesName));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}else {
					List<WorkGetDto> workDtos = generateWorkDtos(workService.findWorksOfProviderByState(id, State.valueOf(state)));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}
			} else {
				List<WorkGetDto> worksDto = generateWorkDtos(workService.findAllWorksOfProvider(id));
				return new ResponseEntity<>(worksDto, HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>("El parametro rol debe ser 'user' o 'provider'", HttpStatus.BAD_REQUEST);
		}
	}
	
	private List<WorkGetDto> generateWorkDtos(List<Work> works){
		List<WorkGetDto> worksDto = new ArrayList<WorkGetDto>();
		for (Work work : works) {
			WorkGetDto dto = modelMapper.map(work, WorkGetDto.class);
			worksDto.add(dto);
		}
		return worksDto;
	}

	private State[] convertStringArrayToStateArray(String[] elements) {
		State[] states= new State[elements.length];
		for (int i = 0; i < states.length; i++) {
			states[i]=State.valueOf(elements[i]);
		}
		return states;
	}
	
	@RequestMapping(value = "/works", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateVisit(@DTO(WorkUpdateDto.class) Work work,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		workService.updateWork(work);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
