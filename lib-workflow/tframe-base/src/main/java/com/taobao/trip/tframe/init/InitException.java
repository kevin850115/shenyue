package com.taobao.trip.tframe.init;

public class InitException extends Exception {

	private static final long serialVersionUID = -1758621270268566921L;
	public InitException() {
	}

	public InitException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitException(String message) {
		super(message);
	}

	public InitException(Throwable cause) {
		super(cause);
	}
}
