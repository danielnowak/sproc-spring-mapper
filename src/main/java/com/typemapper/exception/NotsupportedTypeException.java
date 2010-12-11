package com.typemapper.exception;

public class NotsupportedTypeException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NotsupportedTypeException() {
		super();
	}

	public NotsupportedTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotsupportedTypeException(String arg0) {
		super(arg0);
	}

	public NotsupportedTypeException(Throwable arg0) {
		super(arg0);
	}
}
