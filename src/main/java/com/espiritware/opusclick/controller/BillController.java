package com.espiritware.opusclick.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.Bill;
import com.espiritware.opusclick.model.InvalidTransaction;
import com.espiritware.opusclick.model.Milestone;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.BillService;
import com.espiritware.opusclick.service.InvalidTransactionService;
import com.espiritware.opusclick.service.WorkService;

@Controller
@RequestMapping("/v1")
public class BillController {

	
	@Autowired
	private BillService billService;
	
	@Autowired
	private InvalidTransactionService invalidTransactionService;
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private Publisher publisher;
	
	@Value("${app.payments-encryption-key}")
    private String paymentsEncryptionKey;
	
	@RequestMapping(value = "/bills", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	@Transactional
	public void createBill(UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		if (isSuccessfulTransaction(request) && isValidSignature(request)) {
			Work work = workService.findWorkById(Integer.parseInt(request.getParameter("x_extra1")));
			work.setState(State.IN_PROGRESS);
			work.setHistoryStateChanges(work.getContract().getWork().getHistoryStateChanges() + work.getState().state() + ",");
			work.getContract().setState(work.getState());
			work.getContract().setHistoryStateChanges(work.getContract().getHistoryStateChanges() + work.getState().state() + ",");
			work.getContract().setStartDate(calculateStartContractDate(work.getContract().getCreationDate()));
			work.getContract().setEndDate(calculateContractEnd(work.getContract().getStartDate(), work.getContract().getMilestones()));
			work.getContract().setMilestones(updateMilestones(work.getContract().getCreationDate(), work.getContract().getMilestones()));
			work.getContract().setHistoryStateChanges(work.getContract().getHistoryStateChanges() + work.getContract().getState().state() + ",");
			work.getContract().getWork().setState(work.getContract().getState());
			work.getContract().getWork().setHistoryStateChanges(work.getContract().getWork().getHistoryStateChanges()+ work.getContract().getState().state() + ",");
			work.getContract().setBill(generateBill(request));
			workService.updateWork(work.getContract().getWork());
			publisher.publishUserMakesPaymentEvent(work.getContract());
		} else {
			saveInvalidTransactionData(request);
		}
	}
	
	
	private boolean isSuccessfulTransaction(HttpServletRequest request) {
		return (request.getParameter("x_cod_transaction_state").equals("1") || request.getParameter("x_cod_transaction_state").equals("3"))? true : false;
	}
	
	private boolean isValidSignature(HttpServletRequest request) {		
	    String checkChain=request.getParameter("x_cust_id_cliente")
	    		+"^"+paymentsEncryptionKey
	    		+"^"+request.getParameter("x_ref_payco")
	    		+"^"+request.getParameter("x_transaction_id")
	    		+"^"+request.getParameter("x_amount")
	    		+"^"+request.getParameter("x_currency_code");
	    
	    System.out.println("Cadena: "+checkChain);
	    
	    String SHA256 = DigestUtils.sha256Hex(checkChain);
	    
	    System.out.println("SHA256: "+checkChain);
	    
	    if(SHA256.equalsIgnoreCase(request.getParameter("x_signature"))) {
	    	return true;
	    }else {
	    	return false;
	    }
	}
	
	private Bill generateBill(HttpServletRequest request) {
		Bill bill= new Bill();
		bill.setBillNumber(request.getParameter("x_id_invoice"));
		bill.setDate(new Date());
		bill.setPaymentDescription(request.getParameter("x_description").replace("%", " "));
		bill.setValue(Double.parseDouble(request.getParameter("x_amount_ok")));
		bill.setTransactionState(getTransactionState(request));
		bill.setAuthorizationCode(request.getParameter("x_approval_code"));
		bill.setTransactionNumber(request.getParameter("x_transaction_id"));
		bill.setPaymentMethod(request.getParameter("x_franchise"));
		return billService.createBill(bill);
	}
	
	private void saveInvalidTransactionData(HttpServletRequest request) {
		InvalidTransaction invalidTransaction= new InvalidTransaction();
		invalidTransaction.setBillNumber(request.getParameter("x_id_invoice"));
		invalidTransaction.setDate(new Date());
		invalidTransaction.setPaymentDescription(request.getParameter("x_description").replace("%", " "));
		invalidTransaction.setValue(Double.parseDouble(request.getParameter("x_amount_ok")));
		invalidTransaction.setTransactionState(getTransactionState(request));
		invalidTransaction.setAuthorizationCode(request.getParameter("x_approval_code"));
		invalidTransaction.setTransactionNumber(request.getParameter("x_transaction_id"));
		invalidTransaction.setPaymentMethod(request.getParameter("x_franchise"));
		invalidTransactionService.createInvalidTransaction(invalidTransaction);
	}
	
	private State getTransactionState(HttpServletRequest request) {
		switch (request.getParameter("x_cod_transaction_state")) {
		
		case "1":
			return State.SUCCESSFUL;

		case "2":
			return State.REJECTED;
			
		case "3":
			return State.PENDING;
			
		case "4":
			return State.FAILED;
		
		case "6":
			return State.REVERSED;

		case "7":
			return State.DETAINED;
			
		case "8":
			return State.INITIATED;
			
		case "9":
			return State.EXPIRED;
			
		case "10":
			return State.ABANDONED;

		case "11":
			return State.CANCELED;
			
		case "12":
			return State.ANTIFRAUD;

		default:
			return State.ABORTED;
		}
	}
	
	private Date calculateStartContractDate(Date paymentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(paymentDate);
		calendar.add(Calendar.DATE, 1);
		if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
			calendar.add(Calendar.DATE, 1);
		}
		return calendar.getTime();
	}
	
	
	private Date calculateContractEnd(Date start, Set<Milestone> milestones) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		for (Milestone milestone : milestones) {
			int roundedValue = (int) Math.round(milestone.getItem().getDurationValue());
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Hora(s)")) {
				calendar.add(Calendar.HOUR, roundedValue);
			}
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Día(s)")) {
				calendar.add(Calendar.DATE, roundedValue);
			}
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Mes(es)")) {
				calendar.add(Calendar.MONTH, roundedValue);
			}
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Año(s)")) {
				calendar.add(Calendar.YEAR, roundedValue);
			}
		}
		return calendar.getTime();
	}
	
	private Set<Milestone> updateMilestones(Date start, Set<Milestone> milestones) {
		Set<Milestone> calculatedMilestones = new HashSet<Milestone>();
		Date previousLastDate = null;
		int i = 0;
		for (Milestone milestone : milestones) {
			if (i != 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(previousLastDate);
				int roundedValue = (int) Math.round(milestone.getItem().getDurationValue());
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Hora(s)")) {
					calendar.add(Calendar.HOUR, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Día(s)")) {
					calendar.add(Calendar.DATE, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Mes(es)")) {
					calendar.add(Calendar.MONTH, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Año(s)")) {
					calendar.add(Calendar.YEAR, roundedValue);
				}
				milestone.setStartDate(previousLastDate);
				milestone.setHistoryStateChanges(milestone.getState().state() + State.IN_PROGRESS.state() + "," );
				milestone.setState(State.IN_PROGRESS);
				milestone.setEndDate(calendar.getTime());
				calculatedMilestones.add(milestone);
				previousLastDate = calendar.getTime();
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				int roundedValue = (int) Math.round(milestone.getItem().getDurationValue());
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Hora(s)")) {
					calendar.add(Calendar.HOUR, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Día(s)")) {
					calendar.add(Calendar.DATE, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Mes(es)")) {
					calendar.add(Calendar.MONTH, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Año(s)")) {
					calendar.add(Calendar.YEAR, roundedValue);
				}
				milestone.setHistoryStateChanges(milestone.getState().state() + State.IN_PROGRESS.state() + "," );
				milestone.setState(State.IN_PROGRESS);
				milestone.setStartDate(start);
				milestone.setEndDate(calendar.getTime());
				calculatedMilestones.add(milestone);
				previousLastDate = calendar.getTime();
			}
		}
		return calculatedMilestones;
	}
}
