package com.espiritware.opusclick.error;
import org.springframework.security.core.AuthenticationException;

public final class AccountNotConfirmedException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

    public AccountNotConfirmedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountNotConfirmedException(final String message) {
        super(message);
    }

}
