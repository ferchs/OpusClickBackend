package com.espiritware.opusclick.error;

import org.springframework.security.core.AuthenticationException;

public final class AccountNotFoundException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

    public AccountNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountNotFoundException(final String message) {
        super(message);
    }

}
