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
		
		String transactionState=request.getParameter("transaccionAprobada");
		String billCode=request.getParameter("codigoFactura");
		String description=removeAccents(request.getParameter("campoExtra2").replace("%", " "));
		String billValue=request.getParameter("valorFactura");
		String paymentMethod=getPaymentMethodName(request.getParameter("metodoPago"));


		response.sendRedirect(hostname+"/resumen_pago?estado="+ transactionState
				+"&"+"codigoFactura="+billCode
				+"&"+"descripcion="+description
				+"&"+"valorFactura="+billValue
				+"&"+"medio="+paymentMethod);
		return;
	}
	
	private String getPaymentMethodName(String code) {
		switch (code) {
        case "42":
        	return "MovilRed";
        case "43":
        	return "Via Baloto";
        case "44":
        	return "Cajero ATH";
        case "45":
        	return "Efecty";
        case "11":
        	return "Efecty Codigo Barras";
        case "17":
        	return "Codigo Barras";
        case "35":
        	return "ATH-Exito-Baloto";
        case "47":
        	return "Almacenes Exito";
        case "3":
        	return "Cuenta Ahorro/Corriente";
        case "41":
        	return "Cuenta Ahorro/Corriente";
        case "37":
        	return "Visa";
        case "2":
        	return "Visa";
        case "38":
        	return "MasterCard";
        case "1":
        	return "MasterCard";
        case "39":
        	return "American Express";
        case "5":
        	return "American Express";
        case "40":
        	return "Diners Club";
        case "4":
        	return "Diners Club";
        default:
        	return "No Identificado";
    }
	}
	
	private String removeAccents(String text) {
		return StringUtils.stripAccents(text); 
	}
}
