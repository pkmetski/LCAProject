package Logic2;

import java.util.List;
import java.util.ArrayList;
public class EdgesOfNode
{
	private List<int[]> edges;
	public int next;
	
	public EdgesOfNode(){edges=new ArrayList<int[]>();}
	
	public List<int[]> getAdjacents(){return edges;}

	public void addAdjacent(int[] e){edges.add(e);}
}