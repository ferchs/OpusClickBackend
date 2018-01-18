package com.espiritware.opusclick.error;

public class CastEnumException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CastEnumException() {
        super();
    }

    public CastEnumException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CastEnumException(final String message) {
        super(message);
    }

    public CastEnumException(final Throwable cause) {
        super(cause);
    }
}
