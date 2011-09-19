package com.typemapper.core.db;

import java.util.Arrays;
import java.util.List;

public class SearchPathSchemaFilter {
	
	public static final String filter(final List<String> names, final String searchPath) {
		if (names.size() == 0) {
			throw new IllegalArgumentException("cannot filter empty names list by search path");
		}
		int pos = 0;
		int foundPos = 0;
		List<String> searchPathList = Arrays.asList(searchPath.split(", "));
		for (final String name : names) {
			final String schema = extractSchemaName(name);
			int currentIndex = searchPathList.indexOf(schema);
			if (pos == -1 ) {
				foundPos = pos;
			} else {
				if (currentIndex < pos && currentIndex != -1) {
					foundPos = pos;
				}
			}
			pos++;
		}
		if (pos == -1) {
			throw new IllegalArgumentException("Could not find schema in search_path: " + searchPath + " Names: " + names);
		}
		return names.get(foundPos);
	}
	
	private static final String extractSchemaName(final String fqName) {
		return fqName.substring(0, fqName.indexOf("."));
	}

}
