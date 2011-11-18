package cpu;


public class ALU {

	public static final int AMR = 4;
	public static final int SMR = 5;
	public static final int IAR = 6;
	public static final int ISR = 7;
	public static final int SOB = 14; 
	public static final int MUL = 16;
	public static final int DIV = 17;
	public static final int TST = 18;
	public static final int AND = 19;
	public static final int OR = 20;
	public static final int NOT = 21;
	public static final int SRC = 25;
	public static final int RRC = 26;
	
	Register data1;
	Register data2;
	Register LR;  
	Register AL;
	Register COUNT;
	Register ARR;
	Register ARR2;
	Register CC;
	
	public ALU() {
		data1 = new Register(16);  //This is the first input for most ALU operations
		data2 = new Register(16);  //This is the second input for most ALU operations
		ARR = new Register(16);   //This is the primary output for most ALU operations
		ARR2 = new Register(16);  //This is a second output/result register that is only used for Multiply And Divide
		LR = new Register(1);  //LR Register determines LEFT or RIGHT direction of SHIFT/ROTATE instructions
		AL = new Register(1);  //AL Register determines Arithmetic or Logical nature of Shift/Rotate Instructions
		COUNT = new Register(4);  //Count Register is used in Shift/Rotate Instructions
		CC = new Register(4);  //Control Code Register
	}
	
	/**
	 * Executes the instructions processed in the ALU
	 * @param opcode
	 */
	public void execute(int opcode) {
		
		switch (opcode) {
	
		case (IAR):       //IAR and AMR both perform adds on the data inputs
		case (AMR):    
		    if (((data1.getSigned() + data2.getSigned()) >  32767) || ((data1.getSigned() + data2.getSigned()) <  -32768) )  {  
		        System.out.println("Arithmetic Overflow: Halting Processor");
		        CC.setBit(0, 1);  //Set CC(1) which corresponds to CC register bit 0 to value 1 to show OVERFLOW
		        break;
		      }		         
		    ARR.setSigned(data1.getSigned() + data2.getSigned());
			System.out.println("ALU: " + data1.getSigned() + " + " + data2.getSigned() + " = " + ARR.getSigned());
			break;
		
	    case (SOB):  //SOB ISR and SMR all perform subtracts of data2 from data1 within the ALU.
	    case (ISR): 
		case (SMR):     
		    if (((data1.getSigned() - data2.getSigned()) >  32767) || ((data1.getSigned() - data2.getSigned()) <  -32768) )  {  
		        System.out.println("Arithmetic Overflow: Halting Processor");
		        CC.setBit(0, 1);  //Set CC(1) which corresponds to CC register bit 0 to value 1 to show OVERFLOW
		        break;
		      }		         
    	    ARR.setSigned(data1.getSigned() - data2.getSigned());
		    System.out.println("ALU: " + data1.getSigned() + " - " + data2.getSigned() + " = " + ARR.getSigned());
		    break;
		
		case (MUL):
		    System.out.println("ALU MUL: " + data1.getSigned() + " * " + data2.getSigned());
		    int result = data1.getSigned() * data2.getSigned();
		    System.out.println("Result is " + result);
		        ARR.setSigned(result >> 16);   //Set ARR to Higher 16 bits by shifting the lower bits out of the number
		        ARR2.setSigned(result & 65535);  //Set ARR2 to lower 16 bits using bitwise AND to only keep lower 16
		        System.out.println("Signed High bits: " + ARR.getSigned() + " Unsigned Low bits: " + ARR2.get());
		    break;
		    
		case (DIV):
		    System.out.println("ALU DIV: " + data1.getSigned() + " / " + data2.getSigned());
		    if(data2.get() == 0) {
		        System.out.println("DIVIDE BY ZERO: Halting Processor");
		        CC.setBit(2, 1);  //Set CC(3) which corresponds to CC register bit 2 to value 1 to show DIVZERO
		        break;
		      }
		    ARR.setSigned(data1.getSigned() / data2.getSigned());
		    ARR2.set(data1.getSigned() % data2.getSigned());
		    System.out.println("DIV Quotient = " + ARR.getSigned() + " with a remainder of " + ARR2.get());
		    break;
		    
		case (TST):
		    if(data1.get() == data2.get()) { 
		       CC.setBit(3, 0);  //Bit 3 of CC corresponds to CC 4, EQUALORNOT
		     }
		    else {
		       CC.setBit(3, 1);  //Bit 3 of CC corresponds to CC 4, EQUALORNOT
		     }
		    break;

		case (AND):
		    ARR.set(data1.get() & data2.get());  //bitwise logical AND operation
		    System.out.println("ALU AND: of RX/Data1: " + data1.get() + " & " + data2.get() + " = " + ARR.get());
		    break;		
		
		case (OR):
		    ARR.set(data1.get() | data2.get());  //bitwise logical OR operation of 
		    System.out.println("ALU OR: of RX/Data1: " + data1.get() + " | " + data2.get() + " = " + ARR.get());
		    break;
			    
		case (NOT):
		    //System.out.println("ALU Executing NOT " + data1.getSigned());
		    ARR.setSigned(~(data1.getSigned()));  // Performing NOT on a number will mean it is off by one
		   // System.out.println("ALU Finished NOT. Signed:" + ARR.getSigned() + " Unsigned: " + ARR.get());
		    break;		
		    
		case (SRC):
		    if(LR.get() == 1){// L/R = 1, shift LEFT, Arith and logic are identical   
                System.out.println("LR.get() == 1, shifting left Arith or Logically");
		        ARR.setSigned((short)(data1.getSigned() << COUNT.get()));  // Zero bits are shifted into low
		       }
		    else {             //L/R = 0, shift RIGHT
			    if(AL.get() == 1) { //A/L = 1 shift RIGHT Logically
			      System.out.println("ALU: SRC AL.get() == 1, shifting " + data1.get() + " right logic by count: " + COUNT.get() + " Result will be " + (short)(data1.get() >>> (COUNT.get())));
			      ARR.setSigned((short)(data1.get()>>>(COUNT.get())));  //Zero bits are shifted into high
			     }             
			    else {           //A/L = 0, shift RIGHT Arithmetically
			      System.out.println("AL.get() != 1, shifting right arithmetically");  
			      ARR.setSigned((short)(data1.getSigned() >> (COUNT.get()))); //
			     }
		    }
	     // System.out.println("ALU Finished SRC " + ARR.get());
	      break;
	      
	    case (RRC):
	       System.out.println("ALU Executing RRC on " + data1.getSigned());
	       
	      
	       
	       short temp2 = (short)(data1.getSigned());
	       short temp3 = (short)(data1.getSigned()); //might need 
	       System.out.println("temp2 = " + temp2 + " temp3 " + temp3);
	       
	        if(LR.get() == 1){// L/R = 1, rotate LEFT
                if(AL.get() == 1) { //A/L = 1, rotate LEFT Logically
                   ARR.set((short)((temp2 << COUNT.get()) | (temp3 >> (16 - COUNT.get()))));
                System.out.println("LR.get() == 1, rotating left Logically");
                }
                else {              //AL = 0, rotate LEFT Arithmetically
                    ARR.set((temp2 << (short)(COUNT.get())) | (temp3 >>> (short)(16 - COUNT.get())));
                System.out.println("LR.get() == 1, rotating left Arithmetically");
                }
                   
		       }
		    else {             //L/R = 0, rotate RIGHT
			    if(AL.get() == 1) { //A/L = 1 shift RIGHT Logically
			    System.out.println("AL.get() == 1, rotating " + data1.getSigned() + " right logic by count: " + COUNT.get() + " Result will be " + (((short)(((short)temp2 << (16 - COUNT.get())))) | ((short)(temp3 >> (COUNT.get())))));
			     System.out.println("Temp2 << 15 - COUNT.get()" + (((short)((short)temp2 << (15 - COUNT.get())) + " Temp3 >> COUNT.get() " + (temp3 >> (COUNT.get())))));
			     // ARR.set((short)((short)(temp2 << (15 - COUNT.get())) | ((short)(temp3 >> (COUNT.get())))));  //Zero bits are shifted into high
			     ARR.setSigned((short)((short)temp2 << (16 - COUNT.get())) | (temp3 >> COUNT.get()));
			     }             
			    else {           //A/L = 0, rotate RIGHT Arithmetically
			      System.out.println("AL.get() != 1, rotating right arithmetically");  
			      ARR.set((temp2 << (16 - COUNT.get())) | (temp3 >>> (COUNT.get()))); //
			     }
		    }
	      System.out.println("ALU Finished RRC " + ARR.toString() + " value get " + ARR.get() + " value getSigned2 " + ARR.getSigned2());
	      	       
	      
		  break; 
       }
    }
	
    /**
	 * Return the contents of the ALU result register
	 * @return
	 */
	public int get() {
		return ARR.get();
	}
}



