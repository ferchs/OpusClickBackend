package com.espiritware.opusclick.security;

import com.espiritware.opusclick.dto.LoginDto;
import com.espiritware.opusclick.error.AccountNotConfirmedException;
import com.espiritware.opusclick.error.AccountNotFoundException;
import com.espiritware.opusclick.error.RoleNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
			if(!tokenService.accountExist(creds.getEmail())) {
				throw new AccountNotFoundException("No se ha registrado una cuenta con este Email");
			}
			if(!tokenService.accountConfirmed(creds.getEmail(),creds.isUserLogin())) {
				throw new AccountNotConfirmedException("Debes confirmar tu cuenta para iniciar sesion. ¡Revisa tu Email!");
			}
			if(tokenService.validateLoginRolExist(creds.getEmail(), creds.isUserLogin())) {
				//UserDetailsServiceImpl class implements the authentication package com.espiritware.opusclick.service
				return authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
			}
			else {
				throw new RoleNotFoundException("No existe una cuenta asociada a este rol");
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
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		
		if(failed.getClass().equals(AccountNotFoundException.class)) {
			response.sendError(404,failed.getMessage());	
		}
		else if(failed.getClass().equals(AccountNotConfirmedException.class)) {
			response.sendError(401,failed.getMessage());	
		}
		else if(failed.getClass().equals(RoleNotFoundException.class)) {
			response.sendError(409,failed.getMessage());	
		}
	}
	    
}
