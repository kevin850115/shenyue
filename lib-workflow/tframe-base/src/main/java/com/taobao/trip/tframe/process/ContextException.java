package com.taobao.trip.tframe.process;

public class ContextException extends ProcessException {

	private static final long serialVersionUID = 3779107814632108806L;
	public ContextException() {
	}

	public ContextException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContextException(String message) {
		super(message);
	}

	public ContextException(Throwable cause) {
		super(cause);
	}
}
