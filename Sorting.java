import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Arrays;
import java.lang.Math;

public class Sorting {
    
    private static int[] arr;
    private static int[] arrCopy;
    private static int[] arrCopy2;
    private static BufferedReader read;
    private static Random randomGenerator;
   
    private static int size;
    private static int random;
    private static int instances;	// Number of instances
    private static int n;

    private static void printArray(String msg) {
    	System.out.print(msg + " [" + arr[0]);
        for(int i=1; i<size; i++) {
            System.out.print(", " + arr[i]);
        }
        System.out.println("]");
    }

    public static void insertSort(int left, int right) {
	// insertSort the subarray a[left, right]
	int i, j;

	for(i=left+1; i<=right; i++) {
	    int temp = arr[i];         // store a[i] in temp
	    j = i;                   // start shifts at i
	    // until one is smaller,
	    while(j>left && arr[j-1] >= temp) {
		arr[j] = arr[j-1]; // shift item to right
		--j;                      // go left one position
	    }
	    arr[j] = temp;          // insert stored item
	}  // end for
    }  // end insertSort()
    
    public static void insertionSort() {
    	insertSort(0, size-1);  
    } // end insertionSort()

    public static void buildheap(){
	// Build an in-place bottom up
        n=size-1;
        for(int i=n/2;i>=0;i--){
            heapify(i);
        }
    }
    
    public static void heapify(int i){ 
	// Assuming left and right subtrees are heaps.
	// Check if node i is the largest element.
        int largest;
        int left=2*i+1;
        int right=2*i+2;
        if(left <= n && arr[left] > arr[i]){
            largest=left;
        }
        else{
            largest=i;
        }
        
        if(right <= n && arr[right] > arr[largest]){
            largest=right;
        }
        if(largest!=i){
            exchange(i,largest);
            heapify(largest);
        }
    }
    
    public static void exchange(int i, int j){
        int t=arr[i];
        arr[i]=arr[j];
        arr[j]=t; 
    }

    // Get the median from three numbers for Problem 3
    private static int median(int i, int j, int k){
	    if((j<=k && j>=i) || (j<=i && j>=k)) return j;
	    else if((i<=j && i>=k) || (i<=k && i>=j)) return i;
	    else return k;
    }
 
    
    public static void heapsort(){
        buildheap();    
        for(int i=n;i>0;i--){
            exchange(0, i);
            n=n-1;
            heapify(0);
        }
    }

    // Boolean that checks if the array is sorted
    private static boolean isSorted(int low, int high){
	    for(int i=low;i<high;i++){
		    if(arr[i]>arr[i+1])
			    return false;
	    }
	    return true;
    }

    private static void mergesort(int low, int high) {
        // Check if low is smaller then high, if not then the array is sorted
        if (low < high) {
          // Get the index of the element which is in the middle
          int middle = low + (high - low) / 2;
          // Sort the left side of the array
          mergesort(low, middle);
          // Sort the right side of the array
          mergesort(middle + 1, high);
          // Combine them both
          merge(low, middle, high);
        }
      }


    // Merge sort with isSorted check and insertion for less than 100 elements
    private static void mergesortModified(int low, int high) {
        // Check if low is smaller then high, if not then the array is sorted
        if (low < high) {
		// Get the index of the element which is in the middle
		int middle = low + (high - low) / 2;
		if(high - low >= 200){		// if it's small then use insertion sort
						// This is 200 because this indicates that
						// low to middle is less than 100 and so as
						// middle to high.
          		// Sort the left side of the array
          		if(!isSorted(low, middle)){	// check to see if it's sorted
			mergesortModified(low, middle);
			}
          		// Sort the right side of the array
			if(!isSorted(middle+1, high)){
          		mergesortModified(middle + 1, high);
			}
		}
		else{
			insertSort(low, middle);	// If the elements are 
							// not too much, then
							// call insertion sort.
			insertSort(middle+1, high);
		}
          // Combine them both
          merge(low, middle, high);
        }
      }


    // Bottom up sort method with for loops
    private static void mergesortBU(int high) {
	    for(int i=1;i<size;i=i+i){
		    for(int j=0;j<size-i;j+=i+i) {
			   merge(j, j+i-1, Math.min(j+i+i-1, size-1));
		    }
	    }
    } 

    //

/*
 *  Zhen: Tried to make the Bottom up recursive by splitting the 
 *  group to 2^(height-1) and the rest. Cuz the core idea
 *  of this recursive way is still top down therefore this 
 *  is not tested in this code...
*/

    private static void merge(int low, int middle, int high) {

        // Copy both parts into the arrCopy array
        for (int i = low; i <= high; i++) {
          arrCopy2[i] = arr[i];
        }

        int i = low;
        int j = middle + 1;
        int k = low;

        // Copy the smallest values from either the left or the right side back
        // to the original array
        while (i <= middle && j <= high) {
          if (arrCopy2[i] <= arrCopy2[j]) {
            arr[k] = arrCopy2[i];
            i++;
          } else {
            arr[k] = arrCopy2[j];
            j++;
          }
          k++;
        }
	
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
          arr[k] = arrCopy2[i];
          k++;
          i++;
        }

      }
      
      private static void quicksort(int low, int high) {
	  int i = low, j = high;

	  // Get the pivot element from the middle of the list
	  int pivot = arr[(high+low)/2];

	  // Divide into two lists
	  while (i <= j) {
    	      // If the current value from the left list is smaller then the pivot
    	      // element then get the next element from the left list
    	      while (arr[i] < pivot) {
		  i++;
    	      }
    	      // If the current value from the right list is larger then the pivot
    	      // element then get the next element from the right list
    	      while (arr[j] > pivot) {
    	        j--;
    	      }

    	      // If we have found a value in the left list which is larger then
    	      // the pivot element and if we have found a value in the right list
    	      // which is smaller then the pivot element then we exchange the
    	      // values.
    	      // As we are done we can increase i and j
    	      if (i < j) {
    	        exchange(i, j);
    	        i++;
    	        j--;
    	      } else if (i == j) { i++; j--; }
    	    }

    	    // Recursion
    	    if (low < j)
    	      quicksort(low, j);
    	    if (i < high)
    	      quicksort(i, high);
    	  }
      
      // Quicksort modified :
      // 1. check if the array is sorted;
      // 2. check if the array is too small to 
      // implement quick sort. 
      private static void quicksortModified(int low, int high) {
	  int i = low, j = high;

	  // Get the pivot element from the middle of the list
	  int pivot = arr[(high+low)/2];

	  // Divide into two lists
	  while (i <= j) {
    	      // If the current value from the left list is smaller then the pivot
    	      // element then get the next element from the left list
    	      while (arr[i] < pivot) {
		  i++;
    	      }
    	      // If the current value from the right list is larger then the pivot
    	      // element then get the next element from the right list
    	      while (arr[j] > pivot) {
    	        j--;
    	      }

    	      // If we have found a value in the left list which is larger then
    	      // the pivot element and if we have found a value in the right list
    	      // which is smaller then the pivot element then we exchange the
    	      // values.
    	      // As we are done we can increase i and j
    	      if (i < j) {
    	        exchange(i, j);
    	        i++;
    	        j--;
    	      } else if (i == j) { i++; j--; }
    	    }
		
    	    // Recursion
    	    if (low < j){
		    if(!isSorted(low,j)){	// check if it's sorted
			    if(j - low <= 100)	// check if it's too small
				    insertSort(low, j);
			    else
			    	    quicksortModified(low, j);
		    }
	    }
    	    if (i < high){
		    if(!isSorted(i, high)){
			    if(high - i <= 100)
				    insertSort(i, high);
			    else
    	      			    quicksortModified(i, high);
		    }
	    }
    	  }


      // Quicksort modified for (a) using the median of three elements
      private static void quicksortModifiedA(int low, int high) {
	  int i = low, j = high;

	  // Get the pivot element from the middle of the list
	  int pivot = median(arr[i], arr[(high+low)/2], arr[j]);

	  // Divide into two lists
	  while (i <= j) {
    	      // If the current value from the left list is smaller then the pivot
    	      // element then get the next element from the left list
    	      while (arr[i] < pivot) {
		  i++;
    	      }
    	      // If the current value from the right list is larger then the pivot
    	      // element then get the next element from the right list
    	      while (arr[j] > pivot) {
    	        j--;
    	      }

    	      // If we have found a value in the left list which is larger then
    	      // the pivot element and if we have found a value in the right list
    	      // which is smaller then the pivot element then we exchange the
    	      // values.
    	      // As we are done we can increase i and j
    	      if (i < j) {
    	        exchange(i, j);
    	        i++;
    	        j--;
    	      } else if (i == j) { i++; j--; }
    	    }
		
    	    // Recursion
    	    if (low < j){
		    if(!isSorted(low,j)){	// check if it's sorted
			    if(j - low <= 100)	// check if it's too small
				    insertSort(low, j);
			    else
			    	    quicksortModifiedA(low, j);
		    }
	    }
    	    if (i < high){
		    if(!isSorted(i, high)){
			    if(high - i <= 100)
				    insertSort(i, high);
			    else
    	      			    quicksortModifiedA(i, high);
		    }
	    }
    	  }


      // Quicksort modified for (b) using the median of three medians
      // In order to utilize this, there must be at least 9 integers
      private static void quicksortModifiedB(int low, int high) {
	  int i = low, j = high;

	  // Get the pivot element from the middle of the list
	  int median1 = median(arr[(high+low)/2-1], arr[(high+low)/2], arr[(high+low)/2+1]);
	  int median2 = median(arr[(low+1)], arr[(low+2)], arr[(low+3)]);
	  int median3 = median(arr[(high-1)], arr[(high-2)], arr[(high-3)]);
	  int pivot = median(median1, median2, median3);

	  // Divide into two lists
	  while (i <= j) {
    	      // If the current value from the left list is smaller then the pivot
    	      // element then get the next element from the left list
    	      while (arr[i] < pivot) {
		  i++;
    	      }
    	      // If the current value from the right list is larger then the pivot
    	      // element then get the next element from the right list
    	      while (arr[j] > pivot) {
    	        j--;
    	      }

    	      // If we have found a value in the left list which is larger then
    	      // the pivot element and if we have found a value in the right list
    	      // which is smaller then the pivot element then we exchange the
    	      // values.
    	      // As we are done we can increase i and j
    	      if (i < j) {
    	        exchange(i, j);
    	        i++;
    	        j--;
    	      } else if (i == j) { i++; j--; }
    	    }
		
    	    // Recursion
    	    if (low < j){
		    if(!isSorted(low,j)){	// check if it's sorted
			    if(j - low <= 100)	// check if it's too small
				    insertSort(low, j);
			    else
			    	    quicksortModifiedB(low, j);
		    }
	    }
    	    if (i < high){
		    if(!isSorted(i, high)){
			    if(high - i <= 100)
				    insertSort(i, high);
			    else
    	      			    quicksortModifiedB(i, high);
		    }
	    }
    	  }


      public static void main(String[] args) {
        
        read = new BufferedReader(new InputStreamReader(System.in));
        
        randomGenerator = new Random();

	// Since the question always ask about 10 
	// instances, therefore I changed the code 
	// to make it ask for number of instances.
        
	try
	{
	
	System.out.print("Please enter instances : \n");
	instances = Integer.parseInt(read.readLine());
	System.out.print(instances + "\n");
        
	System.out.print("Please enter array size : \n");
        size = Integer.parseInt(read.readLine());
	System.out.print(size + "\n");
        
        System.out.print("Please enter the random range : \n");
        random = Integer.parseInt(read.readLine());
	System.out.print(random + "\n");

	for(int count=1; count<=instances; count++){

            // create array
            arr = new int[size];
            arrCopy = new int[size];
            arrCopy2 = new int[size];

            // fill array
            for(int i=0; i<size; i++) {
                arr[i] = arrCopy[i] = randomGenerator.nextInt(random);
            }
            if (size < 101) printArray("Initial array:");
            
            long start = System.currentTimeMillis();
            Arrays.sort(arr);
            if (size < 101) printArray("out");
            long finish = System.currentTimeMillis();
            System.out.println("Arrays.sort: " + (finish-start) + " milliseconds.");
	    
            // Heap sort      
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            heapsort();
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("heapsort: " + (finish-start) + " milliseconds.");
             
            // Merge sort
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            mergesort(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("mergesort: " + (finish-start) + " milliseconds.");

            // Merge sort - bottom up
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            mergesortBU(size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("mergesort bottom-up: " + (finish-start) + " milliseconds.");

            // Merge sort modified
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            mergesortModified(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("mergesort modified for problem 2: " + (finish-start) + " milliseconds.");

	    // Quick sort
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            quicksort(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("quicksort: " + (finish-start) + " milliseconds.");

	    // Quick sort modified
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            quicksortModified(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("quicksort modified for problem 2: " + (finish-start) + " milliseconds.");

	    // Quick sort modified using the median of three integers
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            quicksortModifiedA(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("quicksort modified for problem 3(a) : " + (finish-start) + " milliseconds.");

	    // Quick sort modified using the median of three medians
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            quicksortModifiedB(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("quicksort modified for problem 3(b) : " + (finish-start) + " milliseconds.");


	    // arr[0..size-1] is already sorted. We randomly swap 100 pairs to make it nearly-sorted.
	    for (int i = 0; i < 100; i++) {
		int j  = randomGenerator.nextInt(size);
		int k  = randomGenerator.nextInt(size);
		exchange(j, k);
	    }
	    for(int i=0; i<size; i++) arrCopy[i] = arr[i];

	    // Merge sort modified on nearly-sorted array
            start = finish;
            if (size < 101) printArray("in");
            mergesortModified(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("merge sort modified on nearly-sorted: " + (finish-start) + " milliseconds.");

	    // Quick sort on nearly-sorted array
	    for(int i=0;i<size;i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            quicksort(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("quicksort on nearly-sorted: " + (finish-start) + " milliseconds.");

	    // Quick sort modified on nearly-sorted array
	    for(int i=0;i<size;i++) arr[i] = arrCopy[i];
            start = finish;
            if (size < 101) printArray("in");
            quicksortModified(0, size-1);
            if (size < 101) printArray("out");
            finish = System.currentTimeMillis();
            System.out.println("quicksort modified on nearly-sorted: " + (finish-start) + " milliseconds.");

            // Insert sort on nearly-sorted array      
            for(int i=0; i<size; i++) arr[i] = arrCopy[i];
	    start = finish;
            if (size < 101) printArray("in");
	    insertionSort();
	    if (size < 101) printArray("out");
	    finish = System.currentTimeMillis();
	    System.out.println("insertsort on nearly-sorted: " + (finish-start) + " milliseconds.\n");

        }
	}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
