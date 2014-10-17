import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Pillar class contains integers x and y coordinates of a pillar.  x and y are between 0 and specified limit
 * @author gmh73
 *
 */
public class Pillar {
	private int x;
	private int y;
	private static int limit = 0;
	
	/**
	 * Constructs a new pillar at position xCor and yCor
	 * @param xCor the x coordinate
	 * @param yCor the y coordinate
	 */
	public Pillar(int xCor, int yCor){
		checkCoorValid(xCor);
		checkCoorValid(yCor);
		x = xCor;
		y = yCor;
	}
	
	/**
	 * Gets x coordinate
	 * @return returns x coordinate
	 */
	public int getXCor(){
		return x;
	}
	
	/**
	 * Gets y coordinate
	 * @return returns y coordinate
	 */
	public int getYCor(){
		return y;
	}
	
	/**
	 * Checks if this pillar is adjacent to p
	 * @param p A pillar
	 * @return returns true if pillars are adjacent
	 */
	public boolean isAdjectedTo(Pillar p){
		return shortestDistanceTo(p) == 1;
	}
	
	/**
	 * Calculates the shortest distance from this pillar to p
	 * @param p		a pillar
	 * @return	returns the distance from this pillar to p
	 */
	public int shortestDistanceTo(Pillar p){
		int xDiff = Math.abs(x - p.getXCor());
		int yDiff = Math.abs(y - p.getYCor());
		return xDiff + yDiff;
	}
	
	/**
	 * Checks if a coordinate is valid
	 * @param cor an x or y coordinate
	 * @throws IndexOutOfBoundsException	thrown if cor is lessthan 0 or greaterthan limit
	 */
	private void checkCoorValid(int cor) throws IndexOutOfBoundsException{
		if(cor < 0 || cor > limit)
			throw new IndexOutOfBoundsException("Given coordinate is out of bounds");
	}
	
	/**
	 * Sets the limit of Pillar class to l
	 * @param l		an integer that is the limit
	 */
	public static void setLimit(int l){
		limit = l;
	}
	
	/**
	 * All adjoining pillars (connected pillars if we do not use the plank,
	 * unconnected if we do use the plank)
	 * @param usePlank	true if we are using the extra plank
	 * @param layout	the layout of the planks
	 * @return	returns the adjoining pillars
	 */
	public List<Pillar> adjoiningPillars(boolean usePlank, Set<Plank> layout){
		List<Pillar> adjoiningP = new LinkedList<Pillar>();
		//get all adjacent pillars
		List<Pillar> adjacentP = adjacentPillars();
		//check if each pillar is adjacent
		for(Pillar p : adjacentP){
			Plank conn = new Plank(this, p);
			if(connectionAdjoined(usePlank, conn, layout)){
				adjoiningP.add(p);
			}
		}
		return adjoiningP;
	}
	
	/**
	 * Checks if the connection between two pillars is adjoining
	 * @param usePlank true if we are using an extra plank
	 * @param conn	the plank connecting two pillars
	 * @param layout	the set of all planks already used
	 * @return	returns true if the pillars connected by conn are adjoined, false otherwise
	 */
	private boolean connectionAdjoined(boolean usePlank, Plank conn,
			Set<Plank> layout) {
		//If we are using a plank and the plank conn is not in the layout
		if(usePlank && !layout.contains(conn))
			return true;
		//if we are not using a plank and conn is in the layout
		else if (!usePlank && layout.contains(conn))
			return true;
		else
			return false;
	}

	/**
	 * Gets all pillars adjacent to this one
	 * @return	a list of adjacent pillars
	 */
	private List<Pillar> adjacentPillars() {
		List<Pillar> allAdjP = new LinkedList<Pillar>();
		Pillar p;
		if(x != limit){
			p = new Pillar(x + 1, y);
			allAdjP.add(p);
		}
		if(y != limit){
			p = new Pillar(x, y+1);
			allAdjP.add(p);
		}
		if(x != 0){
			p = new Pillar(x - 1, y);
			allAdjP.add(p);
		}
		if(y != 0){
			p = new Pillar(x, y-1);
			allAdjP.add(p);
		}
		return allAdjP;
	}

	@Override
	public boolean equals(Object o){
		if(o != null && o instanceof Pillar){
			Pillar p = (Pillar) o;
			return p.getXCor() == x && p.getYCor() == y;
		}else
			return false;
	}
	
	@Override
	public int hashCode(){
		return 57*x + 23*y;
	}
	
	@Override
	public String toString(){
		return "("+x+","+y+")";
	}
	
}
