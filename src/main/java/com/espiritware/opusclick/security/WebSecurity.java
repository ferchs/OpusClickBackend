package com.espiritware.opusclick.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@ComponentScan({"com.espiritware.opusclick.security"})
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	public static final String USER_REGISTRATION_URL = "/v1/accounts/user";
	public static final String PROVIDER_REGISTRATION_URL = "/v1/accounts/provider";
	public static final String PROVIDER_LIST_URL = "/v1/providers";
	public static final String CITIES_LIST_URL = "/v1/cities";
	public static final String PROFESSIONS_LIST_URL = "/v1/professions";
	public static final String CONFIRM_REGISTRATION_URL = "/v1/registrationConfirm";
	public static final String SEND_RESET_PASSWORD_EMAIL = "/v1/sendResetPasswordEmail";
	public static final String RESET_PASSWORD = "/v1/resetPassword";
	//public static final String LOGIN = "/v1/login";


	private UserDetailsService userDetailsService;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
		
	public WebSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		// .authorizeRequests().anyRequest().permitAll().and()
		.authorizeRequests().antMatchers(HttpMethod.POST, USER_REGISTRATION_URL).permitAll()
		.antMatchers(HttpMethod.POST, PROVIDER_REGISTRATION_URL).permitAll()
		.antMatchers(HttpMethod.GET, PROVIDER_LIST_URL).permitAll()
		.antMatchers(HttpMethod.GET, CONFIRM_REGISTRATION_URL).permitAll()
		.antMatchers(HttpMethod.POST, SEND_RESET_PASSWORD_EMAIL).permitAll()
		.antMatchers(HttpMethod.POST, RESET_PASSWORD).permitAll()
		.antMatchers(HttpMethod.GET, CITIES_LIST_URL).permitAll()
		.antMatchers(HttpMethod.GET, PROFESSIONS_LIST_URL).permitAll()
		.anyRequest().authenticated().and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager(), getApplicationContext()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), getApplicationContext()))
		// this disables session creation on Spring Security
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Lo que ocurre aquí es que solamente se usa un AuthenticationProvider (encargado de manejar las peticiones de autorizacion)
		//en este caso se usa a userDetailsService como AuthenticationProvider que es implementado por la clase userDetailsServiceImp
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//		return source;
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200/*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT"));
		configuration.addAllowedHeader("*");
//		configuration.addAllowedMethod("*");
		configuration.addAllowedOrigin("http://localhost:4200");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
		
	}
  
}
