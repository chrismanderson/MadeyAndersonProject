package cpu;

import java.util.Arrays;

public class Register {

	// this is the internal data representation for the register
	// we use an array for the whole register. 
	public int[] contents;
	public int maxSize;
	public int content;

	
	/**
	 * Constructor to generate a register to the desired number of bits.
	 * Array will be initialized to zero.
	 * 
	 */
	public Register(int size) {
		contents = new int[size];
		maxSize = size;
	}

	/**
	 * Get the entire register.
	 * @return
	 */
	public int[] getBits() {
		return contents;
	}
	
	public int get() {
		return content;
	}
	
	public int getSigned() {
	    if (content > 32767) {
	        return ((content - 32768) * -1);
	    }
	    else {
	        return content;
	       }
	}  
	    
	public int getSigned2() {
	    if (content > 32767) {
	        return((~(short)content)+1);
	       }
	     else {
	         return content;
	       }
	   }
	
	/**
	 * Gets an individual bit from the register.
	 * @param i
	 * @return
	 */
	public int getBit(int i) {
		return contents[i];
	}
	
	/**
	 * Gets a range of bits from the register.
	 * @param startLocation
	 * @param numOfBits
	 * @return
	 */
	public int[] getRangeOfBits(int startLocation, int numOfBits) {
		
		int[] rangeOfBits = new int[numOfBits];
		System.arraycopy(contents, startLocation, rangeOfBits, 0, numOfBits);
		return rangeOfBits;
	}
	
	/**
	 * Sets the entire register as int.
	 * @param _bits
	 */
	public void set(int _bits) {
		content = _bits;
		contents = CPU.getBinaryArrayFromInt(_bits, maxSize);
		
	}

	public void setSigned(int _bits) {
	    if (_bits < 0) {
	        content = (_bits * -1) + 32768;
	    }
	    else {
	        content = _bits;
	    }
	    contents = CPU.getBinaryArrayFromInt(content, maxSize);
	}  	
	
	/**
	 * Sets entire register as a bit array.
	 */
	public void setBits(int[] _bits) {
		contents = new int[maxSize];
		contents = _bits;
		content = CPU.getIntFromBinaryArray(contents);
	}
	
	/**
	 * Sets individual bits of register.
	 */
	public void setBit(int _bit, int i) {
		contents[i] = _bit;
		content = CPU.getIntFromBinaryArray(contents);
	}
	
	
	/**
	 * Sets a range of bits.
	 * @param _bits
	 * @param startLocation
	 */
	public void setRangeOfBits(int[] _bits, int startLocation) {
		int size = _bits.length;
		System.arraycopy(_bits, 0, contents, startLocation, size);
		content = CPU.getIntFromBinaryArray(_bits);
	}
	
	public String toString() {
		return Arrays.toString(contents);
	}
}
