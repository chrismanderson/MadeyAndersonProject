package cpu;

import java.util.*;

public class Memory {
	
	// stores the two related memory registers
	public Register MAR = new Register(CPU.DEFAULTSIZE);
	public Register MBR = new Register(CPU.DEFAULTSIZE);
	
	// memory is implemented as an array vector
	private int[] content;
	
	
	/**
	 * Initializes the memory bank to the desired size.
	 * @param size
	 */
	public Memory(int size) {
		content = new int[size];
	}
	
	/**
	 * Stores a value in memory.
	 * @param value
	 * @param address
	 */
	public void store(int address, int value) {
		content[address] = value;
	}
	
	/**
	 * Access a value in memory.
	 * @param l
	 * @return
	 */
	public int accessMemory(int l) {
		//System.out.println("Memory at location " + MAR.get() + " is " + content[MAR.getSigned()]);
		return content[l];
	}
	
	/**
	 * Sets the fault code in memory in the "1" block.
	 * @param faultCode
	 */
	public void setFault(int faultCode) {
		content[1] = faultCode;
	}

	
	
	
}
