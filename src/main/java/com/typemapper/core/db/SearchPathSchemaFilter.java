package com.typemapper.core.db;

import java.util.Arrays;
import java.util.List;

public class SearchPathSchemaFilter {
	
	public static final String filter(final List<String> names, final String searchPath) {
		if (names.size() == 0) {
			throw new IllegalArgumentException("cannot filter empty names list by search path");
		}
		List<String> searchPathList = Arrays.asList(searchPath.split(", "));
		for (final String currentSchema : searchPathList) {
			for (final String name : names) {
				if (extractSchemaName(name).equals(currentSchema)) {
					return name;
				}
			}
		}
		return null;
	}
	
	private static final String extractSchemaName(final String fqName) {
		return fqName.substring(0, fqName.indexOf("."));
	}

}
