
public class vertexTraversal {
	public String label;        // label (e.g. "A")
	   public boolean wasVisited;
	   public boolean wasGrouped;	// for SCC
	   public int nextNeighbor;
	   
	   public vertexTraversal(String lab) {  // constructor
	      label = lab;
	      wasVisited = false;
	      wasGrouped = false;
	      nextNeighbor = 0;
	   }
	   
	   public void reset() {
		  wasVisited = false;
		  nextNeighbor = 0;
	   }

}
