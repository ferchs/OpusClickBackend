package com.espiritware.opusclick.error;

public class ProviderAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProviderAlreadyExistException() {
        super();
    }

    public ProviderAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProviderAlreadyExistException(final String message) {
        super(message);
    }

    public ProviderAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
