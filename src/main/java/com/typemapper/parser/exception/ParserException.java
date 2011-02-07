package com.typemapper.parser.exception;

public class ParserException extends Exception {

	private static final long serialVersionUID = -1877004443888510377L;

	public ParserException() {
		super();
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

}