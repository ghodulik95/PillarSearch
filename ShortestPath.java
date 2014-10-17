import java.util.List;
import java.util.Set;

/**
 * Shortest path code straight from psuedocode
 * @author gmh73
 *
 */
public class ShortestPath {
	/*n is the length/width of the n x n layout of pillars
	layout is the set of planks used
	ShortestPath(n, layout)*/
	public static Path shortestPath(int n, Set<Plank> layout){
		/*	set limit of Pillar to n - 1*/
		Pillar.setLimit(n - 1);
	/*	Path start ← a new Path with no Pillars and a distance of -1*/
		Path start = new Path();
	/*	Path shortest ← a new Path with distance of infinite*/
		Path shortest = new Path();
		shortest.setDistanceToInfinite();
	/*	Pillar curPil ← the initial pillar (presumably at (0,0) based on assignment desc)*/
		Pillar curPil = new Pillar(0,0);
	/*	hasPlank ← true*/
		boolean hasPlank = true;
	/*calculate the shortest possible distance from the start pillar to the end pillar and store it in variable shortestDistance
	make a new Pillar endPillar that is the end destination*/
		Pillar endPillar = new Pillar(n-1, n-1);
		int shortestDistance = curPil.shortestDistanceTo(endPillar);
		/*return search( curPil, start, hasPlank, shortest, shortestDistance, endPillar, n, layout)*/
		return search( curPil, start, hasPlank, shortest, shortestDistance, endPillar, n, layout);
	}
	
	/*search(Pillar curPil, Path curPath, hasPlank,Path shortest,shortestDistance,endPillar,n,layout)*/
	public static Path search(Pillar curPil, Path curPath, boolean hasPlank, 
			Path shortest, int shortestDistance, Pillar endPillar, int n, Set<Plank> layout){
	/*Add curPil to curPath*/
		curPath.addPillar(curPil);
		Path atLastPillar = pathAtLastPillar(curPil, endPillar, curPath, shortest);
		if(atLastPillar != null)
			return atLastPillar;
	/*If hasPlank is true*/
		if(hasPlank){
			Path pPrime = searchAdjoiningPillars(curPil,curPath,true,shortest,shortestDistance,endPillar,n,layout,false);
			if(pPrime.isSameDistance(shortestDistance))
				return pPrime;
		}
		Path pPrime = searchAdjoiningPillars(curPil,curPath,false,shortest,shortestDistance,endPillar,n,layout,hasPlank);
		if(pPrime.isSameDistance(shortestDistance))
			return pPrime;
	/*return shortest*/
		return shortest;
	}
	
	private static Path pathAtLastPillar(Pillar curPil, Pillar endPillar,
			Path curPath, Path shortest) {
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

	private static Path searchAdjoiningPillars(Pillar curPil, Path curPath, boolean usePlank, 
			Path shortest, int shortestDistance, Pillar endPillar, int n, Set<Plank> layout, boolean hasPlankAfter){
		/*For each Pillar P that is unconnected (Plank between does not exist in layout), adjacent to curPil and not in curPath 
		 * OR (if we are are not using a plank) For each Pillar P that is connected and adjacent to curPil do
		*/
		List<Pillar> adjoiningPillars = curPil.adjoiningPillars(usePlank, layout);
		for(Pillar p : adjoiningPillars){
			if(usePlank){
				/*Plank C ← a new Plank connecting curPil and P
				Add C to curPath*/
				Plank c = new Plank(curPil, p);
				curPath.addPlank(c);
			}
			/*Path P’ ← search(P, curPath, hasPlankAfter, shortest, shortestDistance, endPillar, n, layout)*/
			Path pPrime = search(p,curPath, hasPlankAfter, shortest, shortestDistance, endPillar, n, layout);
			pPrime = checkIfResultShortest(pPrime, shortest, shortestDistance);
			if(pPrime != null)
				return pPrime;
		/*set curPath to pre-search state by removing C from curPath and removing P from curPath*/
			if(usePlank)
				curPath.removePlank();
			curPath.removeLastPillar(p);
		}
		return shortest;
	}
	
	private static Path checkIfResultShortest(Path pPrime, Path shortest, int shortestDistance){
		/*If P’ has a distance shortestDistance, return P’*/
		if(pPrime.isSameDistance(shortestDistance))
			return pPrime;
		/*If P’ has a shorter distance than shortest
			shortest ← P’*/
		if(pPrime.isShorterThan(shortest))
			shortest.setPath(pPrime);
		return null;
	}
}
