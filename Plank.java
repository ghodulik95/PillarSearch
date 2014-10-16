import java.util.HashSet;
import java.util.Set;

/**
 * Plank class contains a set of two adjacent Pillars
 * @author gmh73
 *
 */

public class Plank {
	private Set<Pillar> connected;
	
	public Plank(Pillar p1, Pillar p2){
		Set<Pillar> conn = new HashSet<Pillar>();
		conn.add(p1);
		conn.add(p2);
		connected = conn;
	}
	
	public Set<Pillar> getPillars(){
		return connected;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Plank){
			Plank p = (Plank) o;
			return p.connected.containsAll(this.connected) && this.connected.containsAll(p.connected);
		}
		else
			return false;
	}
	
	@Override
	public int hashCode(){
		return connected.hashCode();
	}
}
