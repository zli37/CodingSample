class stack {
   private final int SIZE = 100;
   public int[] st;
   private int top;

   public stack()           // constructor
      {
      st = new int[SIZE];    // make array
      top = -1;
      }

   public void reset() {     // clear the stack
	   top = -1;
   }
   
   public void push(int j)   // put item on stack
      { st[++top] = j; }

   public int pop()          // take item off stack
      { return st[top--]; }

   public int peek()         // peek at top of stack
      { return st[top]; }

   public boolean isEmpty()  // true if nothing on stack
      { return (top == -1); }
   
   public int size() // return the number of items in the stack
      { return (top+1); }

   }  // end class stack

