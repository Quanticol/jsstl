package eu.quanticol.jsstl2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

import eu.quanticol.jsstl2.Signal;
import eu.quanticol.jsstl2.SignalIterator;
import eu.quanticol.jsstl2.SpatialModel;
import eu.quanticol.jsstl2.io.TraSpatialModelLoader;

public class TestSignal {

	
	public <T> Signal<T> createSignal( double start, double end, double dt, Function<Double,T> f ) {
		Signal<T> signal = new Signal<>();
		double time = start;
		while (time<end) {
			signal.add(time, f.apply(time));
			time += dt;
		}
		signal.complete(end);
		return signal;
	}
	
	public <T> void checkSignal( Signal<T> s, double start, double end, int size, BiFunction<Double,T,Boolean> p, BiFunction<Double,Double,Boolean> step) {
		assertEquals(size,s.size());
		assertEquals("START",start,s.start(),0.0);
		assertEquals("END",end,s.end(),0.0);
		SignalIterator<T> si = s.getIterator();
		double previous = start;
		while (si.hasNext()) {
			double t = si.next();
			T v = si.next(t);
			assertTrue("Time: "+t+" Value: "+v,p.apply(t, v));
			if (t!=start) {
				assertTrue("Step: "+previous+"->"+t,step.apply(previous, t));
			}
			previous = t;
		}
	}
	
	@Test
	public void testCreations() {
		Signal<Boolean> s = new Signal<>();
		for( int i=0 ; i<100 ; i++ ) {
			s.add(i, i%2==0);
		}
		s.complete(100);
		assertEquals("start:",0.0,s.start(),0.0);
		assertEquals("end:",100,s.end(),0.0);
		assertEquals(101,s.size());
	}

	@Test
	public void testCreations2() {
		Signal<Boolean> s = createSignal(0.0, 100, 1.0, x -> true);
		assertEquals("start:",0.0,s.start(),0.0);
		assertEquals("end:",100.0,s.end(),0.0);
		assertEquals(2,s.size());
	}
	
	@Test
	public void testIterator() {
		Signal<Boolean> s = createSignal(0.0, 100, 1.0, x -> x.intValue()%2==0);
		SignalIterator<Boolean> si = s.getIterator();
		double time = 0.0;
		while (time<100) {
			assertEquals("Time",time,si.next(),0.0);
			assertEquals("Value ("+time+")",((int) time)%2==0,si.next(time));
			time += 1.0;
		}
		assertEquals("Time",time,si.next(),0.0);
		assertEquals("Value ("+time+")",false,si.next(time));
	}

	@Test
	public void testUnaryApply() {
		Signal<Boolean> s = createSignal(0.0, 100, 1.0, x -> x.intValue()%2==0).apply(x -> !x);
		assertEquals("start:",0.0,s.start(),0.0);
		assertEquals("end:",100.0,s.end(),0.0);
		assertEquals(101,s.size());
	}


	@Test
	public void testUnaryApply2() {
		Signal<Boolean> s = createSignal(0.0, 100, 1.0, x -> x.intValue()%2==0).apply(x -> !x);
		checkSignal(s, 0.0, 100, 101, (x,y) -> (x<100?y==(!(x.intValue()%2==0)):y==true), (x,y) -> (y-x)==1.0 );
	}
	
	@Test
	public void testBinaryApply() {
		Signal<Boolean> s1 = createSignal(0.0, 100, 1.0, x -> x.intValue()%2==0).apply(x -> !x);
		Signal<Boolean> s2 = createSignal(0.0, 100, 1.0, x -> x.intValue()%2!=0).apply(x -> !x);
		Signal<Boolean> s3 = Signal.apply(s1, (x, y)->x||y,s2);
		checkSignal(s3,0.0,100,2, (x,y) -> true, (x,y) -> (x==0.0)&&(y==100.0));
	}

	@Test
	public void testValues() {
		Signal<Boolean> s = new Signal<>();
		for( int i=0 ; i<100 ; i++ ) {
			s.add(i, i%2==0);
		}
		s.complete(100);
		assertTrue(true);
	}
	
	@Test
	public void testBinaryApply2() {
		Signal<Boolean> s1 = createSignal(50.0, 150, 1.0, x -> x.intValue()%2==0).apply(x -> !x);
		Signal<Boolean> s2 = createSignal(0.0, 100, 1.0, x -> x.intValue()%2!=0).apply(x -> !x);
		Signal<Boolean> s3 = Signal.apply(s1, (x, y)->x||y,s2);
		checkSignal(s3,50.0,100,2, (x,y) -> true, (x,y) -> (x==50.0)&&(y==100.0));
	}

//	@Test
//	public void testBinaryApply3() {
//		Signal<Boolean> s1 = createSignal(0.0, 100, 1.0, x -> x.intValue()%2==0);
//		Signal<Boolean> s2 = createSignal(0.0, 100, 1.0, x -> x.intValue()%3==0);
//		Signal<Boolean> s3 = Signal.apply(s1, (x, y)->x&&y,s2);
//		checkSignal(s3,0.0,100,35, (x,y) -> y==((x.intValue()%2==0)&&(x.intValue()%3==0)), (x,y) -> ((x.intValue()%6==0)&&(y.intValue()%6>0))||((x.intValue()%6>0)&&(y.intValue()%6==0)));
//	}
	
	
}


