import static org.junit.Assert.*;
import java.util.Random;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

public class TestMaze {
	private Maze m;
	private Maze.TestButton t;
	private int n;
	private Set<Plank> layout;
	private Random r;
	private Path expectedShortest;
	
	@Before
	public void initialize(){
		n = 10;
		layout = new HashSet<Plank>();
		r = new Random();
		expectedShortest = new Path();
		buildLayout(n,0,false);
		m = new Maze(n, layout);
		t = m.new TestButton();
	}
	
	/**
	 * Structural Basis
	 * UsePlank is false
	 * -- only one path exists with no extraneous planks
	 */
	@Test
	public void testShortestPathNominal(){
		assertEquals(expectedShortest, m.shortestPath(false));
	}
	
	/**
	 * Structural Basis
	 * UsePlank is true
	 * -- only one path exists with no extraneous planks
	 *  + one plank is missing
	 */
	@Test
	public void testShortestPathUsePlank(){
		resetLayout();
		buildLayout(n,0,true);
		m = new Maze(n,layout);
		assertEquals(expectedShortest, m.shortestPath(true));
	}
	
	public void resetLayout(){
		layout = new HashSet<Plank>();
		expectedShortest = new Path();
	}
	
	/**
	 * Bad data -- layout == null
	 */
	@Test(expected=NullPointerException.class)
	public void testShortestPathWhenNull(){
		m = new Maze(n, null);
	}
	
	/**
	 * Bad data -- n < 0
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testShortestPathWhenNeg(){
		m = new Maze(-1, layout);
	}
	
	/**
	 * Bad Data, layout is empty (no path to finish)
	 * output should be a path with infinite distance
	 */
	@Test
	public void testShortestPathWhenLayoutEmpty(){
		m = new Maze(n, new HashSet<Plank>());
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		assertEquals(expectedShortest, m.shortestPath(true));
	}
	
	/**
	 * Bad Data, no Path to finish (but layout is not empty)
	 * output should be a path with infinite distance
	 */
	@Test
	public void testShortestPathWhenNoPath(){
		//reset layout to a new set
		layout = new HashSet<Plank>();
		//If I build a n/2 size layout, that means a path exists but not to finish
		buildLayout(n/2,0,false);
		m = new Maze(n, layout);
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		assertEquals(expectedShortest, m.shortestPath(true));
	}
	
	/**
	 * Bad Data, no Path to finish (but layout is not empty)
	 * output should be a path with infinite distance
	 */
	@Test
	public void testShortestPathWhenOnePlankMissingNoPlank(){
		//reset layout to a new set
		resetLayout();
		//If I build a n/2 size layout, that means a path exists but not to finish
		buildLayout(n,0,true);
		m = new Maze(n, layout);
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		assertEquals(expectedShortest, m.shortestPath(false));
	}


	public void buildLayout(int n2, double density, boolean usePlank){
		Pillar lastPillar = new Pillar(0,0);
		expectedShortest.addPillar(lastPillar);
		for(int i = 0; i < 2*(n2 - 1) ; i++){
			Pillar nextPillar = null;
			if(lastPillar.getXCor() < n2 - 1 && lastPillar.getYCor() < n2 -1){
				nextPillar = r.nextBoolean() ? new Pillar(lastPillar.getXCor() + 1, lastPillar.getYCor()) :
												new Pillar(lastPillar.getXCor(), lastPillar.getYCor() + 1);
			}else if(lastPillar.getXCor() < n2 - 1){
				nextPillar = new Pillar(lastPillar.getXCor() + 1, lastPillar.getYCor());
			}else if(lastPillar.getYCor() < n2 - 1){
				nextPillar = new Pillar(lastPillar.getXCor() , lastPillar.getYCor() + 1);
			}
			Plank p = new Plank(lastPillar, nextPillar);
			if(!(usePlank && i == n2/2)){
				layout.add(p);
			}else{
				expectedShortest.setPlank(p);
			}
			expectedShortest.addPillar(nextPillar);
			lastPillar = nextPillar;
		}
		
		//Add more planks proportional to density
		int edgesToAdd = (int) Math.floor( ((((n2 - 1)*n2)^2) * 1.0) * density);
		for(int i = 0; i < edgesToAdd; i++){
			Pillar start = new Pillar(r.nextInt(n2), r.nextInt(n2));
			List<Pillar> adjacent = start.adjoiningPillars(true, new HashSet<Plank>(), n2-1);
			int choose = r.nextInt(adjacent.size());
			Pillar end = adjacent.get(choose);
			Plank toAdd = new Plank(start,end);
			if(layout.contains(toAdd)){
				i--;
			}else{
				layout.add(toAdd);
			}
		}
	}
	
	/**
	 * Structured Basis
	 * No Adjoining Pillars, shortest path should be infinite
	 */
	@Test
	public void testAdjoiningPillarsWhenNoAdjoining(){
		resetLayout();
		m = new Maze(n,layout);
		t = m.new TestButton();
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		Pillar start = new Pillar(3,3);
		Pillar end = new Pillar(3,5);
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, false, false));
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, false, true));
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, true, false));
		//If we are using a planke and we have a plank after (true, true), we can make it from start
		//To finish, because we are adding two planks.  However, this would not happen in the application.
		assertFalse(expectedShortest.equals(t.testSearchAdjoiningPillars(start, end, true, true)));
	}
	
	/**
	 * Structured Basis
	 * Adjoining Pillars, not needing to use a plank
	 */
	@Test
	public void testAdjoiningPillarsNoPlank(){
		resetLayout();
		Pillar start = new Pillar(3,3);
		Pillar middle = new Pillar(3,4);
		Pillar end = new Pillar(3,5);
		layout.add(new Plank(start,middle));
		m = new Maze(n,layout);
		t = m.new TestButton();
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		//Even though there are some adjoining pillars, there is no viable path
		//so result should be infinite path
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, false, false));
		
		layout.add(new Plank(middle, end));
		expectedShortest = new Path();
		expectedShortest.addPillar(start);
		expectedShortest.addPillar(middle);
		expectedShortest.addPillar(end);
		//Now there is a path from start to end, so we should have the outputted path
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, false, false));
	}
	
	/**
	 * Structured Basis
	 * Adjoining Pillars, needing to use a plank
	 */
	@Test
	public void testAdjoiningPillarsWithPlank(){
		resetLayout();
		Pillar start = new Pillar(3,3);
		Pillar middle = new Pillar(3,4);
		Pillar end = new Pillar(3,5);
		layout.add(new Plank(start,middle));
		m = new Maze(n,layout);
		t = m.new TestButton();
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		//There is a possible path using a plank, but not a plank from start
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, true, false));
		
		expectedShortest = new Path();
		expectedShortest.addPillar(start);
		expectedShortest.addPillar(middle);
		expectedShortest.addPillar(end);
		expectedShortest.setPlank(new Plank(middle,end));
		//Now since hasPlankAfter is true, a plank should be added after the first hop
		Path result = t.testSearchAdjoiningPillars(start, end, false, true);
		assertTrue(true);
		assertEquals(expectedShortest, result);
		
		layout.add(new Plank(middle, end));
		expectedShortest = new Path();
		expectedShortest.addPillar(start);
		expectedShortest.addPillar(middle);
		expectedShortest.addPillar(end);
		//even though we have Planks, the shortest path should not have to use one
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, false, true));
		
		//Note that, even though a path seems logically reachable,
		//searchAdjoining should not find a path in this case when usePlank is true,
		//usePlank forces searchAdjoiningPillars to only consider connections that are not
		//in layout, in this case moving it away from the end
		//This is the main difference between searchAdjoining Pillars and shortestPath
		expectedShortest = new Path();
		expectedShortest.setDistanceToInfinite();
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, true, true));
		assertEquals(expectedShortest, t.testSearchAdjoiningPillars(start, end, true, false));
	}
	
	@Test
	public void testPathAtLastPillar(){
		
	}
}
