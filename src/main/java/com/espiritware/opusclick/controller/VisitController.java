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
import com.espiritware.opusclick.dto.VisitUpdateStateDto;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.UserService;
import com.espiritware.opusclick.service.VisitService;
import com.espiritware.opusclick.service.WorkService;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class VisitController {

	@Autowired
	private VisitService visitService;
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private Publisher publisher;
		
	@Autowired
	private ModelMapper modelMapper;
	
	
	@RequestMapping(value = "/visits", method = RequestMethod.POST, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<?> createVisit(@Valid @DTO(VisitScheduleDto.class) Visit visit,
			@RequestParam(value = "user", required = false) String userId,
			@RequestParam(value = "provider", required = false) String providerId,
			@RequestParam(value = "work", required = false) String workId, UriComponentsBuilder uriComponentsBuilder) {
		
		/*visit.setDate(setHourToDate(visit.getDate(),visit.getLowerLimit()));
		visit.setAlternativeDate(setHourToDate(visit.getAlternativeDate(),visit.getAlternativeLowerLimit()));*/
		
		if (workId != null) {
			Work work = workService.findWorkById(Integer.parseInt(workId));
			if (work == null) {
				return new ResponseEntity<>("No se encontraron Works con el Id suministrado", HttpStatus.NOT_FOUND);
			} else {
				if (work.getVisits() == null) {
					Set<Visit> visits = new HashSet<Visit>();
					visit.setHistoryStateChanges("PENDING_BY_PROVIDER_ACCEPT, ");
					visits.add(visit);
					visit.setWork(work);
					work.setVisits(visits);
					workService.updateWork(work);
					publisher.publishUserVisitRequestEvent(visit);
					return new ResponseEntity<>(HttpStatus.OK);
				} else {
					visit.setHistoryStateChanges("PENDING_BY_PROVIDER_ACCEPT, ");
					work.getVisits().add(visit);
					visit.setWork(work);
					workService.updateWork(work);
					publisher.publishUserVisitRequestEvent(visit);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
		} else {
			if (userId != null && providerId != null) {
				Work work = new Work();// Cual es el problema si yo mismo decido instanciar el objeto Work, y no uso el
										// @Autowired para este objetivo.
				work.setWorkNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
				work.setProviderLabel(work.getWorkNumber());
				work.setUserLabel(work.getWorkNumber());
				work.setCreationDate(new Date());
				work.setState(State.PENDING_BY_VISIT);
				work.setHistoryStateChanges("PENDING_BY_VISIT, ");
				work.setUser(userService.findUserById(Integer.parseInt(userId)));
				work.setProvider(providerService.findProviderById(Integer.parseInt(providerId)));
				Set<Visit> visits = new HashSet<Visit>();
				visit.setWork(work);
				visit.setHistoryStateChanges("PENDING_BY_PROVIDER_ACCEPT, ");
				visits.add(visit);
				work.setVisits(visits);
				workService.createWork(work);
				publisher.publishUserVisitRequestEvent(visit);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>("Los request params user and provider son necesarios", HttpStatus.BAD_REQUEST);
		}
	}
	
	/*private Date setHourToDate(Date date, Time hour) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(hour);
	int hourTmp=calendar.get(Calendar.HOUR);
	int minuteTmp=calendar.get(Calendar.MINUTE);
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY,hourTmp);
	calendar.set(Calendar.MINUTE,minuteTmp);
	calendar.set(Calendar.SECOND,0);
	calendar.set(Calendar.MILLISECOND,0);
	return calendar.getTime();
}*/

	@RequestMapping(value = "/visits", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<?> getVisits(@RequestParam(value = "rol", required = true) String rolName,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "work", required = false) String workId,
			@RequestParam(value = "state", required = false) String state, UriComponentsBuilder uriComponentsBuilder) {

		if (rolName.equalsIgnoreCase("user")) {
			if (state != null && workId != null) {
				String[] states = state.split("\\,");
				if (states.length > 1) {
					List<VisitGetDto> visitsDto = new ArrayList<VisitGetDto>();
					visitsDto = generateVisitsDto(visitService.findVisitsOfUserByWorkAndStates(Integer.parseInt(workId),
							id, convertStringArrayToStateArray(states)));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				} else {
					List<VisitGetDto> visitsDto = generateVisitsDto(visitService
							.findVisitsOfUserByWorkAndState(Integer.parseInt(workId), id, State.valueOf(state)));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				}

			} else {
				if (state != null) {
					String[] statesName = state.split("\\,");
					if (statesName.length > 1) {
						List<VisitGetDto> visitsDto = new ArrayList<VisitGetDto>();
						visitsDto = generateVisitsDto(visitService.findVisitsOfUserByStates(id, statesName));
						return new ResponseEntity<>(visitsDto, HttpStatus.OK);
					} else {
						List<VisitGetDto> visitsDto = generateVisitsDto(
								visitService.findVisitsOfUserByState(id, State.valueOf(state)));
						return new ResponseEntity<>(visitsDto, HttpStatus.OK);
					}
				} else if (workId != null) {
					List<VisitGetDto> visitsDto = generateVisitsDto(
							visitService.findVisitsOfUserByWork(Integer.parseInt(workId), id));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				} else {
					List<VisitGetDto> visitsDto = generateVisitsDto(visitService.findAllVisitsOfUser(id));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				}
			}

		} else if (rolName.equalsIgnoreCase("provider")) {
			if (state != null && workId != null) {
				String[] states = state.split("\\,");
				if (states.length > 1) {
					List<VisitGetDto> visitsDto = new ArrayList<VisitGetDto>();
					visitsDto = generateVisitsDto(visitService.findVisitsOfProviderByWorkAndStates(
							Integer.parseInt(workId), id, convertStringArrayToStateArray(states)));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				} else {
					List<VisitGetDto> visitsDto = generateVisitsDto(visitService
							.findVisitsOfProviderByWorkAndState(Integer.parseInt(workId), id, State.valueOf(state)));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				}
			} else {
				if (state != null) {
					String[] statesName = state.split("\\,");
					if (statesName.length > 1) {
						List<VisitGetDto> visitsDto = new ArrayList<VisitGetDto>();
						visitsDto = generateVisitsDto(visitService.findVisitsOfProviderByStates(id, statesName));
						return new ResponseEntity<>(visitsDto, HttpStatus.OK);
					} else {
						List<VisitGetDto> visitsDto = generateVisitsDto(
								visitService.findVisitsOfProviderByState(id, State.valueOf(state)));
						return new ResponseEntity<>(visitsDto, HttpStatus.OK);
					}
				} else if (workId != null) {
					List<VisitGetDto> visitsDto = generateVisitsDto(
							visitService.findVisitsOfProviderByWork(Integer.parseInt(workId), id));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				} else {
					List<VisitGetDto> visitsDto = generateVisitsDto(visitService.findAllVisitsOfProvider(id));
					return new ResponseEntity<>(visitsDto, HttpStatus.OK);
				}
			}
		} else {
			return new ResponseEntity<>("El parametro rol debe ser 'user' o 'provider'", HttpStatus.BAD_REQUEST);
		}
	}

	private List<VisitGetDto> generateVisitsDto(List<Visit> visits){
		List<VisitGetDto> visitsDto = new ArrayList<VisitGetDto>();
		for (Visit visit : visits) {
			VisitGetDto dto = modelMapper.map(visit, VisitGetDto.class);
			visitsDto.add(dto);
		}
		return visitsDto;
	}

	private State[] convertStringArrayToStateArray(String[] elements) {
		State[] states= new State[elements.length];
		for (int i = 0; i < states.length; i++) {
			states[i]=State.valueOf(elements[i]);
		}
		return states;
	}
	
	@RequestMapping(value = "/visits", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateVisit(@DTO(VisitUpdateDto.class) Visit visit,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		State previousState = visitService.findVisitById(visit.getId()).getState();
		String previousStateChanges = visitService.findVisitById(visit.getId()).getHistoryStateChanges();
		if (visit != null) {
			if (visit.getState().equals(State.PENDING_BY_PROVIDER_ACCEPT)) {
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "PENDING_BY_PROVIDER_ACCEPT,");
				} else {
					visit.setHistoryStateChanges("PENDING_BY_PROVIDER_ACCEPT,");
				}
				visitService.updateVisit(visit);
				if (!previousState.equals(State.PENDING_BY_PROVIDER_ACCEPT)) {
					publisher.publishUserVisitChangeDateEvent(visit);
				}
			} else if (visit.getState().equals(State.PENDING_BY_USER_ACCEPT)) {
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "PENDING_BY_USER_ACCEPT,");
				} else {
					visit.setHistoryStateChanges("PENDING_BY_USER_ACCEPT,");
				}
				visitService.updateVisit(visit);
				publisher.publishProviderVisitChangeDateEvent(visit);
			} else if (visit.getState().equals(State.POSTPONE_BY_PROVIDER)) {
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "POSTPONE_BY_PROVIDER,");
				} else {
					visit.setHistoryStateChanges("POSTPONE_BY_PROVIDER,");
				}
				visitService.updateVisit(visit);
				publisher.publishProviderVisitPostponedEvent(visit);
			} else if (visit.getState().equals(State.POSTPONE_BY_USER)) {
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "POSTPONE_BY_USER,");
				} else {
					visit.setHistoryStateChanges("POSTPONE_BY_USER,");
				}
				visitService.updateVisit(visit);
				publisher.publishUserVisitPostponedEvent(visit);
			} else if (visit.getState().equals(State.ACCEPTED_BY_PROVIDER)) {
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "ACCEPTED_BY_PROVIDER,");
				} else {
					visit.setHistoryStateChanges("ACCEPTED_BY_PROVIDER,");
				}
				visitService.updateVisit(visit);
				publisher.publishProviderVisitAcceptedEvent(visit);
			} else if (visit.getState().equals(State.ACCEPTED_BY_USER)) {
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "ACCEPTED_BY_USER,");
				} else {
					visit.setHistoryStateChanges("ACCEPTED_BY_USER,");
				}
				visitService.updateVisit(visit);
				publisher.publishUserVisitAcceptedEvent(visit);
			} else if (visit.getState().equals(State.UNFULFILLED_VISIT_BY_PROVIDER)) {
				String actualVisitBreachDescription = visitService.findVisitById(visit.getId()).getBreachDescription();
				if(actualVisitBreachDescription!=null) {
					visit.setBreachDescription(actualVisitBreachDescription + " &Cliente: " + visit.getBreachDescription() + "@");
				}else {
					visit.setBreachDescription(" &Cliente: " + visit.getBreachDescription() + "@");
				}
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "UNFULFILLED_VISIT_BY_PROVIDER,");
				} else {
					visit.setHistoryStateChanges("UNFULFILLED_VISIT_BY_PROVIDER,");
				}
				visitService.updateVisit(visit);
				publisher.publishProviderVisitUnfulfilledEvent(visit);
			} else if (visit.getState().equals(State.UNFULFILLED_VISIT_BY_USER)) {
				String actualVisitBreachDescription = visitService.findVisitById(visit.getId()).getBreachDescription();
				if(actualVisitBreachDescription!=null) {
					visit.setBreachDescription(actualVisitBreachDescription + " &Experto: " + visit.getBreachDescription() + "@");
				}else {
					visit.setBreachDescription(" &Experto: " + visit.getBreachDescription() + "@");
				}
				visit.setPreviousState(previousState);
				if (previousStateChanges != null) {
					visit.setHistoryStateChanges(previousStateChanges + "UNFULFILLED_VISIT_BY_USER,");
				} else {
					visit.setHistoryStateChanges("UNFULFILLED_VISIT_BY_USER,");
				}
				visitService.updateVisit(visit);
				publisher.publishUserVisitUnfulfilledEvent(visit);

			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/visits/states", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateVisitState(@DTO(VisitUpdateStateDto.class) Visit visit,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		State previousState = visitService.findVisitById(visit.getId()).getState();
		String previousStateChanges = visitService.findVisitById(visit.getId()).getHistoryStateChanges();
		State newState = visit.getState();
		if (newState.equals(State.REJECTED_BY_PROVIDER)) {
			visit.setPreviousState(previousState);
			if (previousStateChanges != null) {
				visit.setHistoryStateChanges(previousStateChanges + "REJECTED_BY_PROVIDER,");
			} else {
				visit.setHistoryStateChanges("REJECTED_BY_PROVIDER,");
			}
			visitService.updateVisit(visit);
			publisher.publishProviderVisitRejectedEvent(visit);
		} else if (newState.equals(State.REJECTED_BY_USER)) {
			visit.setPreviousState(previousState);
			if (previousStateChanges != null) {
				visit.setHistoryStateChanges(previousStateChanges + "REJECTED_BY_USER,");
			} else {
				visit.setHistoryStateChanges("REJECTED_BY_USER,");
			}
			visitService.updateVisit(visit);
			publisher.publishUserVisitRejectedEvent(visit);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
