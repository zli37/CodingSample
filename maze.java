import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class maze {

    private static final int right = 0;
    private static final int down = 1;
    private static final int left = 2;
    private static final int up = 3;
    
    public static int Size;    
    
    public static class Point {  // a Point is a position in the maze

        public int x, y;
        public boolean visited;   // for DFS
	public Point parent;      // for DFS

        public Point(int x, int y) {
            this.x = x;
	    this.y = y;
        }

        public void copy(Point p) {
            this.x = p.x;
            this.y = p.y;
        }
    }
    
    public static class Edge { 
	// an Edge is a link between two Points: 
	// For the grid graph, an edge can be represented by a point and a direction.
	Point point;
	int direction;
	boolean used;     // for maze creation
	boolean deleted;  // for maze creation
	
	public Edge(Point p, int d) {
            this.point = p;
	    this.direction = d;	    
	    this.used = false;
	    this.deleted = false;
        }
    }

    // A board is an SizexSize array whose values are Points                                                                                                           
    public static Point[][] board;
    
    // A graph is simply a set of edges: graph[i][d] is the edge 
    // where i is the index for a Point and d is the direction 
    public static Edge[][] graph;
    public static int N;   // number of points in the graph
    
    public static void displayInitBoard() {
        System.out.println("\nInitial Configuration:");

        for (int i = 0; i < Size; ++i) {
            System.out.print("    -");
            for (int j = 0; j < Size; ++j) System.out.print("----");
            System.out.println();
            if (i == 0) System.out.print("Start");
            else System.out.print("    |");
            for (int j = 0; j < Size; ++j) {
                if (i == Size-1 && j == Size-1)
		    System.out.print("    End");
                else System.out.print("   |");
            }
            System.out.println();
        }
        System.out.print("    -");
        for (int j = 0; j < Size; ++j) System.out.print("----");
        System.out.println();
    }
    
    // If the edge is deleted then no edge will be ploted.
    // If it is the path then filled with ~~~
    private static void displayFinalBoard() {
        System.out.println("\nFinal Configuration:");

        for (int i = 0; i < Size; ++i) {
            System.out.print("    -");
            for (int j = 0; j < Size; ++j) {
		    if (i == 0 || !graph[i*Size+j][up].deleted)
		    	    System.out.print("----");
		    else
			    System.out.print("   -");
	    }
            System.out.println();
            if (i == 0) 
		    System.out.print("Start");
            else
		    System.out.print("    |");
            for (int j = 0; j < Size; ++j) {
                if (i == Size-1 && j == Size-1)
		        System.out.print(" O  End");
                else if (j == Size - 1 || !graph[i*Size+j][right].deleted) {
			if (board[i][j].visited)
				System.out.print(" O |");
			else
				System.out.print("   |");
		}
		else {
			if (board[i][j].visited)
				System.out.print(" O  ");
			else
				System.out.print("    ");
		}
            }
            System.out.println();
        }
        System.out.print("    -");
        for (int j = 0; j < Size; ++j) System.out.print("----");
        System.out.println();
    }
    // Function for rank
    private static int rank(Point a) {
	    if (a.parent == a)
		    return 0;
	    else {
		    return rank(a.parent) + 1;
	    }
    }

    // Function for union
    private static void r_Union(Point a, Point b) {
	    int ra = rank(a);
	    int rb = rank(b);
	    if (ra > rb) 
		    b.parent = a;
	    else
		    a.parent = b;
    }

    // Function for find
    private static Point Find(Point a) {
	    if (a.parent == a)
		    return a;
	    else {
		    return Find(a.parent);
	    }
    }

    // Function for pc_find with Point b
    // being the root of Point a. Int r
    // is the rank of Point a.
    public static void pc_Find(Point a, Point b, int r) {
	    if (r == 0 || r == 1)
		    return;
	    else {
		    for(int i = r; i >= 2; i--) {
			    Point temp = a.parent;
			    a.parent = b;
			    a = temp;
		    }
	    }
    }
    
    // Function to check if the maze is done
    private static boolean oneSet() {
	    int count = 0;
	    Point a = Find(board[0][0]);
	    for (int i = 0; i < Size; i++)
		    for (int j = 0; j < Size; j++) {
			    if (Find(board[i][j]) != a)
				    return false;
		    }
	    return true;
    }

    // Function to generate random number
    public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
    }

    // Solve the maze recursively
    private static boolean solve (int x, int y, int k) {
	    
	    boolean done = false;

	    for (int i = 3; i >= 0 && !done; i--) 
		    if (i != k)
			    switch (i) {
				    case 0:
					    if (graph[x*Size+y][0].deleted)
						    done = solve(x, y+1, 2);
					    break;
				    case 1:
					    if (graph[x*Size+y][1].deleted)
						    done = solve(x+1, y, 3);
					    break;
				    case 2:
					    if (graph[x*Size+y][2].deleted)
						    done = solve(x, y-1, 0);
					    break;
				    case 3:
					    if (graph[x*Size+y][3].deleted)
						    done = solve(x-1, y, 1);
					    break;
			    }
	    if (x == 0 && y == 0)
		    done = true;
	    if (done) {
		    board[x][y].visited = true;
		    switch (k) {
			    case 0:
				    board[x][y+1].visited = true;
				    break;
			    case 1:
				    board[x+1][y].visited = true;
				    break;
			    case 2:
				    board[x][y-1].visited = true;
				    break;
			    case 3:
				    board[x-1][y].visited = true;
				    break;
		    }
	    }
	    return done;
    }

    private static void solveMaze() {
	    solve(Size - 1, Size - 1, -1);
    }






    public static void main(String[] args) {
         
	Scanner scan = new Scanner(System.in);
         
	try {
	     
	    System.out.println("What's the size of your maze? ");
	    Size = scan.nextInt();
         
	    Edge dummy = new Edge(new Point(0, 0), 0);
	    dummy.used = true;
	    dummy.point.visited = true;
	    				
	    board = new Point[Size][Size];
	    N = Size*Size;  // number of points
	    graph = new Edge[N][4];         
	     
	    for (int i = 0; i < Size; ++i) 
		for (int j = 0; j < Size; ++j) {
		    Point p = new Point(i, j);
		    int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j
		     
		    board[i][j] = p;
		     
		    graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
		    graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;        
		    graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;         
		    graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;
			
		    p.parent = p;		// At the beginning, every element 
		    				// has itself as its parent.
		    for (int k = 0; k < 4; k++) {
			    if (graph[pindex][k] != dummy)
				    graph[pindex][k].used = false;
		    }
		}

		    while (!oneSet()) {
			    int i = randInt(0, Size-1);
			    int j = randInt(0, Size-1);
			    int d = randInt(0, 3);
			    if(graph[i*Size+j][d].used || graph[i*Size+j][d].deleted)
				    continue;
			    else if (graph[i*Size+j][d] == dummy)
				    continue;
			    else if (d == 0 && Find(board[i][j]) == Find(board[i][j+1])) {
				    graph[i*Size+j][d].used = true;
				    continue;
			    }
			    else if (d == 1 && Find(board[i][j]) == Find(board[i+1][j])) {
				    graph[i*Size+j][d].used = true;
				    continue;
			    }
			    else if (d == 2 && Find(board[i][j]) == Find(board[i][j-1])) {
				    graph[i*Size+j][d].used = true;
				    continue;
			    }
			    else if (d == 3 && Find(board[i][j]) == Find(board[i-1][j])) {
				    graph[i*Size+j][d].used = true;
				    continue;
			    }
			    else {
				    if (d == 0)
					    r_Union(Find(board[i][j]), Find(board[i][j+1]));
				    else if (d == 1)
					    r_Union(Find(board[i][j]), Find(board[i+1][j]));
				    else if (d == 2)
					    r_Union(Find(board[i][j]), Find(board[i][j-1]));
				    else
					    r_Union(Find(board[i][j]), Find(board[i-1][j]));
			    }
			    graph[i*Size+j][d].deleted = true;
			    
			    // Do a path compression after union to 
			    // speed up the next Find steps.
			    pc_Find(board[i][j],Find(board[i][j]),rank(board[i][j]));
		    }

		    // Initialize booleans.
		    for(int i = 0; i < Size; i++)
			    for (int j = 0; j < Size; j++) {
				    board[i][j].visited = false;
			    }
		    solveMaze();

	    displayInitBoard();
	    displayFinalBoard();
         
	}
	catch(Exception ex){
	    ex.printStackTrace();
	}
	scan.close();
    }    
}

