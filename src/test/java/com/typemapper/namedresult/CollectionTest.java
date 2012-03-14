package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithList;
import com.typemapper.namedresult.results.ClassWithObjectWithArray;
import com.typemapper.namedresult.results.ClassWithPrimitives;
import com.typemapper.namedresult.results.ClassWithSet;
import com.typemapper.namedresult.results.ClassWithStringSet;
import com.typemapper.namedresult.results.ObjectWithArray;

public class CollectionTest extends AbstractTest {

	
	@Test
	public void testSimpleList() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ARRAY[ROW(1,2,'c')::tmp.simple_type, ROW(1,2,'c')::tmp.simple_type]::tmp.simple_type[] as arr");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithList.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithList result = (ClassWithList) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getArray());
			Assert.assertTrue(result.getArray().size() == 2);
			Assert.assertNotNull(result.getArray().get(0));
			Assert.assertNotNull(result.getArray().get(1));
			ClassWithPrimitives classWithPrimitives = result.getArray().get(0);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());
			classWithPrimitives = result.getArray().get(1);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());

		}
	}
	
	@Test
	public void testSimpleSet() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ARRAY[ROW(1,2,'c')::tmp.simple_type, ROW(1,2,'c')::tmp.simple_type]::tmp.simple_type[] as arr");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithSet.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithSet result = (ClassWithSet) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getArray());
			Assert.assertTrue(result.getArray().size() == 2);
			List<ClassWithPrimitives> list = new ArrayList<ClassWithPrimitives>();
			for (ClassWithPrimitives tmp : result.getArray()) {
				list.add(tmp);
			}
			Assert.assertNotNull(list.get(0));
			Assert.assertNotNull(list.get(1));
			ClassWithPrimitives classWithPrimitives = list.get(0);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());
			classWithPrimitives = list.get(1);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());

		}
	}
	
	@Test
	public void testSimpleStringSet() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ARRAY['result_1', 'result_2']::text[] as arr");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithStringSet.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithStringSet result = (ClassWithStringSet) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getArray());
			Assert.assertTrue(result.getArray().size() == 2);
			Assert.assertTrue(result.getArray().contains("result_1"));
			Assert.assertTrue(result.getArray().contains("result_2"));
		}
	}
	
	@Test
	public void testSimpleStringSetWithNullValue() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, null as arr");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithStringSet.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithStringSet result = (ClassWithStringSet) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNull(result.getArray());
		}
	}
	
	
	@Test
	public void testSimpleSetWithNullValue() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, null as arr");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithSet.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithSet result = (ClassWithSet) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNull(result.getArray());
		}
	}
		
	@Test
	public void testObjectWithList() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT ROW(ARRAY[ROW(1,2,'c')::tmp.simple_type, ROW(1,2,'c')::tmp.simple_type], 'str')::tmp.array_type as obj, 'str' as str ");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObjectWithArray.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObjectWithArray result = (ClassWithObjectWithArray) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getObj());
			Assert.assertEquals("str", result.getObj().getStr());
			Assert.assertNotNull(result.getObj().getArray());
			Assert.assertEquals(2, result.getObj().getArray().size());
			
			Assert.assertNotNull(result.getObj().getArray().get(0));
			Assert.assertNotNull(result.getObj().getArray().get(1));
			ClassWithPrimitives classWithPrimitives = result.getObj().getArray().get(0);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());
			classWithPrimitives = result.getObj().getArray().get(1);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());
		}
	}
	
	@Test
	public void testObjectWithEmptyList() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT ROW(ARRAY[]::tmp.simple_type[], 'str')::tmp.array_type as obj, 'str' as str ");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObjectWithArray.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObjectWithArray result = (ClassWithObjectWithArray) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getObj());
			Assert.assertEquals("str", result.getObj().getStr());
			Assert.assertNotNull(result.getObj().getArray());
			Assert.assertEquals(0, result.getObj().getArray().size());}
	}
	
	@Test
	public void testObjectWithNullList() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT null as arr, 'str' as str ");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ObjectWithArray.class);
		int i = 0;
		while( rs.next() ) {
			ObjectWithArray result = (ObjectWithArray) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNull(result.getArray());
		}
	}
	
	@Test
	public void testObjectWithObjectNullList() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT ROW(null, 'str')::tmp.array_type as obj, 'str' as str ");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObjectWithArray.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObjectWithArray result = (ClassWithObjectWithArray) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getObj());
			Assert.assertEquals("str", result.getObj().getStr());
			Assert.assertNotNull(result.getObj().getArray());
			Assert.assertEquals(0, result.getObj().getArray().size());
		}
	}
	
}
