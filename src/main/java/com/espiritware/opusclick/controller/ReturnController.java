package com.espiritware.opusclick.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/v1")
public class ReturnController {

	@Value("${app.hostname}")
	private String hostname;
	
	
	@RequestMapping(value = "/return", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	@Transactional
	public void returnData(UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		String transactionState=request.getParameter("x_cod_transaction_state");
		String billCode=request.getParameter("x_id_invoice");
		String description=removeAccents(request.getParameter("x_description").replace("%", " "));
		String billValue=request.getParameter("x_amount_ok");
		String paymentMethod=getPaymentMethodName(request.getParameter("x_franchise"));


		response.sendRedirect(hostname+"/resumen_pago?estado="+ transactionState
				+"&"+"codigoFactura="+billCode
				+"&"+"descripcion="+description
				+"&"+"valorFactura="+billValue
				+"&"+"medio="+paymentMethod);
		return;
	}
	
	
	@RequestMapping(value = "/return", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public void returnData(UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request,
			final HttpServletResponse response, @RequestParam(value = "ref_payco", required = false) String refPayco) throws IOException {
		
		response.sendRedirect(hostname+"/resumen_pago?estado="+ "10"
				+"&"+"codigoFactura="+"N/A"
				+"&"+"descripcion="+"Transaccion Abandonada"
				+"&"+"valorFactura="+"N/A"
				+"&"+"medio="+"N/A");
		return;
	}
	
	private String getPaymentMethodName(String code) {
		switch (code) {
        case "PR":
        	return "Punto Red";
        case "BA":
        	return "Baloto";
        case "CR":
        	return "Credencial";
        case "EF":
        	return "Efecty";
        case "GA":
        	return "Gana";
        case "RS":
        	return "Red Servi";
        case "PSE":
        	return "PSE";
        case "VS":
        	return "Visa";
        case "MC":
        	return "MasterCard";
        case "AM":
        	return "American Express";
        case "DC":
        	return "Diners Club";
        case "SP":
        	return "SafetyPay";
        default:
        	return "No Identificado";
    }
	}
	
	private String removeAccents(String text) {
		return StringUtils.stripAccents(text); 
	}
}
