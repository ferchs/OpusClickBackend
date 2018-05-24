package com.espiritware.opusclick.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espiritware.opusclick.model.Account;

import static java.util.Collections.emptyList;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private AccountService accountService;
	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountService.findAccountById(email);
		if (account != null) {
//			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//	        grantedAuthorities.add(new SimpleGrantedAuthority(userOptional.get().role));
			return new User(account.getEmail(), account.getPassword(), emptyList());
		} else {
			throw new UsernameNotFoundException(email);
		}
	}
}
