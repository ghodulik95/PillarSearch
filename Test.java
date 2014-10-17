import java.util.HashSet;
import java.util.Set;

/**
 * A very simple test case where n = 2
 * @author gmh73
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 2;
		Set<Plank> layout = new HashSet<Plank>();
		Pillar p1 = new Pillar(0,0);
		Pillar p2 = new Pillar(1,0);
		Pillar p3 = new Pillar(0,1);
		Pillar p4 = new Pillar(1,1);
		
		Plank pl1 = new Plank(p1, p2);
		Plank pl2 = new Plank(p2, p4);
		
		layout.add(pl1);
		//layout.add(pl2);
		
		Maze m = new Maze(n, layout);
		Path s = (m.shortestPath(true));
		
		System.out.println(s.toString()+s.addedPlank.getPillars());
		
	}

}
