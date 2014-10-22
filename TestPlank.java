import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
/**
 * Exhaustive testing on Plank Class.
 * @author gmh73
 *
 */
public class TestPlank {
	Plank p;
	Plank.TestButton t;
	
	/**
	 * Structural Basis, good data
	 * Test constructor
	 */
	@Before @Test
	public void testConstruct(){
		p = new Plank(new Pillar(0,1), new Pillar(1,1));
		t = p.new TestButton();
	}
	
	/**
	 * Structured Basis, Bad Data
	 * Make a Plank with null Pillars
	 */
	@Test
	public void testConstructorWithNullData(){
		try{
			p = new Plank(null,null);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(NullPointerException ne){
			//We want this.
		}
		try{
			p = new Plank(null, new Pillar(0,0));
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(NullPointerException ne){
			//We want this.
		}
		try{
			p = new Plank(new Pillar(0,0),null);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(NullPointerException ne){
			//We want this.
		}
	}
	
	/**
	 * Structural Bases, nominal case of getPillars
	 */
	@Test
	public void testGetPillars(){
		Set<Pillar> connected = new HashSet<Pillar>();
		connected.add(new Pillar(0,1));
		connected.add(new Pillar(1,1));
		assertTrue(p.getPillars().containsAll(connected) && connected.containsAll(p.getPillars()));
	}
	
	/**
	 * Structural Basis
	 * Test all combinations of inputs for equals
	 */
	@Test
	public void testEquals(){
		//Null should return false
		assertFalse(p.equals(null));
		//A non-Plank data type should return false
		assertFalse(p.equals(5));
		//p should be equal to itself
		assertTrue(p.equals(p));
		Plank p1 = new Plank(new Pillar(0,1), new Pillar(1,1));
		//p should be equal to a plank with the same pillars
		assertTrue(p1.equals(p));
		assertTrue(p.equals(p1));
		p1 = new Plank(new Pillar(1,1), new Pillar(0,1));
		//p should be equal to a plank with the same pillars, even though the order is reversed
		assertTrue(p1.equals(p));
		assertTrue(p.equals(p1));
	}
	
	/**
	 * Structural Basis
	 * Test to make sure that different combinations of Pillars still
	 * have the same plank hashcode
	 */
	@Test
	public void testHashCode(){
		Plank p1 = new Plank(new Pillar(0,1), new Pillar(1,1));
		Plank p2 = new Plank(new Pillar(1,1), new Pillar(0,1));
		assertTrue(p.hashCode() == p1.hashCode());
		assertTrue(p1.hashCode() == p2.hashCode());
		assertTrue(p.hashCode() == p2.hashCode());
	}
	
	/**
	 * Structural Basis
	 * check if null inputs throw exceptions
	 * check if good inputs do not throw exceptions
	 */
	@Test
	public void testCheckNull(){
		t.testCheckIfNull(new Pillar(0,0), new Pillar(1,1));
		t.testCheckIfNull(new Pillar(0,0), new Pillar(0,1));
		
		try{
			t.testCheckIfNull(null,null);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(NullPointerException ne){
			//We want this.
		}
		try{
			t.testCheckIfNull(null, new Pillar(0,0));
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(NullPointerException ne){
			//We want this.
		}
		try{
			t.testCheckIfNull(new Pillar(0,0),null);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(NullPointerException ne){
			//We want this.
		}
	}
}
