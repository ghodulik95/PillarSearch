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
	
	public Path(){
		distance = -1;
		pillars = new HashSet<Pillar>();
		ppath = new LinkedList<Pillar>();
		addedPlank = null;
	}
	
	public void add(Pillar p){
		if(ppath.isEmpty() || p.isAdjectedTo(lastPillar())){
			ppath.add(p);
			pillars.add(p);
			distance++;
		}
	}
	
	public boolean isShorterThan(Path p){
		return this.distance < p.distance;
	}
	
	public boolean isShorterThan(int dist){
		return this.distance < dist;
	}
	
	public boolean isSameDistance(Path p){
		return this.distance == p.distance;
	}
	
	public boolean isSameDistance(int dist){
		return this.distance == dist;
	}
	
	public void setDistanceToInfinite(){
		if(ppath.isEmpty())
			distance = Integer.MAX_VALUE;
	}
	
	public void set(Path p){
		distance = p.distance;
		pillars = p.pillars;
		ppath = p.ppath;
		addedPlank = p.addedPlank;
	}
	
	public void removeLastPillar(Pillar lastP){
		if(lastP.equals(lastPillar())){
			ppath.remove(ppath.size() - 1);
			pillars.remove(lastP);
			distance--;
		}
	}
	
	public void addPlank(Plank pl){
		addedPlank = pl;
	}
	
	public void removePlank(){
		addedPlank = null;
	}
	
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
