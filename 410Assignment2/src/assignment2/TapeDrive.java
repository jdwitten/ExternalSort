package assignment2;
import java.util.*;
/**
 * Simulates a tape drive
 */
public class TapeDrive {

    private int[] tape;
    private int currentPos = 0;

    public TapeDrive(int capacity) {
        tape = new int[capacity];
    }

    public void write(int i) {
        tape[currentPos] = i;
        currentPos = (currentPos + 1) % tape.length;
    }

    public int read() {
        int i = tape[currentPos];
        currentPos = (currentPos + 1) % tape.length;
        return i;
    }

    public void reset() {
        currentPos = 0;
    }

    /**
     * A helper function for debugging purpose. 
     * Inspect and print the tape data in the range of [0, tape.length)
     */
    public void printTape() {
        System.out.print("Tape data: ");
        for (int i=0; i<tape.length; ++i) {
            System.out.print(tape[i] + " ");
        }
        System.out.println();
    }

    /**
     * Create a new TapeDrive that can hold `capacity` numbers, fill it with random numbers, and return it.
     * The numbers must be random in the full integer range.
     */
    public static TapeDrive generateRandomTape(int capacity) {
        // TODO: Implement me for 10 points
    	TapeDrive tape_drive = new TapeDrive(capacity);
    	Random random = new java.util.Random();
    	for(int i=0; i< capacity; i++){
    		tape_drive.tape[i] = random.nextInt();;
    	}

        return tape_drive;
    }
}
