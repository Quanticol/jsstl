package eu.quanticol.jsstl2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import eu.quanticol.jsstl2.SpatialModel;
import eu.quanticol.jsstl2.io.TraSpatialModelLoader;

public class TestSpatialModel {

	public final String TRA_CODE1 = "LOCATIONS 10\n0 1 1\n0 2 2\n1 3 1\n2 3 4\n";
	
	@Test
	public void testCreations() {
		SpatialModel sm = new SpatialModel(10);
		assertTrue(true);
	}
	
	@Test 
	public void testSet() {
		SpatialModel sm = new SpatialModel(10);
		sm.set(0, 1, 10);
		assertEquals("i->j",10, sm.get(0,1),0.0);
	}
	
	@Test
	public void testDistance1() {
		SpatialModel sm = new SpatialModel(4);
		sm.set(0, 1, 1);
		sm.set(0, 2, 2);
		sm.set(1, 3, 2);
		sm.set(2, 3, 4);
		sm.computeDistances();
		assertEquals("0->0",0,sm.distance(0, 0),0.0);
		assertEquals("0->1",1,sm.distance(0, 1),0.0);
		assertEquals("0->2",2,sm.distance(0, 2),0.0);
		assertEquals("0->3",3,sm.distance(0, 3),0.0);
	}

	
	@Test
	public void loadModel1() throws IOException {
		TraSpatialModelLoader loader = new TraSpatialModelLoader();
		SpatialModel sm = loader.load(new ByteArrayInputStream(TRA_CODE1.getBytes()));
		assertNotNull(sm);
		assertEquals(10,sm.size());
		assertEquals("0->0",0,sm.distance(0, 0),0.0);
		assertEquals("0->1",1,sm.distance(0, 1),0.0);
		assertEquals("0->2",2,sm.distance(0, 2),0.0);
		assertEquals("0->3",2,sm.distance(0, 3),0.0);
	}
	
}


