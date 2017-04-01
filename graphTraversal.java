import java.util.*;

class graphTraversal {
    private int MAX_VERTS;
    private int MAX_EDGES;
    private vertexTraversal vertexList[]; // list of vertices
    private int adjMat[][];      // adjacency matrix
    private int nVerts;          // current number of vertices
    private queue thequeue;
    private stack theStack;
    private int[] parent;
    private static boolean printLabel;

    // ZHEN:
    // Articulation points
    private boolean artpoint[];
    private int treeArray[];
    private int beta[];
    private int alpha[];

//    public graphTraversal()  {                         // constructor
//	this(20, 40);
//    }

    public graphTraversal(int V, int E)  {             // constructor
	MAX_VERTS = V;
	MAX_EDGES = E;

	vertexList = new vertexTraversal[MAX_VERTS];
	parent = new int[MAX_VERTS];
                                          // adjacency matrix
	adjMat = new int[MAX_VERTS][MAX_VERTS];

	artpoint = new boolean[MAX_VERTS];	// artpoint boolean array
	treeArray = new int[MAX_VERTS];
	beta = new int[MAX_VERTS];	// beta number for vertex
	alpha = new int[MAX_VERTS];

	nVerts = 0;
	for(int j=0; j<MAX_VERTS; j++)      // set adjacency
	    for(int k=0; k<MAX_VERTS; k++)   //    matrix to 0
		adjMat[j][k] = 0;
      
	for(int j=1; j<=MAX_VERTS; j++) {     // fill VertexList
	    vertexList[nVerts++] = new vertexTraversal("v"+j);
	}
	
	// ZHEN:
	// In order to set the max number of edges.
	// Though I changed the code to make sure 
	// I have the MAX_EDGES edges, I sometimes
	// still see the number of edges does not
	// match the MAX_EDGES.
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
	adjMat[end][start] = 1;   // undirected graph only
    }

   public String toString() {
	   if (nVerts == 0) return "G is empty";
	   String x = "G = (V, E), where\n  V = { " + vertexList[0].label;
	   for (int i = 1; i < nVerts; i++) x = x + ", " + vertexList[i].label;
	   x = x + " }\n  E = { ";
	   for (int i = 0; i < nVerts; i++)
		   for(int j=i+1; j<nVerts; j++)
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

   // ZHEN:
   // In the origin code given by professor,
   // if there is an isolated vertex,
   // the method will generate a edge between
   // the vertex and the root. When
   // finding the articulate point, I
   // ignored this edge because this edge
   // won't show up at the begining.
   // This explains the reason why sometimes
   // the search tree will have some edges
   // that don't exist at all. 
   
    public void dfs() { // depth-first search
	theStack = new stack();                             
	vertexList[0].wasVisited = true;  // begin at vertex 0 and mark it
	treeArray[0] = 0;		// the root
	beta[0] = 0;			// beta initialized as alpha
	alpha[0] = 0;
	printLabel = true;
	displayvertex(0);                 // display it
	parent[0] = -1;                   // vertex 0 has no parent
	theStack.push(0);                 // push it
	int counterArray = 1;	// fill in treeArray with 
				// vertex sequence
	    	  
	while( !theStack.isEmpty() )  {    // until stack empty,
	    // get an unvisited vertex adjacent to stack top
	    int v = getAdjUnvisitedVertex( theStack.peek() );
	    if(v == -1)                    // if no such vertex,
		theStack.pop();
	    else {                          // if it exists,
		vertexList[v].wasVisited = true;  // mark it
		displayvertex(v);		// display it
		treeArray[counterArray] = v;	
	        counterArray++;	
		parent[v] = theStack.peek();
		theStack.push(v);                 // push it
	    }
	}  // end while
	
//	System.out.println();
//	System.out.println("treeArray is: ");
	for(int i=0;i<MAX_VERTS;i++) {
//		displayvertex(treeArray[i]);
		alpha[treeArray[i]]=i;
		beta[treeArray[i]]=i;
//		System.out.print("\t"+treeArray[i]+"\t"+alpha[treeArray[i]]+"\t"+beta[treeArray[i]]+"\n");
	}
//	System.out.println();
	// stack is empty, so we're done
	for(int j=0; j<nVerts; j++)          // reset flags
	    vertexList[j].reset();
    }  // end dfs

    public void articulate() {

	    System.out.println();
	    // update beta with back edges
	    for(int i=MAX_VERTS-1;i>0;i--) {
		    for(int j=MAX_VERTS-1;j>=0;j--) {
			    if (treeArray[j]!=treeArray[parent[i]] && adjMat[treeArray[i]][treeArray[j]]==1 && beta[treeArray[i]]>beta[treeArray[j]]) {
				    int tempBeta = 0;
				    tempBeta = beta[treeArray[j]];
				    beta[treeArray[i]] = tempBeta;
			    }
		    }
	    }
	    // update beta with parents
	    for(int i=MAX_VERTS-1;i>0;i--) {
		    for(int j=i-1;j>=0;j--) {
			    if (treeArray[j]==parent[treeArray[i]] && beta[treeArray[j]]>beta[treeArray[i]]) {
				    int tempBeta = 0;
				    tempBeta = beta[treeArray[i]];
				    beta[treeArray[j]] = tempBeta;
			    }
		    }
	    }
	    // find articulate points
	    for(int i=MAX_VERTS-1;i>0;i--) {
		    for(int j=i-1;j>=0;j--) {
			    if (treeArray[j]==parent[treeArray[i]] && beta[treeArray[i]]>=alpha[treeArray[j]]) {
				    artpoint[treeArray[j]] = true;
			    }
			    else 
				    artpoint[treeArray[j]] = false;
		    }
	    }

	    // label articulate points
	    int countForRoot=0;
	    for(int i=MAX_VERTS-1;i>0;i--) {
		    if (parent[i] == 0 && adjMat[0][i]==1)
			    countForRoot++;
	    }
	    if (countForRoot > 1)
		    artpoint[0] = true;
	    else 
		    artpoint[0] = false;

	    System.out.println("Articulate points are: ");

	    int artpointZero=0;
	    for(int i=0;i<MAX_VERTS;i++) {
		    if(artpoint[i]) {
			    displayvertex(i);
			    artpointZero++;
		    }
	    }

	    if(artpointZero==0)
	    System.out.println("There is no Articulate point");
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

