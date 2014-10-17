import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Path class contains
 * A distance, which can be any integer from -1 to infinite
 * A set of Pillars with constant time contains? check
 * A list of Pillars in order from start to current
 * A plank that is empty if there was no extra plank used in the path, or containing the Plank used if one was used
 * @author gmh73
 *
 */
public class Path {
	 /* A distance, which can be any integer from -1 to infinite*/
	private int distance;
	 /* A set of Pillars with constant time contains? check*/
	private Set<Pillar> pillars;
	 /* A list of Pillars in order from start to current*/
	private List<Pillar> ppath;
	 /* A plank that is empty if there was no extra plank used in the path, or containing the Plank used if one was used*/
	Plank addedPlank;
	
	/**
	 * Constructs a new path with distance -1 and empty set/list of pillars
	 */
	public Path(){
		distance = -1;
		pillars = new HashSet<Pillar>();
		ppath = new LinkedList<Pillar>();
		addedPlank = null;
	}
	
	/**
	 * Adds an adjacent pillar to the path
	 * @param p a pillar
	 */
	public void addPillar(Pillar p){
		if(ppath.isEmpty() || p.isAdjectedTo(lastPillar())){
			ppath.add(p);
			pillars.add(p);
			distance++;
		}
	}
	
	/**
	 * returns true if this path is shorter than p
	 * @param p a path
	 * @return	true if this is shorter than p
	 */
	public boolean isShorterThan(Path p){
		return this.distance < p.distance;
	}
	
	/**
	 * Checks if distance is less than given dist
	 * @param dist	a distance
	 * @return	returns true if this distance is less than given distance
	 */
	public boolean isShorterThan(int dist){
		return this.distance < dist;
	}
	
	/**
	 * Checks if distance of this and p are the same
	 * @param p	a path
	 * @return	returns true if distances are the same
	 */
	public boolean isSameDistance(Path p){
		return this.distance == p.distance;
	}
	
	/**
	 * Checks if this distance is the same as the given distance
	 * @param dist	a distance
	 * @return	returns true if same distance
	 */
	public boolean isSameDistance(int dist){
		return this.distance == dist;
	}
	
	/**
	 * sets this distance to infinite
	 */
	public void setDistanceToInfinite(){
		if(ppath.isEmpty())
			distance = Integer.MAX_VALUE;
	}
	
	/**
	 * Sets this path to the given path
	 * @param p	 a path
	 */
	public void setPath(Path p){
		distance = p.distance;
		pillars = p.pillars;
		ppath = p.ppath;
		addedPlank = p.addedPlank;
	}
	
	/**
	 * Removes the last pillar, given the last pillar
	 * @param lastP	the last pillar in this path
	 */
	public void removeLastPillar(Pillar lastP){
		if(lastP.equals(lastPillar())){
			ppath.remove(ppath.size() - 1);
			pillars.remove(lastP);
			distance--;
		}
	}
	
	/**
	 * Adds a plank to this path
	 * @param pl	 a plank
	 */
	public void addPlank(Plank pl){
		addedPlank = pl;
	}
	
	/**
	 * Sets the plank of this path to null
	 */
	public void removePlank(){
		addedPlank = null;
	}
	
	/**
	 * gets the last pillar in the path
	 * @return	the last pillar in the path
	 */
	public Pillar lastPillar(){
		return ppath.get(ppath.size() - 1);
	}
	
	@Override
	public String toString(){
		String out = "";
		for(Pillar p : ppath){
			out += p.toString() + " --> ";
		}
		return out;
	}
}
