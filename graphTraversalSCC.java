import java.util.*;

class graphTraversalSCC {
    private int MAX_VERTS;
    private int MAX_EDGES;
    private vertexTraversal vertexList[]; // list of vertices
    private int adjMat[][];      // adjacency matrix
    private int nVerts;          // current number of vertices
    private queue thequeue;
    private stack theStack;
    private int[] parent;
    private static boolean printLabel;

//    public graphTraversal()  {                         // constructor
//	this(20, 40);
//    }

    public graphTraversalSCC(int V, int E)  {             // constructor
	MAX_VERTS = V;
	MAX_EDGES = E;

	vertexList = new vertexTraversal[MAX_VERTS];
	parent = new int[MAX_VERTS];
                                          // adjacency matrix
	adjMat = new int[MAX_VERTS][MAX_VERTS];

	nVerts = 0;
	for(int j=0; j<MAX_VERTS; j++)      // set adjacency
	    for(int k=0; k<MAX_VERTS; k++)   //    matrix to 0
		adjMat[j][k] = 0;
      
	for(int j=1; j<=MAX_VERTS; j++) {     // fill VertexList
	    vertexList[nVerts++] = new vertexTraversal("v"+j);
	}
	
	// ZHEN:
	// In order to set the number of edges.
	int edgeCounter = 0;
        Random randomGenerator = new Random();
        for (int i = 0;; i++) {
            int v = randomGenerator.nextInt(V);
            int w = randomGenerator.nextInt(V);
            if (adjMat[v][w] == 0) {
		    addEdge(v, w);
		    edgeCounter++;
	    }
	    if (edgeCounter == MAX_EDGES)
		    break;
        }

    }  // end constructor

    public void addvertex(String lab){
	vertexList[nVerts++] = new vertexTraversal(lab);
    }

    public void addEdge(int start, int end) {
	adjMat[start][end] = 1;
    }

   public String toString() {
	   if (nVerts == 0) return "G is empty";
	   String x = "G = (V, E), where\n  V = { " + vertexList[0].label;
	   for (int i = 1; i < nVerts; i++) x = x + ", " + vertexList[i].label;
	   x = x + " }\n  E = { ";
	   for (int i = 0; i < nVerts; i++)
		   for(int j=0; j<nVerts; j++)
	         if (adjMat[i][j]==1) 
	        	 x = x + "(" + vertexList[i].label + ", " + vertexList[j].label + ") ";
	   return x + "}";
   }
   
   public String displayTree() {
	   if (nVerts == 0) return "G is empty";
	   String x = "Search tree: ";
	   for (int i = 1; i < nVerts; i++) {
		   int j = parent[i];
	       x = x + "(" + vertexList[j].label + ", " + vertexList[i].label + ") ";
	   }
	   return x+"\n";   
   }
   
   public void displayvertex(int v) {
      if (printLabel) System.out.print(vertexList[v].label);
   }

   
    public void dfs(int a) { // depth-first search
	theStack = new stack();                             
	vertexList[a].wasVisited = true;  // begin at vertex 0 and mark it
	printLabel = true;
	displayvertex(a);                 // display it
	parent[a] = -1;                   // vertex 0 has no parent
	theStack.push(a);                 // push it
	while( !theStack.isEmpty() )  {    // until stack empty,
	    // get an unvisited vertex adjacent to stack top
	    int v = getAdjUnvisitedVertex( theStack.peek() );
	    if(v == -1)                    // if no such vertex,
		theStack.pop();
	    else {                          // if it exists,
		vertexList[v].wasVisited = true;  // mark it
		displayvertex(v);		// display it
		parent[v] = theStack.peek();
		theStack.push(v);                 // push it
	    }
	}  // end while

	// stack is empty, so we're done
    }  // end dfs

    public void SCC() {
	    // Transpose the graph
	    for(int i=0;i<MAX_VERTS-1;i++)
		    for(int j=i+1;j<MAX_VERTS;j++) {
			    if(adjMat[i][j]==0 && adjMat[j][i]==1) {
				    adjMat[i][j] = 1;
				    adjMat[j][i] = 0;
			    }
			    if(adjMat[i][j]==1 && adjMat[j][i]==0) {
				    adjMat[i][j] = 0;
				    adjMat[j][i] = 1;
			    }
		    }
	    System.out.println();

	    System.out.println("Strongly Connected Group:");
	    for(int i=0;i<MAX_VERTS;i++) {
		    if(!vertexList[i].wasVisited) {
			    dfs(i);
			    for(int j=0;j>MAX_VERTS;j++) {
				    if(vertexList[j].wasVisited && !vertexList[j].wasGrouped) {
					    displayvertex(j);
					    vertexList[j].wasGrouped = true;
				    }
			    }
			    System.out.println();
		    }
	    }
    }
 
    // returns an unvisited vertex adj to v
    public int getAdjUnvisitedVertex(int v) {
	for(int j=vertexList[v].nextNeighbor; j<nVerts; j++)
	    if(adjMat[v][j]==1 && vertexList[j].wasVisited==false) {
		vertexList[v].nextNeighbor = j+1;
		return j;
	    }
	vertexList[v].nextNeighbor = nVerts;
	return -1;
    }  // end getAdjUnvisitedvertex()

}  // end class graph

