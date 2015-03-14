package com.taobao.trip.tframe.process;

public class ProcessException extends Exception {

	private static final long serialVersionUID = -1758621270268566921L;
	public ProcessException() {
	}

	public ProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessException(String message) {
		super(message);
	}

	public ProcessException(Throwable cause) {
		super(cause);
	}
}
