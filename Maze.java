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
	

	/*n is the length/width of the n x n layout of pillars
	layout is the set of planks used
	ShortestPath(n, layout)*/
	public Maze(int n, Set<Plank> layout, Pillar start, Pillar end){
		checkInitInput(n,layout);
		/*	set limit of Pillar to n - 1*/
		maxCoordinate = n - 1;
	/*	Path start ← a new Path with no Pillars and a distance of -1*/
		curPath = new Path();
	/*	Path shortest ← a new Path with distance of infinite*/
		shortest = new Path();
		shortest.setDistanceToInfinite();
	/*	Pillar curPil ← the initial pillar (presumably at (0,0) based on assignment desc)*/
		curPil = start;
	/*	hasPlank ← true*/
	/*calculate the shortest possible distance from the start pillar to the end pillar and store it in variable shortestDistance
	make a new Pillar endPillar that is the end destination*/
		endPillar = end;
		shortestDistance = curPil.shortestDistanceTo(endPillar);
		/*return search( curPil, start, hasPlank, shortest, shortestDistance, endPillar, n, layout)*/
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
	 * Throws exception if n < 0 or layout == null
	 * @param n throws IndexOutOfBoundsException if n < 0
	 * @param layout throws NullPointerException if layout == null
	 */
	private void checkInitInput(int n, Set<Plank> layout){
		if(n < 0)
			throw new IndexOutOfBoundsException("Given maze size < 0.");
		if(layout == null)
			throw new NullPointerException("Given layout is null.");
	}
	
	public Path shortestPath(boolean hasPlank) {
		/*Add curPil to curPath*/
		Path atLastPillar = pathAtLastPillar();
		if(atLastPillar != null)
			return atLastPillar;
	/*If hasPlank is true*/
		if(hasPlank){
			Path pPrime = searchAdjoiningPillars(true,false);
			if(pPrime.isSameDistance(shortestDistance))
				return pPrime;
		}
		Path pPrime = searchAdjoiningPillars(false,hasPlank);
		if(pPrime.isSameDistance(shortestDistance))
			return pPrime;
	/*return shortest*/
		return shortest;
	}

	private Path searchAdjoiningPillars(boolean usePlank, boolean hasPlankAfter) {
		/*For each Pillar P that is unconnected (Plank between does not exist in layout), adjacent to curPil and not in curPath 
		 * OR (if we are are not using a plank) For each Pillar P that is connected and adjacent to curPil do
		*/
		Pillar prev = curPil;
		List<Pillar> adjoiningPillars = curPil.adjoiningPillars(usePlank, layout, maxCoordinate);
		for(Pillar p : adjoiningPillars){
			curPil = p;
			if(usePlank){
				/*Plank C ← a new Plank connecting curPil and P
				Add C to curPath*/
				Plank c = new Plank(curPil, prev);
				curPath.setPlank(c);
			}
			/*Path P’ ← search(P, curPath, hasPlankAfter, shortest, shortestDistance, endPillar, n, layout)*/
			Path pPrime = shortestPath(hasPlankAfter);
			pPrime = checkIfResultShortest(pPrime);
			if(pPrime != null)
				return pPrime;
		/*set curPath to pre-search state by removing C from curPath and removing P from curPath*/
			if(usePlank)
				curPath.removePlank();
			curPath.removeLastPillar(p);
			curPil = prev;
		}
		return shortest;
	}
	
	
	private Path pathAtLastPillar() {
		if(curPath.containsPillar(curPil))
			return shortest;
		curPath.addPillar(curPil);
		/*If curPil is endPillar */
		if(curPil.equals(endPillar))
			/*	return curPath*/
			return curPath;
		/*If curPath is longer than or the same distance as shortest*/
		if(!curPath.isShorterThan(shortest))
			/*return shortest*/
			return shortest;
		return null;
	}


	
	private Path checkIfResultShortest(Path pPrime){
		/*If P’ has a distance shortestDistance, return P’*/
		if(pPrime.isSameDistance(shortestDistance))
			return pPrime;
		/*If P’ has a shorter distance than shortest
			shortest ← P’*/
		if(pPrime.isShorterThan(shortest))
			shortest = pPrime.copy();
		return null;
	}
	
	public class TestButton{
		public int getMaxCoordinate(){
			return Maze.this.maxCoordinate;
		}
		public Path getCurPath(){
			return Maze.this.curPath;
		}
		public Pillar getCurPil(){
			return Maze.this.curPil;
		}
		public Path getShortest(){
			return Maze.this.shortest;
		}
		public int getShortestDistance(){
			return Maze.this.shortestDistance;
		}
		public Pillar getEndPillar(){
			return Maze.this.endPillar;
		}
		public Set<Plank> getLayout(){
			return Maze.this.layout;
		}
		public Path testSearchAdjoiningPillars(Pillar start, Pillar end, boolean usePlank, boolean hasPlankAfter){
			Path curPathCopy = Maze.this.curPath;
			Pillar curPilCopy = Maze.this.curPil;
			Path shortestCopy = Maze.this.shortest;
			Pillar endPillarCopy = Maze.this.endPillar;
			int shortestDistanceCopy = Maze.this.shortestDistance;
			
			Maze.this.curPath = new Path();
			Maze.this.curPath.addPillar(start);
			Maze.this.curPil = start;
			Maze.this.shortest = new Path();
			shortest.setDistanceToInfinite();
			Maze.this.endPillar = end;
			Maze.this.shortestDistance = start.shortestDistanceTo(end);
			
			Path out = Maze.this.searchAdjoiningPillars(usePlank, hasPlankAfter);
			
			Maze.this.curPath = curPathCopy;
			Maze.this.curPil = curPilCopy;
			Maze.this.shortest = shortestCopy;
			Maze.this.endPillar = endPillarCopy;
			Maze.this.shortestDistance = shortestDistanceCopy;
			
			return out;
		}
		public Path testPathAtLastPillar(Pillar start, Pillar middle, Pillar end, Path shortestPath){
			Path curPathCopy = Maze.this.curPath;
			Pillar curPilCopy = Maze.this.curPil;
			Path shortestCopy = Maze.this.shortest;
			Pillar endPillarCopy = Maze.this.endPillar;
			
			Maze.this.curPath = new Path();
			Maze.this.curPath.addPillar(start);
			Maze.this.curPil = middle;
			Maze.this.shortest = shortestPath;
			Maze.this.endPillar = end;
			
			Path out = Maze.this.pathAtLastPillar();
			
			Maze.this.curPath = curPathCopy;
			Maze.this.curPil = curPilCopy;
			Maze.this.shortest = shortestCopy;
			Maze.this.endPillar = endPillarCopy;
			
			return out;
		}
	}
}
