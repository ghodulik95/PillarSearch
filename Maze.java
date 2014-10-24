import java.util.List;
import java.util.Set;

/**
 * Shortest path code straight from psuedocode
 * @author gmh73
 *
 */
public class Maze {
	private int maxCoordinate;
	private Path curPath;
	private Pillar curPil;
	private Path shortest;
	private int shortestDistance;
	private Pillar endPillar;
	private Set<Plank> layout;
	private Pillar startPillar;
	
	/**
	 * Constructor for Maze Class
	 * @param n the length and width of grid of pillars
	 * @param layout	the set of planks in the grid
	 * @param start		the start point 
	 * @param end		the end point
	 */
	public Maze(int n, Set<Plank> layout, Pillar start, Pillar end){
		checkInitInput(n,layout);
		//Set the max coordinate of the n x n grid to n - 1
		maxCoordinate = n - 1;
		//Initialize the current path
		curPath = new Path();
		//Initialize the shortest path found to a path of distance infinite
		shortest = new Path();
		shortest.setDistanceToInfinite();
		//Set the current pillar and the start pillar to the given start
		curPil = start;
		startPillar = start;
		//Set the end pillar to the given end
		endPillar = end;
		//calculate the shortest possible distance from the start pillar to the end pillar
		shortestDistance = curPil.shortestDistanceTo(endPillar);
		//Set the plank layout to the given layout
		this.layout = layout;
	}
	
	/**
	 * Default constructor if no start and and is specified
	 * Start is (0,0) and end is (n-1,n-1)
	 * @param n  The length of the grid
	 * @param layout  A set of planks
	 */
	public Maze(int n, Set<Plank> layout){
		this(n, layout, new Pillar(0,0), new Pillar(n - 1, n - 1));
	}
	
	/**
	 * Resets the search values
	 */
	public void resetShortestPath(){
		curPil = startPillar;
		curPath = new Path();
	}
	
	/**
	 * Throws exception if n < 0 or layout == null
	 * @param n throws IndexOutOfBoundsException if n < 0
	 * @param layout throws NullPointerException if layout == null
	 */
	private void checkInitInput(int n, Set<Plank> layout){
		if(n < 1)
			throw new IndexOutOfBoundsException("Given maze size < 0.");
		if(layout == null)
			throw new NullPointerException("Given layout is null.");
	}
	
	/**
	 * Finds the shortest path recursively
	 * Basically a depth first search that, upon finishing, says
	 * "Is there a possible shorter path?" and continues to search if there is
	 * @param hasPlank	true if we have not yet used the additional plank
	 * @return	returns the shortest path found
	 */
	public Path shortestPath(boolean hasPlank) {
		/*Add curPil to curPath*/
		Path atLastPillar = pathAtLastPillar();
		//If the path is at the last pillar, ie it is finished, or it is longer
		//than an already found shortest path, return here
		if(atLastPillar != null)
			return atLastPillar;
		/*If hasPlank is true, search the adjoining pillars found by using the plank*/
		if(hasPlank){
			//Get the shortest path found by using this plank
			Path pPrime = searchAdjoiningPillars(true,false);
			//If the found path is the shortest distance possible, the return it
			if(pPrime.isSameDistance(shortestDistance))
				return pPrime;
		}
		//Search the adjoining pillars found by not using a plank
		//Note that if we still do have a plank, it is carried to the next pillar
		//by including the hasPlank parameter
		Path pPrime = searchAdjoiningPillars(false,hasPlank);
		//If the found path is the shortest distance possible, the return it
		if(pPrime.isSameDistance(shortestDistance))
			return pPrime;
		//If this is reached, then the shortest path is whatever is stored in shortest
		return shortest;
	}
	
	/**
	 * Search the adjoining pillars based on whether or not we are using a plank
	 * @param usePlank	true if we are going to use a plank here
	 * @param hasPlankAfter		true if we have a plank after this search
	 * @return
	 */
	private Path searchAdjoiningPillars(boolean usePlank, boolean hasPlankAfter) {
		/*For each Pillar P that is unconnected (Plank between does not exist in layout), adjacent to curPil and not in curPath 
		 * OR (if we are are not using a plank) For each Pillar P that is connected and adjacent to curPil do
		*/
		Pillar prev = curPil;
		//Get the adjoining pillars based on whether or not we are using the plank
		List<Pillar> adjoiningPillars = curPil.adjoiningPillars(usePlank, layout, maxCoordinate);
		for(Pillar p : adjoiningPillars){
			curPil = p;
			//If we are using the plank, set the path plank to the added plank
			if(usePlank){
				/*Plank C ← a new Plank connecting curPil and P
				Add C to curPath*/
				Plank c = new Plank(curPil, prev);
				curPath.setPlank(c);
			}
			//Search the path from this new pillar, remembering if we have a plank after or not
			Path pPrime = shortestPath(hasPlankAfter);
			//If the result is the shortest distance possible, return it
			pPrime = checkIfResultShortest(pPrime);
			if(pPrime != null)
				return pPrime;
		/*set curPath to pre-search state by removing C from curPath and removing P from curPath*/
			if(usePlank)
				curPath.removePlank();
			curPath.removeLastPillar(p);
			//Set the curPil back to what it originally was
			curPil = prev;
		}
		//return whatever is stored in shortest
		return shortest;
	}
	
	/**
	 * Returns a shortest path if curPath is done, null otherwise.
	 * curPath is done if curPil is in curPath, or curPil is the end pillar
	 * or curPath is not shorter than the shortest path
	 * @return	a shortest path, or null if we're not there yet
	 */
	private Path pathAtLastPillar() {
		//If curPil is already in curPath
		if(curPath.containsPillar(curPil))
			//return whatever the current shortest path is now
			return shortest;
		//Add curPil in curPath since it is not previously in it
		curPath.addPillar(curPil);
		/*If curPil is endPillar */
		if(curPil.equals(endPillar))
			/*	return curPath, we are done searching this particular path*/
			return curPath;
		/*If curPath is longer than or the same distance as shortest*/
		if(!curPath.isShorterThan(shortest))
			/*return shortest, no point in continuing*/
			return shortest;
		return null;
	}

	/**
	 * Returns a shortest path if pPrims is the shortest possible distance.
	 * Sets the shortest to pPrime also.
	 * @param pPrime	a Path found from start to end
	 * @return	returns pPrime if it is a shortest possible path, null otherwise
	 */
	private Path checkIfResultShortest(Path pPrime){
		/*If P’ has a shorter distance than shortest
		shortest ← P’*/
		if(pPrime.isShorterThan(shortest))
			shortest = pPrime.copy();
		/*If P’ has a distance shortestDistance, return P’*/
		if(pPrime.isSameDistance(shortestDistance))
			return pPrime;
		//Return null if pPrime is not the shortest distsance possible
		return null;
	}
	
	/**
	 * A test button for testing
	 * @author gmh73
	 *
	 */
	public class TestButton{
		/**
		 * Return the max coordinate
		 * @return this max coord
		 */
		public int getMaxCoordinate(){
			return Maze.this.maxCoordinate;
		}
		/**
		 * Return the curPath
		 * @return this curPath
		 */
		public Path getCurPath(){
			return Maze.this.curPath;
		}
		/**
		 * Return the curPil
		 * @return this curPil
		 */
		public Pillar getCurPil(){
			return Maze.this.curPil;
		}
		/**
		 * Return the shortest path found
		 * @return this shortest
		 */
		public Path getShortest(){
			return Maze.this.shortest;
		}
		/**
		 * Return the shortest distance possible
		 * @return this shortest distance
		 */
		public int getShortestDistance(){
			return Maze.this.shortestDistance;
		}
		/**
		 * Return the end pillar
		 * @return	this end pillar
		 */
		public Pillar getEndPillar(){
			return Maze.this.endPillar;
		}
		/**
		 * Return the layout
		 * @return	this layout
		 */
		public Set<Plank> getLayout(){
			return Maze.this.layout;
		}
		/**
		 * Return the start pillar
		 * @return this start pillar
		 */
		public Pillar getStart(){
			return Maze.this.startPillar;
		}
		/**
		 * Calls searchAdjoiningPillars
		 * @param start		the start pillar we want to set to start
		 * @param end	the end pillar we want to set to end
		 * @param usePlank	whether or not we want to use a plank
		 * @param hasPlankAfter		whether or not we have a plank after
		 * @return	return the shortest path found
		 */
		public Path testSearchAdjoiningPillars(Pillar start, Pillar end, boolean usePlank, boolean hasPlankAfter){
			//Save the current state of this maze
			Path curPathCopy = Maze.this.curPath;
			Pillar curPilCopy = Maze.this.curPil;
			Path shortestCopy = Maze.this.shortest;
			Pillar endPillarCopy = Maze.this.endPillar;
			int shortestDistanceCopy = Maze.this.shortestDistance;
			
			//Set the current state of this maze to what we want for testing
			Maze.this.curPath = new Path();
			Maze.this.curPath.addPillar(start);
			Maze.this.curPil = start;
			Maze.this.shortest = new Path();
			shortest.setDistanceToInfinite();
			Maze.this.endPillar = end;
			Maze.this.shortestDistance = start.shortestDistanceTo(end);
			
			//Call searchAdjoiningPillars
			Path out = Maze.this.searchAdjoiningPillars(usePlank, hasPlankAfter);
			
			//Revert maze state back to what it was originally
			Maze.this.curPath = curPathCopy;
			Maze.this.curPil = curPilCopy;
			Maze.this.shortest = shortestCopy;
			Maze.this.endPillar = endPillarCopy;
			Maze.this.shortestDistance = shortestDistanceCopy;
			
			//Return the result of the search
			return out;
		}
		/**
		 * Calls pathAtLastPillar
		 * @param middle	a middle pillar
		 * @param end	an end pillar
		 * @param curPath	a current Path
		 * @param shortestPath	a shortets Possible path
		 * @return
		 */
		public Path testPathAtLastPillar(Pillar middle, Pillar end, Path curPath, Path shortestPath){
			//Save the current state of this maze
			Path curPathCopy = Maze.this.curPath;
			Pillar curPilCopy = Maze.this.curPil;
			Path shortestCopy = Maze.this.shortest;
			Pillar endPillarCopy = Maze.this.endPillar;
			
			//Set the current state of the maze for testing
			Maze.this.curPath = curPath;
			Maze.this.curPil = middle;
			Maze.this.shortest = shortestPath;
			Maze.this.endPillar = end;
			
			//Call path at last pillar
			Path out = Maze.this.pathAtLastPillar();
			
			//Revert the state of the maze to original state
			Maze.this.curPath = curPathCopy;
			Maze.this.curPil = curPilCopy;
			Maze.this.shortest = shortestCopy;
			Maze.this.endPillar = endPillarCopy;
			
			//return the result
			return out;
		}
		
		/**
		 * Calls checkIfResultShortest
		 * Since this method changes two things, I return an array containing the two things changed
		 * @param pPrime	a found path
		 * @param shortest	a currently shortest path
		 * @param shortestDistance	a shortest possibled distance
		 * @return returns the changes from calling checkIfResultShortest
		 */
		public Path[] testCheckIfResultShortest(Path pPrime, Path shortest, int shortestDistance){
			//Save the current state of the maze
			Path curPathCopy = Maze.this.curPath;
			Path shortestCopy = Maze.this.shortest;
			int shortestDistanceCopy = Maze.this.shortestDistance;
			
			//Set the current state of the maze for testing
			Maze.this.curPath = curPath;
			Maze.this.shortest = shortest;
			Maze.this.shortestDistance = shortestDistance;
			
			//Call checkIfResultShortest and store results
			Path[] ret = new Path[2];
			ret[0] = Maze.this.checkIfResultShortest(pPrime);
			ret[1] = Maze.this.shortest;
				
			//Revert the state of the maze back to the original
			Maze.this.curPath = curPathCopy;
			Maze.this.shortest = shortestCopy;
			Maze.this.shortestDistance = shortestDistanceCopy;
			
			//Return the changes
			return ret;
		}
		/**
		 * Call testInitInput
		 * @param n a length
		 * @param layout	a plank layout
		 */
		public void testCheckInitInput(int n, Set<Plank> layout){
			Maze.this.checkInitInput(n,layout);
		}
	}
}
