package com.espiritware.opusclick.security;

import com.espiritware.opusclick.dto.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
   
    private AuthenticationManager authenticationManager;
    
    private TokenService tokenService;
    
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
        tokenService=ctx.getBean(TokenService.class);
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			LoginDto creds = new ObjectMapper().readValue(req.getInputStream(), LoginDto.class);
			if(tokenService.validateLoginRol(creds.getEmail(), creds.isUserLogin())) {
				return authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
			}
			else {
				System.out.println("No puede Loguearse....");
				return authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>()));
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		System.out.println("Logueado Perfectamente....");
		String token = tokenService.createAuthenticationToken(auth);
		long expirationTime = tokenService.getExpirationTime(token);
		res.addHeader("Access-Control-Expose-Headers", "Authorization");
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
		res.setHeader("Content-Type", "application/json");
		res.getWriter().print("{\"expiresIn\": " + expirationTime + "}");
	}
	    
}
