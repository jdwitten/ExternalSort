package assignment2;

/**
 * Represents a machine with limited memory that can sort tape drives.
 */
public class TapeSorter {

    private int memorySize;
    private int tapeSize;
    public int[] memory;

    public TapeSorter(int memorySize, int tapeSize) {
        this.memorySize = memorySize;
        this.tapeSize = tapeSize;
        this.memory = new int[memorySize];
    }

    /**
     * Sorts the first `size` items in memory via quicksort
     */
    public void quicksort(int size) {
        // TODO: Implement me for 10 points
    	quickSortPrivate(memory, 0, size);
    }
    private void quickSortPrivate(int[] arr, int left, int right){
		if(left>=right){return;}//base case
		
		int i=left;
		int j=right;
		int pivotIndex = (right+left)/2;  //pick the pivot index as the median
		
		//Move the pivot to the end of the array
		int tmp = arr[right];  
		arr[right] = arr[pivotIndex];
		arr[pivotIndex] = tmp;
		j--;
		
		//Swap values to the correct side of the pivot
		while(j>=i){
			if(arr[i]>=arr[right]&& arr[j]<arr[right]){
				int tmp2=arr[i];
				arr[i] = arr[j];
				arr[j] = tmp2;
			}
			if(arr[i]<arr[right]){i++;}
			if(arr[j]>=arr[right]){j--;}
		}
	
		//put the pivot element back in the correct location
		tmp = arr[right];
		arr[right] = arr[i];
		arr[i] = tmp;
		
		//Recursive calls for both halves of the pivot
		quickSortPrivate(arr, left, i-1);
		quickSortPrivate(arr, i+1, right);
	}

    /**
     * Reads in numbers from drive `in` into memory (a chunk), sorts it, then writes it out to a different drive.
     * It writes chunks alternatively to drives `out1` and `out2`.
     *
     * If there are not enough numbers left on drive `in` to fill memory, then it should read numbers until the end of
     * the drive is reached.
     *
     * Example 1: Tape size = 8, memory size = 2
     * ------------------------------------------
     *   BEFORE:
     * in: 4 7 8 6 1 3 5 7
     *
     *   AFTER:
     * out1: 4 7 1 3 _ _ _ _
     * out2: 6 8 5 7 _ _ _ _
     *
     *
     * Example 2: Tape size = 10, memory size = 3
     * ------------------------------------------
     *   BEFORE:
     * in: 6 3 8 9 3 1 0 7 3 5
     *
     *   AFTER:
     * out1: 3 6 8 0 3 7 _ _ _ _
     * out2: 1 3 9 5 _ _ _ _ _ _
     *
     *
     * Example 3: Tape size = 13, memory size = 4
     * ------------------------------------------
     *   BEFORE:
     * in: 6 3 8 9 3 1 0 7 3 5 9 2 4
     *
     *   AFTER:
     * out1: 3 6 8 9 2 3 5 9 _ _ _ _ _
     * out2: 0 1 3 7 4 _ _ _ _ _ _ _ _
     */
    public void initialPass(TapeDrive in, TapeDrive out1, TapeDrive out2) {
        // TODO: Implement me for 15 points!
    	int numberIn=0;
    	int numberOut=0;
    	int last_number=0;
    	while(numberIn < tapeSize){
    		//Read memorySize numbers from "in" into memory
    		for(int i=0; i<memorySize && numberIn<tapeSize; i++, last_number=i-1){
    			memory[i] = in.read();
    			numberIn++;
    		}
    		//Sorts them
    		quicksort(last_number);
    		//Writes these numbers back out to "out1"
    		for(int i=0; i<memorySize && numberOut<tapeSize; i++){
    			out1.write(memory[i]);
    			numberOut++;
    		}
    		//Checks if there are any numbers left to read
    		if(numberIn >= tapeSize){break;}
    		else{
    			//Read numbers into memory from "in"
    			for(int i=0; i<memorySize && numberIn<tapeSize; i++, last_number=i-1){
        			memory[i] = in.read();
        			numberIn++;
        		}
    			//Sorts these numbers
        		quicksort(last_number);
        		
        		//Writes them to "out2"
        		for(int i=0; i<memorySize && numberOut<tapeSize; i++){
        			out2.write(memory[i]);
        			numberOut++;
        		}
    		}
    	}
    }

    /**
     * Merges the first chunk on drives `in1` and `in2` and writes the sorted, merged data to drive `out`.
     * The size of the chunk on drive `in1` is `size1`.
     * The size of the chunk on drive `in2` is `size2`.
     *
     *          Example
     *       =============
     *
     *  (BEFORE)
     * in1:  [ ... 1 3 6 8 9 ... ]
     *             ^
     * in2:  [ ... 2 4 5 7 8 ... ]
     *             ^
     * out:  [ ... _ _ _ _ _ ... ]
     *             ^
     * size1: 4, size2: 4
     *
     *   (AFTER)
     * in1:  [ ... 1 3 6 8 9 ... ]
     *                     ^
     * in2:  [ ... 2 4 5 7 8 ... ]
     *                     ^
     * out:  [ ... 1 2 3 4 5 6 7 8 _ _ _ ... ]
     *                             ^
     */
    public void mergeChunks(TapeDrive in1, TapeDrive in2, TapeDrive out, int size1, int size2) {
        // TODO: Implement me for 10 points
    	int n1=0; int n2=0;
		int i=0;  int j=0;
		boolean firstloop = true;
		boolean drive1 = false;
		boolean drive2 = false;
		
		//Merge the first size1 or size2 numbers (whichever is smaller)
    	while(i<size1 && j<size2){
    		//If this is the first number read from both in1 and in2
    		if(firstloop){
    			n1 = in1.read();
    			n2 = in2.read();
    			firstloop= false;
    		}
    		//Read in the next number from the previous loop
    		if(drive1){n1=in1.read(); drive1=false;}
    		if(drive2){n2=in2.read(); drive2=false;}
    		
    		//Write the lower number to the out drive
    		if(n1<n2){out.write(n1); drive1=true; i++;}
    		
    		else{out.write(n2); drive2=true; j++;}
    	}
    	
    	//Write the remaining numbers from the longer drive
    	if(i<size1){
    		if(firstloop){n1 = in1.read();firstloop= false;}
    		while(i<size1){
    			out.write(n1);
    			i++;
    			if(i!= size1){n1 = in1.read();}
    		}
    	}
    	if(j<size2){
    		if(firstloop){n2 = in2.read();firstloop= false;}
    		while(j<size2){
    			out.write(n2);
    			j++;
    			if(j!= size2){n2 = in2.read();}
    		}
    	}		
    }

    /**
     * Merges chunks from drives `in1` and `in2` and writes the resulting merged chunks alternatively to drives `out1`
     * and `out2`.
     *
     * The `runNumber` argument denotes which run this is, where 0 is the first run.
     *
     * -- Math Help --
     * The chunk size on each drive prior to merging will be: memorySize * (2 ^ runNumber)
     * The number of full chunks on each drive is: floor(tapeSize / (chunk size * 2))
     *   Note: If the number of full chunks is 0, that means that there is a full chunk on drive `in1` and a partial
     *   chunk on drive `in2`.
     * The number of leftovers is: tapeSize - 2 * chunk size * number of full chunks
     *
     * To help you better understand what should be happening, here are some examples of corner cases (chunks are
     * denoted within curly braces {}):
     *
     * -- Even number of chunks --
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 } { 3 5 6 9 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 3 5 5 6 7 8 9 9 }
     *
     * -- Odd number of chunks --
     * in1 ->   { 1 3 5 } { 6 7 9 } { 3 4 8 }
     * in2 ->   { 2 4 6 } { 2 7 8 } { 0 3 9 }
     * out1 ->  { 1 2 3 4 5 6 } { 0 3 3 4 8 9 }
     * out2 ->  { 2 6 7 7 8 9 }
     *
     * -- Number of leftovers <= the chunk size --
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 5 7 8 9 }
     *
     * -- Number of leftovers > the chunk size --
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 } { 3 5 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 3 5 5 7 8 9 }
     *
     * -- Number of chunks is 0 --
     * in1 ->   { 2 4 5 8 9 }
     * in2 ->   { 1 5 7 }
     * out1 ->  { 1 2 4 5 5 7 8 9 }
     * out2 ->
     */
    public void doRun(TapeDrive in1, TapeDrive in2, TapeDrive out1, TapeDrive out2, int runNumber) {
        // TODO: Implement me for 15 points
    	//Reset all of the tapes
    	in1.reset();
    	in2.reset();
    	out1.reset();
    	out2.reset();
    	
    	// Determine chunkSize, fullChunks and leftovers
    	int chunkSize = memorySize * ((int)Math.pow(2,runNumber));
    	int fullChunks = (int) Math.floor(tapeSize / (chunkSize * 2));
    	int leftovers = tapeSize - (2 * chunkSize * fullChunks);
    	
    	//Merge the leftovers from an incomplete chunk
    	if(fullChunks==0){
    		leftovers = tapeSize-chunkSize;
    		mergeChunks(in1, in2, out1, chunkSize, leftovers);
    		return;
    	}
    	//Merge all of the full chunks in each drive and alternate the out tape
    	int tracker=fullChunks;
    	while(tracker>0){
    		mergeChunks(in1, in2, out1, chunkSize, chunkSize);
    		tracker--;
    		
    		//Write the leftovers if there are no more full chunks
    		if(tracker<=0){
    			if(leftovers<=chunkSize && leftovers!=0){
    				mergeChunks(in1, in2, out2, leftovers, 0);
    				return;
    			}
    			if(leftovers>chunkSize && leftovers!=0){
    				mergeChunks(in1, in2, out2, chunkSize, leftovers-chunkSize);
    				return;
    			}
    			else{return;}
    		}
    		mergeChunks(in1, in2, out2, chunkSize, chunkSize);
   			tracker--;
    	}
    	//Write the leftovers if there are no more full chunks
    	if(leftovers<=chunkSize && leftovers!=0){
			mergeChunks(in1, in2, out1, leftovers, 0);
			return;
		}
		if(leftovers>chunkSize && leftovers!=0){
			mergeChunks(in1, in2, out1, chunkSize, leftovers-chunkSize);
			return;
		}
    }

    /**
     * Sorts the data on drive `t1` using the external sort algorithm. The sorted data should end up on drive `t1`.
     *
     * Initially, drive `t1` is filled to capacity with unsorted numbers.
     * Drives `t2`, `t3`, and `t4` are empty and are to be used in the sorting process.
     */
    public void sort(TapeDrive t1, TapeDrive t2, TapeDrive t3, TapeDrive t4) {
        // TODO: Implement me for 15 points
    	//reset all of the tapes
    	t1.reset();
    	t2.reset();
    	t3.reset();
    	t4.reset();
    	
    	//Determine the number of runs needed to sort 
    	int tmp = memorySize;
    	int numberOfRuns = 0;
    	while(tmp<tapeSize){
    		tmp = 2*tmp;
    		numberOfRuns ++;
    	}
    	
    	//Run the initial pass by reading in numbers from t1 to t2 and t3
    	initialPass(t1, t2, t3);
    	
    	//Do the rest of the runs, alternating input and output tapes
    	int counter=0;
    	while(counter<numberOfRuns){
    		doRun(t2, t3, t1, t4, counter);
    		counter++;
    		if(counter>= numberOfRuns){return;}
    		doRun(t1, t4, t2, t3, counter);
    		counter++;
    	}
    	
    	//if t2 was the last output tape then rewrite it back to t1
    	t1.reset();
    	t2.reset();
    	for(int i=0; i<tapeSize; i++){
    		t1.write(t2.read());
    	}
    	return;
    }

    public static void main(String[] args) {
        // Example of how to test
 
        TapeSorter tapeSorter = new TapeSorter(100, 80);
        TapeDrive t1 = TapeDrive.generateRandomTape(80);
        TapeDrive t2 = new TapeDrive(80);
        TapeDrive t3 = new TapeDrive(80);
        TapeDrive t4 = new TapeDrive(80);
        tapeSorter.sort(t1, t2, t3, t4);
        int last = Integer.MIN_VALUE;
        boolean sorted = true;
        for (int i = 0; i < 80; i++) {
            int val = t1.read();
            sorted &= last <= val; // <=> sorted = sorted && (last <= val);
            last = val;
        }
        if (sorted)
            System.out.println("Sorted!");
        else
            System.out.println("Not sorted!");
        t1.printTape();
    }
}
