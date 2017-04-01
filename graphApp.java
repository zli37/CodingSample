import java.util.Scanner;

public class graphApp {
		
    public static void main(String args[]) {

	    System.out.println("Please input two integers for V and E: ");
	    Scanner in = new Scanner(System.in);
	    int Vvalue = in.nextInt();
	    int Evalue = in.nextInt();
	graphTraversal theGraph = new graphTraversal(Vvalue, Evalue);

	System.out.println("Finding articulate points:----");

	System.out.println(theGraph.toString());
		    	
	System.out.print("\nDFS Visits: ");
	theGraph.dfs();             // depth-first search
	System.out.println("\n  "+theGraph.displayTree());
	theGraph.articulate();
	System.out.println();

	graphTraversalSCC theSCC = new graphTraversalSCC(Vvalue,Evalue);
	System.out.println();
	System.out.println("Finding SCC:----");
	System.out.println(theSCC.toString());
	theSCC.SCC();

		    
    }  // end main
		   
}  // end class GraphApp

