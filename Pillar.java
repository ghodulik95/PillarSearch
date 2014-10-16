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
	
	public Pillar(int xCor, int yCor){
		checkCoorValid(xCor);
		checkCoorValid(yCor);
		x = xCor;
		y = yCor;
	}
	
	public int getXCor(){
		return x;
	}
	
	public int getYCor(){
		return y;
	}
	
	public boolean isAdjectedTo(Pillar p){
		return shortestDistanceTo(p) == 1;
	}
	
	public int shortestDistanceTo(Pillar p){
		int xDiff = Math.abs(x - p.getXCor());
		int yDiff = Math.abs(y - p.getYCor());
		return xDiff + yDiff;
	}
	
	private void checkCoorValid(int cor) throws IndexOutOfBoundsException{
		if(cor < 0 || cor > limit)
			throw new IndexOutOfBoundsException("Given coordinate is out of bounds");
	}
	
	public static void setLimit(int l){
		limit = l;
	}
	
	public List<Pillar> adjacentPillars(boolean usePlank, Set<Plank> layout){
		List<Pillar> adjP = new LinkedList<Pillar>();
		List<Pillar> allAdjP = getAllAdjacentPillars();
		for(Pillar p : allAdjP){
			Plank conn = new Plank(this, p);
			if(connectionValid(usePlank, conn, layout)){
				adjP.add(p);
			}
		}
		return adjP;
	}
	
	private boolean connectionValid(boolean usePlank, Plank conn,
			Set<Plank> layout) {
		if(usePlank && !layout.contains(conn))
			return true;
		else if (!usePlank && layout.contains(conn))
			return true;
		else
			return false;
	}

	private List<Pillar> getAllAdjacentPillars() {
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
		if(o instanceof Pillar){
			Pillar p = (Pillar) o;
			return p.getXCor() == x && p.getYCor() == y;
		}else
			return false;
	}
	
	@Override
	public int hashCode(){
		return 2^x + 5^y;
	}
	
	@Override
	public String toString(){
		return "("+x+","+y+")";
	}
	
}
