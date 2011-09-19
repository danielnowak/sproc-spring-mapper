package com.typemapper.core.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class SearchPathSchemaFilterTest {
	
	@Test
	public void testFilter() {
		List<String> functionNames = new ArrayList<String>();
		functionNames.add("tmp2.test");
		functionNames.add("tmp.test");
		
		
		String result = SearchPathSchemaFilter.filter(functionNames, "tmp2, tmp");
		Assert.assertEquals("tmp2.test", result);
	}
	

}
