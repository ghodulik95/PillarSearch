import java.util.List;
import java.util.Set;

/**
 * Shortest path code straight from psuedocode
 * @author gmh73
 *
 */
public class Maze {
	private int length;
	private Path curPath;
	private Pillar curPil;
	private Path shortest;
	private int shortestDistance;
	private Pillar endPillar;
	private Set<Plank> layout;
	

	/*n is the length/width of the n x n layout of pillars
	layout is the set of planks used
	ShortestPath(n, layout)*/
	public Maze(int n, Set<Plank> layout){
		/*	set limit of Pillar to n - 1*/
		Pillar.setLimit(n - 1);
		length = n - 1;
	/*	Path start ← a new Path with no Pillars and a distance of -1*/
		curPath = new Path();
	/*	Path shortest ← a new Path with distance of infinite*/
		shortest = new Path();
		shortest.setDistanceToInfinite();
	/*	Pillar curPil ← the initial pillar (presumably at (0,0) based on assignment desc)*/
		curPil = new Pillar(0,0);
	/*	hasPlank ← true*/
	/*calculate the shortest possible distance from the start pillar to the end pillar and store it in variable shortestDistance
	make a new Pillar endPillar that is the end destination*/
		endPillar = new Pillar(n-1, n-1);
		shortestDistance = curPil.shortestDistanceTo(endPillar);
		/*return search( curPil, start, hasPlank, shortest, shortestDistance, endPillar, n, layout)*/
		this.layout = layout;
	}
	
	public Path shortestPath(boolean hasPlank) {
		/*Add curPil to curPath*/
		curPath.addPillar(curPil);
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
		List<Pillar> adjoiningPillars = curPil.adjoiningPillars(usePlank, layout);
		for(Pillar p : adjoiningPillars){
			curPil = p;
			if(usePlank){
				/*Plank C ← a new Plank connecting curPil and P
				Add C to curPath*/
				Plank c = new Plank(curPil, prev);
				curPath.addPlank(c);
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
			shortest = pPrime;
		return null;
	}
}
