package com.typemapper.parser.exception;

import java.text.ParseException;

public class HStoreParseException extends ParseException {

	private static final long serialVersionUID = 1734348462350943810L;

	public HStoreParseException(String s, int errorOffset) {
		super(s, errorOffset);
	}

}
