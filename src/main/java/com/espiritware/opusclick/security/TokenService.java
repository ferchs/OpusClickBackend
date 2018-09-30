package com.espiritware.opusclick.security;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.State;
import org.springframework.security.core.userdetails.User;
import com.espiritware.opusclick.service.AccountService;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenService {
		
	@Value("${app.jwt-secret-key}")
	public String SECRET;
//	public static final long EXPIRATION_TIME = 86_400_000; // 1 day
	public static final long EXPIRATION_TIME = 86_400_000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProviderService providerService;
	
	public TokenService() {
		
	}

	public String createVerificationEmailToken(int id,String email) {
		String emailToken = Jwts.builder()
				.setId(id+"")
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		System.out.println("Token Creado: "+ emailToken);
		return emailToken;
	}
	
	public boolean validateAccountEmailToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		if (accountService.accountExist((String) claims.get("sub")) 
				&& accountService.getAccountState((String) claims.get("sub")).equals(State.WAITING_EMAIL_CONFIRMATION)
				&& !isTokenExpired(claims)) {
			return true;
		}else if (accountService.accountExist((String) claims.get("sub")) 
				&& accountService.getAccountState((String) claims.get("sub")).equals(State.ACCOUNT_CONFIRMED)
				&& !isTokenExpired(claims)) {
			if(accountService.findAccountByEmail((String) claims.get("sub")).getUser().getState().equals(State.WAITING_EMAIL_CONFIRMATION) ||
					accountService.findAccountByEmail((String) claims.get("sub")).getProvider().getState().equals(State.WAITING_EMAIL_CONFIRMATION)	) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	private boolean isTokenExpired(Claims claims) {
		Integer intDate = (Integer) claims.get("exp");
		Date tokenDate = new Date(intDate.longValue() * 1000);
		Date currentDate = new Date();
		if (currentDate.after(tokenDate)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isTokenExpired(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		Integer intDate = (Integer) claims.get("exp");
		Date tokenDate = new Date(intDate.longValue() * 1000);
		Date currentDate = new Date();
		if (currentDate.before(tokenDate)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getSubjectFromEmailToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		return (String) claims.get("sub");
	}
	
	public int getIdFromEmailToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		return Integer.parseInt((String) claims.get("jti"));
	}
	
	public String createAuthenticationToken(Authentication auth) {
		String token = Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
		return token;
	}
	
	public long getExpirationTime(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
		Integer intDate = (Integer) claims.get("exp");
		long tokenDate = (intDate.longValue() * 1000);
		return tokenDate;
	}
	
	public String getSubjectFromAuthorizationToken(String token) {
		String subject = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody().getSubject();
		return subject;
	}
	
	public String createResetPasswordToken(String email) {
		String emailToken = Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		System.out.println("Token Creado: " + emailToken);
		return emailToken;
	}
	
	public boolean validateResetPasswordToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		if (accountService.accountExist((String) claims.get("sub")) && !isTokenExpired(claims)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean validateLoginRolExist(String email, int id, boolean isUserLogin) {
		if (isUserLogin) {
			return userService.userExist(id);
		} else {
			return providerService.providerExist(id);
		}
	}
	
	public boolean accountExist(String email) {
		return accountService.accountExist(email);
	}
	
	public boolean accountConfirmed(String email, int id, boolean isUser) {
		if(isUser) {
			if(accountService.accountConfirmed(email) && userService.findUserById(id).getState()!=State.WAITING_EMAIL_CONFIRMATION) {
				return true;
			}else {
				return false;
			}
		}else {
			if(accountService.accountConfirmed(email) && providerService.findProviderById(id).getState()!=State.WAITING_EMAIL_CONFIRMATION) {
				return true;
			}else {
				return false;
			}
		}
	}
}
