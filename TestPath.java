import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

/**
 * Exhaustive testing on Path Class.
 * @author gmh73
 *
 */
public class TestPath {
	private Path p;
	private Path.TestButton t;
	
	/**
	 * Initialize
	 */
	@Before
	public void initialize(){
		p = new Path();
		t = p.new TestButton();
	}
	
	/**
	 * Test the nominal and only case of the constructor
	 */
	@Test
	public void testConstructor(){
		assertTrue(p.isSameDistance(-1));
		assertTrue(t.getPillars().isEmpty());
		assertTrue(t.getPPath().isEmpty());
		assertTrue(p.getPlank() == null);
	}
	
	
	/**
	 * Structural Basis
	 * Must test each case in the function
	 */
	@Test
	public void testAddPillar(){
		Set<Pillar> expectedPillars = new HashSet<Pillar>();
		List<Pillar> expectedPPath = new LinkedList<Pillar>();
		int expectedDist = -1;
		
		//Test 1: PPath is empty And toAdd not on Pillars
		Pillar toAdd = new Pillar(0,0);
		p.addPillar(toAdd);
		expectedPillars.add(toAdd);
		expectedPPath.add(toAdd);
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(expectedPPath.equals(t.getPPath()));
		assertTrue(p.isSameDistance(++expectedDist));
		
		//Test 2: toAdd is adjacent to last pillar and and toAdd not in Pillars
		toAdd = new Pillar(1,0);
		p.addPillar(toAdd);
		expectedPillars.add(toAdd);
		expectedPPath.add(toAdd);
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(expectedPPath.equals(t.getPPath()));
		assertTrue(p.isSameDistance(++expectedDist));
		
		//Test 3: toAdd is not adjacent to last Pillar
		toAdd = new Pillar(1,100);
		p.addPillar(toAdd);
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(expectedPPath.equals(t.getPPath()));
		assertTrue(p.isSameDistance(expectedDist));
		
		//Test 4: toAdd is adjacent to last Pillar and toAdd is in ppath
		toAdd = new Pillar(1,0);
		p.addPillar(toAdd);
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(expectedPPath.equals(t.getPPath()));
		assertTrue(p.isSameDistance(expectedDist));
	}
	
	/**
	 * Bad Data
	 */
	@Test(expected=NullPointerException.class)
	public void testAddPillarNull(){
		p.addPillar(null);
	}
	
	/**
	 * Structured Basis, Boundary conditions
	 */
	@Test
	public void testIsShorterThanPath(){
		Path p1 = new Path();
		//Case 1, p1.distance == p.distance
		assertFalse(p.isShorterThan(p1) || p1.isShorterThan(p));
		
		//Case 2,  p.distance < p1.distance 
		p1.addPillar(new Pillar(10,10));
		assertTrue(p.isShorterThan(p1));
		
		//Case 3, (actually case 2 but reverse) p1.distance > p.distance
		assertFalse(p1.isShorterThan(p));
	}
	
	/**
	 * Structured Basis, Boundary conditions
	 */
	@Test
	public void testIsShorterThanInt(){
		p.addPillar(new Pillar(0,1));
		//Case 1, p.distance == 0
		assertFalse(p.isShorterThan(0) || p.isShorterThan(0));
		
		//Case 2,  p.distance < 5
		assertTrue(p.isShorterThan(5));
		
		//Case 3, p.distance > -1
		assertFalse(p.isShorterThan(-1));
	}
	
	/**
	 * Structured Basis, Boundary conditions
	 */
	@Test
	public void testIsSameDistancePath(){
		Path p1 = new Path();
		//Case 1, p1.distance == p.distance
		assertTrue(p.isSameDistance(p1) || p1.isSameDistance(p));
		
		//Case 2,  p.distance < p1.distance 
		p1.addPillar(new Pillar(10,10));
		assertFalse(p.isSameDistance(p1));
		
		//Case 3, (actually case 2 but reverse) p1.distance > p.distance
		assertFalse(p1.isSameDistance(p));
	}
	
	/**
	 * Structured Basis, Boundary conditions
	 */
	@Test
	public void testIsSameDistanceInt(){
		p.addPillar(new Pillar(0,1));
		//Case 1, p.distance == 0
		assertTrue(p.isSameDistance(0));
		
		//Case 2,  p.distance < 5
		assertFalse(p.isSameDistance(5));
		
		//Case 3, p.distance > -1
		assertFalse(p.isSameDistance(-1));
	}
	

	/**
	 * Structured Basis, Bad Data
	 */
	@Test(expected=NullPointerException.class)
	public void testIsShorterThanPathWhenNull(){
		assertFalse(p.isShorterThan(null));
	}
	
	/**
	 * Structured Basis, Bad Data
	 */
	@Test(expected=NullPointerException.class)
	public void testIsSameDistancePathWhenNull(){
		assertFalse(p.isSameDistance(null));
	}
	
	/**
	 * Structured Basis
	 * Case where Path is Empty
	 */
	@Test
	public void testSetDistanceToInfinitePathEmpty(){
		p.setDistanceToInfinite();
		assertTrue(p.isSameDistance(Integer.MAX_VALUE));
	}
	
	/**
	 * Structured Basis
	 * Case where Path is Empty
	 */
	@Test
	public void testSetDistanceToInfinitePathNotEmpty(){
		p.addPillar(new Pillar(0,0));
		p.setDistanceToInfinite();
		assertFalse(p.isSameDistance(Integer.MAX_VALUE));
	}
	
	/**
	 * Structural Basis
	 * Test if contains pillar works
	 */
	@Test
	public void testContainsPillar(){
		Pillar p1 = new Pillar(0,0);
		Pillar p2 = new Pillar(0,1);
		
		//When ppath is empty
		assertFalse(p.containsPillar(p1));
		
		//When ppath contains pillar;
		p.addPillar(p1);
		assertTrue(p.containsPillar(p1));
		
		//When ppath does not contain pillar
		assertFalse(p.containsPillar(p2));
	}
	
	/**
	 * Structural Basis, Good Data
	 */
	@Test
	public void testRemoveLastPillar(){
		Pillar p1 = new Pillar(0,0);
		Pillar p2 = new Pillar(1,0);
		Set<Pillar> expectedPillars = new HashSet<Pillar>();
		List<Pillar> expectedPPath = new LinkedList<Pillar>();
		int expectedDist = -1;
		
		assertEquals(p.lastPillar(), null);
		//Test when ppath is empty
		p.removeLastPillar(p1);
		assertEquals(p.lastPillar(), null);
		assertTrue(p.isSameDistance(expectedDist));
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(t.getPPath().equals(expectedPPath));
		
		//Test removing a pillar that is the last pillar
		p.addPillar(p1);
		p.removeLastPillar(p1);
		assertEquals(p.lastPillar(), null);
		assertTrue(p.isSameDistance(expectedDist));
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(t.getPPath().equals(expectedPPath));
		
		//Test removing a pillar that is not the last pillar
		p.addPillar(p2);
		p.removeLastPillar(p1);
		expectedPillars.add(p2);
		expectedPPath.add(p2);
		assertEquals(p.lastPillar(), p2);
		assertTrue(p.isSameDistance(++expectedDist));
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(t.getPPath().equals(expectedPPath));
		
		//Test removing a pillar that is the last pillar but not also the first
		p.addPillar(p1);
		p.removeLastPillar(p1);
		assertEquals(p.lastPillar(), p2);
		assertTrue(p.isSameDistance(expectedDist));
		assertTrue(t.getPillars().containsAll(expectedPillars) && expectedPillars.containsAll(t.getPillars()));
		assertTrue(t.getPPath().equals(expectedPPath));
	}
	
	/**
	 * Structural Basis, Bad Data
	 * Test what happends when input is null
	 */
	@Test(expected=NullPointerException.class)
	public void testRemoveLastPillarWhenNull(){
		p.removeLastPillar(null);
	}
	
	/**
	 * Structural Basis, Good Data
	 */
	@Test
	public void testSetAndGetPlankGoodData(){
		Plank added1 = new Plank(new Pillar(0,0), new Pillar(1,0));
		Plank added2 = new Plank(new Pillar(2,1), new Pillar(2,2));
		p.setPlank(added1);
		assertEquals(added1, p.getPlank());
		assertFalse(added2.equals(p.getPlank()));
		p.setPlank(added2);
		assertEquals(added2, p.getPlank());
		assertFalse(added1.equals(p.getPlank()));
	}
	
	/**
	 * Bad Data/Null Data
	 */
	@Test
	public void testSetAndGetPlankBadData(){
		assertEquals(null, p.getPlank());
		p.setPlank(null);
		assertEquals(null, p.getPlank());
	}
	
	/**
	 * Structural Basis, Good data
	 */
	@Test
	public void testRemovePlank(){
		//Remove plank after init should result in plank == null
		p.removePlank();
		assertEquals(null, p.getPlank());
		//Remove plank after setting it should result in plank == null
		p.setPlank(new Plank(new Pillar(0,0), new Pillar(1,0)));
		p.removePlank();
		assertEquals(null, p.getPlank());
	}
	
	/**
	 * Structured Basis, Good data
	 */
	@Test
	public void testLastPillar(){
		assertEquals("Last Pillar is null when there are no pillars in path.",p.lastPillar(),null);
		Pillar p1 = new Pillar(0,0);
		p.addPillar(p1);
		assertEquals(p1,p.lastPillar());
	}
	
	/**
	 * Structured Basis
	 */
	@Test
	public void testToString(){
		p.addPillar(new Pillar(0,0));
		p.addPillar(new Pillar(1,0));
		p.addPillar(new Pillar(2,0));
		p.addPillar(new Pillar(2,1));
		p.addPillar(new Pillar(2,2));
		String expected = "(0,0) --> (1,0) --> (2,0) --> (2,1) --> (2,2)";
		assertEquals(p.toString(), expected);
	}
	
	/**
	 * Structural Basis, Good data
	 */
	@Test
	public void testEquals(){
		//Paths should equal themselves
		assertTrue(p.equals(p));
		
		Path p1 = new Path();
		//True when all parts of the path are equal;
		assertTrue(p.equals(p1));
		assertTrue(p1.equals(p));
		
		//False when there are different pillars/distance is different
		p1.addPillar(new Pillar(0, 1));
		assertFalse(p.equals(p1));
		assertFalse(p1.equals(p));
		
		//False when there distance is different
		p.addPillar(new Pillar(1, 1));
		assertFalse(p.equals(p1));
		assertFalse(p1.equals(p));
		
		//False when planks are not the same
		p1 = new Path();
		p1.setPlank(new Plank(new Pillar(0, 1), new Pillar(1,1)));
		assertFalse(p.equals(p1));
		assertFalse(p1.equals(p));
	}
	
	/**
	 * Bad data
	 */
	@Test
	public void testEqualsNull(){
		assertFalse(p.equals(null));
	}
	
	/**
	 * Structured Basis, Good data
	 */
	@Test
	public void testCheckNullWhenNotNull(){
		t.testCheckNull(5);
	}
	
	/**
	 * Structured Basis, Good data, null data
	 */
	@Test(expected=NullPointerException.class)
	public void testCheckNullWhenNull(){
		t.testCheckNull(null);
	}
}
