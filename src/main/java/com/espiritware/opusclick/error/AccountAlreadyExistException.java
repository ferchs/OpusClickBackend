package com.espiritware.opusclick.error;

public class AccountAlreadyExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AccountAlreadyExistException() {
        super();
    }

    public AccountAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountAlreadyExistException(final String message) {
        super(message);
    }

    public AccountAlreadyExistException(final Throwable cause) {
        super(cause);
    }
}
