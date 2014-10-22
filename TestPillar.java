import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

public class TestPillar {
	Pillar p;
	Pillar.TestButton t;
	
	/**
	 * Good Data
	 * Constructs a pillar with good (non-negative) coordinates.
	 */
	@Before @Test
	public void constructGoodData(){
		p = new Pillar(1,1);
		t = p.new TestButton();
	}
	
	/**
	 * Bad Data
	 * Makes sure that exceptions are thrown when a Pillar is given negative coordinates
	 */
	@Test
	public void testConstructorBadData(){
		try{
			p = new Pillar(-1,-1);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(IndexOutOfBoundsException ie){
			//We want this
		}
		try{
			p = new Pillar(1,-1);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(IndexOutOfBoundsException ie){
			//We want this
		}
		try{
			p = new Pillar(-1,1);
			fail("This line should not be reached as the above line should throw an exception.");
		}catch(IndexOutOfBoundsException ie){
			//We want this
		}
	}
	
	/**
	 * Boundary
	 * Makes sure that a pillar with boundary coordinates of 0 are allowed
	 */
	@Test
	public void testConstructorBoundaryData(){
		p = new Pillar(0,0);
		//Note that 100 is Good data, not a boundary
		p = new Pillar(0, 100);
		p = new Pillar(100, 0);
	}
	
	/**
	 * Structured Test, nominal case getXCor should return x
	 */
	@Test
	public void testGetXCor(){
		assertEquals("The x coordinate initializes to 1", p.getXCor(), 1);
	}
	
	/**
	 * Structured Test, nominal case getYCor should return y
	 */
	@Test
	public void testGetYCor(){
		assertEquals("The y coordinate initializes to 1", p.getYCor(), 1);
	}
	
	/**
	 * Structured Basis, data flow, good data
	 * Test if isAdjectTo returns true for adjacent pillars and false
	 * for equal or non-adjacent pillars
	 */
	@Test
	public void testIsAdjacentTo(){
		//Test equal pillars
		Pillar p1 = new Pillar(1,1);
		assertFalse("Equal pillars are not adjacent", p.isAdjacentTo(p1));
		
		//Test adjacent pillars
		p1 = new Pillar(2,1);
		assertTrue(p1.isAdjacentTo(p));
		assertTrue(p1.isAdjacentTo(p));
		p1 = new Pillar(1,2);
		assertTrue(p1.isAdjacentTo(p));
		assertTrue(p1.isAdjacentTo(p));
		p1 = new Pillar(0,1);
		assertTrue(p1.isAdjacentTo(p));
		assertTrue(p1.isAdjacentTo(p));
		p1 = new Pillar(1,0);
		assertTrue(p1.isAdjacentTo(p));
		assertTrue(p1.isAdjacentTo(p));
		
		//Test non-adjacent pillars
		p1 = new Pillar(3,1);
		assertFalse(p1.isAdjacentTo(p));
		assertFalse(p.isAdjacentTo(p1));
		p1 = new Pillar(0,0);
		assertFalse(p1.isAdjacentTo(p));
		assertFalse(p.isAdjacentTo(p1));
		p1 = new Pillar(100,100);
		assertFalse(p1.isAdjacentTo(p));
		assertFalse(p.isAdjacentTo(p1));
	}
	
	/**
	 * Structured Basis, Good Data
	 * Nominal Cases, make sure shortestDistanceTo() returns the correct value
	 */
	@Test
	public void testShortestDistanceTo(){
		//Equal pillars have a distance of 0 to each other
		assertEquals(p.shortestDistanceTo(p), 0);
		
		//Test Good data
		Pillar p1 = new Pillar(2,1);
		assertEquals(p1.shortestDistanceTo(p), 1);
		assertEquals(p.shortestDistanceTo(p1), 1);
		
		p1 = new Pillar(0,0);
		assertEquals(p1.shortestDistanceTo(p), 2);
		assertEquals(p.shortestDistanceTo(p1), 2);
		
		p1 = new Pillar(100,100);
		assertEquals(p1.shortestDistanceTo(p), 198);
		assertEquals(p.shortestDistanceTo(p1), 198);
	}
	
	/**
	 * Structured, Dataflow, good data
	 * Tests adjoiningPillars for 4 corners of the Maze
	 */
	@Test
	public void testAdjoiningPillarsCorners(){
		//Initialize good data
		int limit = 20;
		Set<Plank> layout = new HashSet<Plank>();
		layout.add(new Plank( new Pillar(0,0), new Pillar(0,1)  ));
		layout.add(new Plank( new Pillar(limit,limit), new Pillar(limit,limit - 1)  ));
		layout.add(new Plank( new Pillar(0,limit), new Pillar(0,limit - 1)  ));
		layout.add(new Plank( new Pillar(limit,0), new Pillar(limit,1)  ));
		
		List<Pillar> expectedOutput = new LinkedList<Pillar>();
		
		//Test when there are 2 adjacent pillars from the 0,0 corner
		//both when usePlank is true and false
		Pillar p1 = new Pillar(0,0);
		expectedOutput.add(new Pillar(0,1));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(1, 0));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 2 adjacent pillars from the 0,limit corner
		//both when usePlank is true and false
		p1 = new Pillar(0,limit);
		expectedOutput.add(new Pillar(0,limit - 1));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(1, limit));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 2 adjacent pillars from the limit,limit corner
		//both when usePlank is true and false
		p1 = new Pillar(limit,limit);
		expectedOutput.add(new Pillar(limit,limit - 1));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(limit - 1, limit));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 2 adjacent pillars from the limit,0 corner
		//both when usePlank is true and false
		p1 = new Pillar(limit,0);
		expectedOutput.add(new Pillar(limit,1));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(limit - 1, 0));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
	}
	
	/**
	 * Structured, Dataflow, good data
	 * Tests adjoiningPillars for 4 edges of the Maze
	 */
	@Test
	public void testAdjoiningPillarsEdges(){
		//Initialize good data
		int limit = 20;
		assertTrue("This test would not work for a limit less than or equal to 2", limit > 2);
		Set<Plank> layout = new HashSet<Plank>();
		layout.add(new Plank( new Pillar(limit/2,0), new Pillar(limit/2,1)  ));
		layout.add(new Plank( new Pillar(0,limit/2), new Pillar(1,limit/2)  ));
		layout.add(new Plank( new Pillar(limit/2,limit), new Pillar(limit/2,limit - 1)  ));
		layout.add(new Plank( new Pillar(limit,limit/2), new Pillar(limit - 1,limit/2)  ));
		
		List<Pillar> expectedOutput = new LinkedList<Pillar>();
		
		//Test when there are 3 adjacent pillars from the limit/2,0 edge
		//both when usePlank is true and false
		Pillar p1 = new Pillar(limit/2,0);
		expectedOutput.add(new Pillar(limit/2,1));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(limit/2 + 1, 0));
		expectedOutput.add(new Pillar(limit/2 - 1, 0));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 3 adjacent pillars from the 0,limit/2 edge
		//both when usePlank is true and false
		p1 = new Pillar(0,limit/2);
		expectedOutput.add(new Pillar(1,limit/2));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(0, limit/2 + 1));
		expectedOutput.add(new Pillar(0, limit/2 - 1));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 3 adjacent pillars from the limit/2,limit edge
		//both when usePlank is true and false
		p1 = new Pillar(limit/2,limit);
		expectedOutput.add(new Pillar(limit/2,limit - 1));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(limit/2 + 1,limit));
		expectedOutput.add(new Pillar(limit/2 - 1,limit));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 3 adjacent pillars from the limit,limit/2 edge
		//both when usePlank is true and false
		p1 = new Pillar(limit,limit/2);
		expectedOutput.add(new Pillar(limit-1,limit/2));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(limit,limit/2 + 1));
		expectedOutput.add(new Pillar(limit,limit/2 -1));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
	}
	
	/**
	 * Structured, Dataflow, good data
	 * Tests adjoiningPillars for middle pillars of the maze (non-corner and non-edge)
	 */
	@Test
	public void testAdjoiningPillarsMiddle(){
		//Initialize good data
		int limit = 20;
		assertTrue("This test would not work for a limit less than or equal to 2", limit > 2);
		Set<Plank> layout = new HashSet<Plank>();
		layout.add(new Plank( new Pillar(limit/2,limit/2), new Pillar(limit/2,limit/2 + 1)  ));
		layout.add(new Plank( new Pillar(limit/2,limit/2), new Pillar(limit/2 - 1,limit/2)  ));
		
		List<Pillar> expectedOutput = new LinkedList<Pillar>();
		
		//Test a pillar in the middle of the grid
		//both when usePlank is true and false
		Pillar p1 = new Pillar(limit/2,limit/2);
		expectedOutput.add(new Pillar(limit/2,limit/2 + 1));
		expectedOutput.add(new Pillar(limit/2 - 1,limit/2));
		assertEquals(p1.adjoiningPillars(false, layout, limit), expectedOutput);
		expectedOutput.clear();
		expectedOutput.add(new Pillar(limit/2 + 1, limit/2));
		expectedOutput.add(new Pillar(limit/2, limit/2 - 1));
		assertEquals(p1.adjoiningPillars(true, layout, limit), expectedOutput);
		expectedOutput.clear();
	}
	
	/**
	 * Structured, Boundary
	 * When the limit is 0, it should have no adjoining Pillars
	 */
	@Test
	public void testAdjoiningPillarsLimitIs0(){
		Set<Plank> layout = new HashSet<Plank>();
		List<Pillar> l = new LinkedList<Pillar>();
		p = new Pillar(0,0);
		assertEquals(p.adjoiningPillars(true, layout, 0), l);
	}
	
	/**
	 * Structured, Boundary
	 * When the limit is 0, it should have no adjoining Pillars
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testAdjoiningPillarsLimitIsNeg(){
		Set<Plank> layout = new HashSet<Plank>();
		List<Pillar> l = new LinkedList<Pillar>();
		p = new Pillar(0,0);
		assertEquals(p.adjoiningPillars(true, layout, -1), l);
	}
	
	/**
	 * Structured Basis, Bad data
	 * An exception is thrown if the given limit is less than the
	 * Pillars x or y coordinates
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testAdjoiningPillarsLimitLessThanCoor(){
		Set<Plank> layout = new HashSet<Plank>();
		p.adjoiningPillars(true, layout, 0);
	}
	
	/**
	 * Bad data, layout = null
	 */
	@Test(expected=NullPointerException.class)
	public void testAdjoiningPillarsNullLayout(){
		p.adjoiningPillars(true, null, 10);
	}
	
	/**
	 * Structured Basis
	 * Make make sure equal/identical pillars are equal
	 * Make sure unequal pillars in the x, y, or both coordinate are unequal
	 */
	@Test
	public void testEquals(){
		//Test when pillars have same x's and y's
		Pillar p1 = new Pillar(1,1);
		assertTrue(p1.equals(p));
		assertTrue(p.equals(p1));
		assertTrue(p1.equals(p1));
		assertTrue(p.equals(p));
		
		//Test when pillars have different x's
		p1 = new Pillar(1,0);
		assertFalse(p1.equals(p));
		assertFalse(p.equals(p1));
		
		//Test when pillars have different y's
		p1 = new Pillar(0,1);
		assertFalse(p1.equals(p));
		assertFalse(p.equals(p1));
		
		//Test when pillars have both different x's and y's
		p1 = new Pillar(0,0);
		assertFalse(p1.equals(p));
		assertFalse(p.equals(p1));
	}
	
	/**
	 * Structured Basis, nominal case
	 * make sure hash codes for identical pillars are the same
	 */
	@Test
	public void testHashCode(){
		Pillar p1 = new Pillar(1,1);
		assertEquals(p1.hashCode(), p.hashCode());
	}
	
	/**
	 * Structured Basis
	 * Make sure toString works like it should
	 */
	@Test
	public void testToString(){
		assertEquals(p.toString(), "(1,1)");
	}
	
	/**
	 * Structed Basis, Good Data
	 * Make sure checkCoorValid works for good data, both > 0 and == 0
	 */
	@Test
	public void testCheckCoorValid(){
		t.testCheckCoorValid(0);
		t.testCheckCoorValid(100);
	}
	
	/**
	 * Structured Basis, Bad data
	 * Make sure exception is thrown for when coordinate is negative
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testCheckCoorValidBadData(){
		t.testCheckCoorValid(-1);
	}
	
	/**
	 * Structured Bases, Data flow, good data
		//both when usePlank is true and false
	 * Test the output of adjacent pillars for middle (non-edge, non-corner) Pillars
	 */
	@Test
	public void testAdjacentPillarsMiddle(){
		int limit = 20;
		List<Pillar> expected = new LinkedList<Pillar>();
		expected.add(new Pillar(2,1));
		expected.add(new Pillar(1,2));
		expected.add(new Pillar(0,1));
		expected.add(new Pillar(1,0));
		
		assertEquals(t.testAdjacentPillars(limit), expected);
	}
	
	/**
	 * Structured Bases, Data flow, good data
	 * Test the output of adjacent pillars for middle corner Pillars
	 */
	@Test
	public void testAdjacentPillarsCorner(){
		//Initialize good data
		int limit = 20;
		assertTrue(limit > 1);
		
		List<Pillar> expectedOutput = new LinkedList<Pillar>();
		
		//Test when there are 2 adjacent pillars from the 0,0 corner
		Pillar p1 = new Pillar(0,0);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(1,0));
		expectedOutput.add(new Pillar(0,1));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 2 adjacent pillars from the 0,limit corner
		p1 = new Pillar(0,limit);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(1,limit));
		expectedOutput.add(new Pillar(0,limit - 1));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 2 adjacent pillars from the limit,limit corner
		p1 = new Pillar(limit,limit);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(limit-1,limit));
		expectedOutput.add(new Pillar(limit,limit - 1));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 2 adjacent pillars from the limit,0 corner
		p1 = new Pillar(limit,0);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(limit,1));
		expectedOutput.add(new Pillar(limit - 1,0));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
	}
	
	/**
	 * Structured Bases, Data flow, good data
	 * Test the output of adjacent pillars for edge Pillars
	 */
	@Test
	public void testAdjacentPillarsEdges(){
		//Initialize good data
		int limit = 20;
		assertTrue(limit > 2);
		
		List<Pillar> expectedOutput = new LinkedList<Pillar>();
		
		//Test when there are 3 adjacent pillars from the limit/2,0 edge
		Pillar p1 = new Pillar(limit/2,0);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(limit/2 + 1,0));
		expectedOutput.add(new Pillar(limit/2 ,1));
		expectedOutput.add(new Pillar(limit/2 -1,0));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 3 adjacent pillars from the 0,limit/2 edge
		p1 = new Pillar(0,limit/2);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(1,limit/2));
		expectedOutput.add(new Pillar(0,limit/2 + 1));
		expectedOutput.add(new Pillar(0,limit/2 - 1));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 3 adjacent pillars from the limit/2,limit edge
		p1 = new Pillar(limit/2,limit);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(limit/2 + 1,limit));
		expectedOutput.add(new Pillar(limit/2 - 1,limit));
		expectedOutput.add(new Pillar(limit/2,limit - 1));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
		
		//Test when there are 3 adjacent pillars from the limit,limit/2 edge
		p1 = new Pillar(limit,limit/2);
		t = p1.new TestButton();
		expectedOutput.add(new Pillar(limit,limit/2 + 1));
		expectedOutput.add(new Pillar(limit -1,limit/2));
		expectedOutput.add(new Pillar(limit,limit/2 -1));
		assertEquals(t.testAdjacentPillars(limit), expectedOutput);
		expectedOutput.clear();
	}
	
	/**
	 * Structural Basis, boundary
	 * When the limit is 0, there should be no adjacent pillars
	 */
	@Test
	public void testAdjacentPillarsLimitIs0(){
		p = new Pillar(0,0);
		t = p.new TestButton();
		List<Pillar> l = new LinkedList<Pillar>();
		assertEquals(l,t.testAdjacentPillars(0));
	}
	
	/**
	 * Structural Basis, Good data
	 * Make sure connectionAdjoined has correct output
	 */
	@Test
	public void testConnectionAdjoined(){
		Plank pl = new Plank(p, new Pillar(1,0));
		Set<Plank> layout = new HashSet<Plank>();
		layout.add(pl);
		
		//Test all 4 cases
		//pl in layout and usePlank == false
		assertTrue(t.testConnectionAdjoined(false, pl, layout));
		//pl in layout and usePlank == true
		assertFalse(t.testConnectionAdjoined(true, pl, layout));
		layout.remove(pl);
		//pl not in layout and usePlank == false
		assertFalse(t.testConnectionAdjoined(false, pl, layout));
		//pl not in layout and usePlank == true
		assertTrue(t.testConnectionAdjoined(true, pl, layout));
	}
	
	/**
	 * Structural Basis
	 * Test Check Null when input is good
	 */
	@Test
	public void testCheckNull(){
		Set<Plank> layout = new HashSet<Plank>();
		t.testCheckNull(layout);
	}
	
	/**
	 * Structural Basis
	 * Test Check Null when input is Null
	 */
	@Test(expected=NullPointerException.class)
	public void testCheckNullWhenNull(){
		t.testCheckNull(null);
	}
	
	/**
	 * Structural Basis
	 * Test Check Limit when limit is valid, >= Pillar coordinate
	 */
	@Test
	public void testCheckLimit(){
		t.testCheckLimit(2);
		t.testCheckLimit(1);
		t.testCheckLimit(100);
	}
	
	/**
	 * Structural Basis
	 * Test Check Limit when limit is less than p's coordinates
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testCheckLimitWhenLess(){
		t.testCheckLimit(0);
	}
	/**
	 * Structural Basis
	 * Test Check Limit when limit is negative
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testCheckLimitWhenNeg(){
		t.testCheckLimit(-1);
	}
}
