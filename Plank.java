import java.util.HashSet;
import java.util.Set;

/**
 * Plank class contains a set of two adjacent Pillars
 * @author gmh73
 *
 */

public class Plank {
	private Set<Pillar> connected;
	
	/**
	 * Construct a pillar connecting p1 and p2
	 * @param p1	a pillar
	 * @param p2	another pillar
	 */
	public Plank(Pillar p1, Pillar p2){
		checkIfNull(p1,p2);
		Set<Pillar> conn = new HashSet<Pillar>();
		conn.add(p1);
		conn.add(p2);
		connected = conn;
	}
	
	/**
	 * Checks if pillars are null
	 * @param p1	a pillar
	 * @param p2	a pillar
	 */
	private void checkIfNull(Pillar p1, Pillar p2) {
		if(p1 == null || p2 == null)
			throw new NullPointerException("Given pillar is null");
	}
	
	/**
	 * returns the set of connected pillars
	 * @return	returns set of connected pillars
	 */
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
	
	@Override
	public String toString(){
		String out = "[";
		for(Pillar p: connected){
			out += p.toString();
		}
		out += "]";
		return out;
	}
	
	/**
	 * A test button for junit testing
	 * @author gmh73
	 */
	public class TestButton{
		public void testCheckIfNull(Pillar p1, Pillar p2){
			Plank.this.checkIfNull(p1, p2);
		}
	}
}
