package com.espiritware.opusclick.error;

import org.springframework.security.core.AuthenticationException;

public final class RoleNotFoundException extends AuthenticationException{
	
	private static final long serialVersionUID = 1L;

    public RoleNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RoleNotFoundException(final String message) {
        super(message);
    }
}
