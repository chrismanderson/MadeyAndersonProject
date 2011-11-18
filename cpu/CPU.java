package cpu;

import ui.CPUInterface;
import cpu.Instruction;


/** 
 * Class Test
 * @author Chris Anderson
 * @version .1
 * This class implements the UI and the overall schematic of the virtual CPU.
 * 
 */




public class CPU {

    public static int DEFAULTSIZE = 16;
    public static final int HLT = 0;
    
    // Load Store Instructions:
    public static final int LDR = 1;    // LDR r, x, address[,I] //load register from memory
    public static final int STR = 2;    // STR r, x, address[,I] //store register to memory
    public static final int LDA = 3;

    public static final int LDX = 33;    //Original values were in octal, 041 OCTAL = 33 Dec 
    public static final int STX = 34;
    
    // Transfer Instructions:
    public static final int JZ = 8;
    public static final int JNE = 9;
    public static final int JCC = 10;
    public static final int JMP = 11;
    public static final int JSR = 12;
    public static final int RFS = 13;
    public static final int SOB = 14;
    
    // Arithmetic and Logical Instructions:
    public static final int AMR = 4;
    public static final int SMR = 5;
    public static final int IAR = 6;
    public static final int ISR = 7;    
    public static final int MUL = 16;
    public static final int DIV = 17;
    public static final int TST = 18;
    public static final int AND = 19;
    public static final int OR = 20;
    public static final int NOT = 21;
    
    //Shift/Rotate Operations:
    public static final int SRC = 25; //Why do CompSci students confuse Halloween with Christmas?  OCT 31 = DEC 25
    public static final int RRC = 26; //You don't get many opportunities to tell that joke.

    
    private ProgramCounter programCounter;
    private Memory mem;
    public Register PC, IND, IX, XO, ADDR, OP, RSR, HL, RX, RY, MSR;
    public InstructionRegister IR;
    public RegisterFile RF;
    public Instruction currentInstruction;
    public ALU alu;
    
    
    public CPU() {
        // Sets up all of the registers.
        Instruction currentInstruction = new Instruction();

        // program counter registers
        PC = new Register(DEFAULTSIZE);

        // opcode register
        OP = new Register(6);
        
        // instruction register
        IR = new InstructionRegister(DEFAULTSIZE);
        
        // indirect register and indexing registers
        IND = new Register(1);
        IX = new Register(1);
        
        // Register select register
        RSR = new Register(2);
        
        // halt register
        HL = new Register(1);
        
        // memory related registers and memory itself
        mem = new Memory(2048);

        XO = new Register(DEFAULTSIZE);
        
        // register file
        RF = new RegisterFile(4);
        
        // Address register
        ADDR = new Register(6);
        
        // Arithmetic logic unit
        alu = new ALU();
        
        //Additional registers for Arithmetic and Logical Instructions
        RX = new Register(2);
        RY = new Register(2); 
        
        //MSR Register
        MSR = new Register(2);
               
    }
    
    public void runCPU(int memAddress) {
        // need timing code
         
        // load the memory with the instructions
        mem.store(3, 1178);  //LDR R2 c[Memory 26] Initialize R2 with 19   
        
     //Begin LOOP
        
        mem.store(4, 1629);  //LDR I R1 [Memory 29] Set Register 1 to the NEXTVALUE
        mem.store(5, 5214);  //SMR R1 [Memory 30]  Subtract User Input from NEXTVALUE/IntegerX 
        mem.store(6, 2139);  //STR R1 [Memory 27] Store result in a TMP (Part 1 of Register Copy) 
        mem.store(7, 1051);  //LDR R0 [Memory 27] Load Register Zero with TMP (Part 2 of reg copy)
        mem.store(8, 25679); //SRC Right R0 Logic, 15; Logically shift register zero righ by 15 places 
                             // Shifting register right 15 places means the last digit is a 1 if negative and a 0 if zero or positive
        mem.store(9, 8204);  //JZ R0 [Instruction 12], Jump to instruction if Register zero is equal to 0,
        mem.store(10, 21760); //NOT R1, Difference is negative, need absolute value.  Step 1 = Complement value
        mem.store(11, 6209); //IAR R1 + 1, Add 1 to R1 because performing NOT on 2s complement -21 would give you 20.  We want 21.
        mem.store(12, 2139); //STR R1, [Memory 27] Store result in TMP (Part 1 of Register Copy)
        mem.store(13, 1051); //LDR R0, [Memory 27] Load TMP into Register Zero (Part 2 of Register Copy)        
        mem.store(14, 5148); //SMR R0, [Small Diff], Subtract Smallest Difference from Current Difference, if Negative, Current diff smaller
        mem.store(15, 25679); //SRC Right R0 Logic, 15; Logically shift register zero right by 15 places to set up jump (next)
        mem.store(16, 8211); //JZ R0 [Instruction 19] Jump to instruction 19 if Register Zero equals 0
        mem.store(17, 2140); //STR R1, [Memory 28], Store new value in Smallest Difference
        mem.store(18, 1245); //LDR R3 [Memory 29], Load R3 with Closest Value
        mem.store(19, 1117); //LDA R1 {Memory 29], Load R1 with pointer to Current IntX, Part 1 of iterating the Memory Pointer for next loop
        mem.store(20, 6209); //IAR R1 +1, increment Current IntX location to the next value, Part 2 of iterating the Next Value Pointer for next loop  
        mem.store(21, 2141); //STR R1 [29], Store the next IntX back to [memory 29]
        mem.store(22, 14468); //SOB R2, x, [4], Decrement R2 and jump to [instruction 4]
        
     //Loop complete, program finished, set up registers with final results
        
        mem.store(23, 2267);  //STR R3 [Mem 27], Stores the pointer to ClosestValue (Part 1/2)
        mem.store(24, 1627);  //LDR I R1 [Mem 27], Loads value of ClosestValue into R1 (Part 2/2)
        mem.store(25, 1054);  //LDR R0 [Mem 30], Loads value of UserInput into R0
        
     // load memory with data
     
        mem.store(26, 20);  //Used to initialize the loop counter, Loaded to R2 with first instruction  
        mem.store(27, 0);  //TMP variable for copying registers and whatnot
        mem.store(28, 32767); //SmallestDifference variable keeps track of difference between CLOSEST and NEXTVALUE, initialized large so it gets replaced on the first try 
        mem.store(29, 31);  //This is where we keep track of the NEXTVALUE
        mem.store(30, 4416);  //This is the user input
        mem.store(31, 2342);  //First value of 20
        mem.store(32, 465);
        mem.store(33, 2052);
        mem.store(34, 5016);
        mem.store(35, 6330);
        mem.store(36, 7707);
        mem.store(37, 7874);
        mem.store(38, 7933);
        mem.store(39, 8752);        //These are all values 1..20
        mem.store(40, 9328);        //They are only placed in order to make it easy to see what the answer should be
        mem.store(41, 12833);       //I also tested them out of order. 
        mem.store(42, 13157);
        mem.store(43, 14392);
        mem.store(44, 17313);
        mem.store(45, 19430);
        mem.store(46, 22336);
        mem.store(47, 23869);
        mem.store(48, 26788);
        mem.store(49, 31304);
        mem.store(50, 32605);  //20th Value
      
        
       // ---------------------DEBUGGING CODE:------------------------------
       //  /*
        //Multiply and DIVIDE tests: 
        //Mult Opcode = 16384 + RX 00 +0 or 10 +512  + RY 00 +0 or 10 +64  XXXXXX,
       // mem.store(
      //  mem.store(, 16384); // 0 0, doesn't halt
      // mem.store(3, 1033);  //LDR R0 [Memory 9]   //This Instruction loads R0 for MUL/DIV
       // mem.store(4, 1162);  //LDR R2 c[Memory 10] //This instruction loads R2 for MUL/DIV
        
       // mem.store(5, 16896); // MUL RX2 RY0, 
       // mem.store(6, 16512); // MUL RX0 RY2
       //Div Opcode = 17408 + RX 00 +0 or 10 +512  + RY 00 +0 or 10 +128  XXXXXX,
       // mem.store(5, 17536); // DIV RX0, RY2
      //  mem.store(6, 17920); //Div RX2, RY0
        
      // mem.store(9, 225);  //Data values for MUL/DIV
      //  mem.store(10, 55)      
       //    //RX RY tests
      //  mem.store(3, 17152); // Fails RX = 3, verified   
      //  mem.store(4, 16576); // Fails RY = 3, verified
      //  mem.store(5, 16640); // Fails RX = 1, verified
      //  mem.store(6, 16448); // Fails RY = 1, verified
       
      //RRC Tests, RRC Opcode: +26624, LR: L+1024 R: 3+768 2+512 1+256  AL: Logic+64 XX COUNT: 12-15
          
       //   mem.store(4, 26691); // RRC Right Reg0 Logical Count:1 , Verified 225 becomes 112
          
        
       // sets the program start address
        PC.set(memAddress);

        // fetch the given instructions
        while (HL.get() != 1) {
            runInstructions(); 
        }
        System.out.println("Program finished, R0:" + RF.getSigned(0) + " R1:" + RF.getSigned(1) + " R2:" + RF.getSigned(2) + " R3:" + RF.getSigned(3));
        System.out.println("R0 should be User Input, R1 should be closest value, R2 is the now useless last shift value, R3 is Address of Closest Value");
        
//      runInstruction();
        
    }
    
    public void runInstructions() {
        
        // fetches the instruction
        fetchInstruction();
        
        // decodes the instruction
        decodeInstruction();
        
        // processes the instruction
        execute(OP.get());
        
        // sets halt register
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void fetchInstruction() {
        
        System.out.print("PC: " + PC.get() + " ");  // PC counter is 
        
        // gets the current instruction from memory
        mem.MAR.set(PC.get());
        mem.MBR.set(mem.accessMemory(mem.MAR.get()));
        
        // sets the instruction register
        // System.out.println("MBR is: " + mem.MBR.get());
        IR.set(mem.MBR.get());
    }
    
    /**
     * Decodes the instruction register
     * @param IR
     */
    public void decodeInstruction() {
        //The Decode is a case statement for three different instruction types
        //System.out.println("Decoding!");
        OP.setBits(IR.getRangeOfBits(0, 6));
        //System.out.println(OP.get());
        switch (OP.get()) {   
             case(1):  //LDR   OPCODE[0-5], I[6], IX[7], AC[8,9], ADDRESS[10-15]
             case(2):  //STR
             case(3):  //LDA
             case(4):  //AMR
             case(5):  //SMR
             case(6):  //IAR
             case(7):  //ISR             
             case(33): //LDX
             case(34): //STX
             case(8):  //JZ
             case(9):  //JNE
             case(10): //JCC
             case(11): //JMP
             case(12): //JSR
             case(13): //RFS
             case(14): //SOB     
                         
                IND.setBits(IR.getRangeOfBits(6, 1));
                IX.setBits(IR.getRangeOfBits(7, 1));
                RSR.setBits(IR.getRangeOfBits(8, 2));
                // stores address.
                ADDR.setBits(IR.getRangeOfBits(10, 6));
                System.out.println("Load/Store/Transfer OP:" + OP.get() + " IND:" + IND.get() + " IX:" + IX.get() + " R" + RSR.get() + " ADDR:" + ADDR.get());
                break;
                
             case(16): //MUL  OPCODE[0-5], Rx[6,7], Ry[8,9], XXXXX[10-15]
             case(17): //DIV
             case(18): //TST
             case(19): //AND
             case(20): //OR
             case(21): //NOT
                 
                RX.setBits(IR.getRangeOfBits(6, 2));
                RY.setBits(IR.getRangeOfBits(8, 2));
                  if((OP.get() == 16) || (OP.get() == 17)) {  //16 (MUL) and 17 (DIV) must use R0 or R2 for RX and RY
                    if((RX.get()) != 0  && (RX.get() != 2)) {
                       HL.set(1);
                       System.out.println("Setting HALT 1: RX is " + RX.get());
                       mem.setFault(2);
                    }                             
                
                    if((RY.get() != 0)  && (RY.get() != 2)) {
                        HL.set(1);
                        System.out.println("Setting HALT 1: RY is " + RY.get());
                        mem.setFault(2);
                    } 
                  }
                System.out.println("Arith/Logic OP:" + OP.get() + " RX:" + RX.get() + " RY:" + RY.get() + " XXXXX ");
                break;
             
             case(25):  //SRC  OPCODE[0-5], LR[6], RSR[7,8], AL[9], XX[10,11], COUNT[12-15]
             case(26):  //RRC  
                
                 alu.LR.setBits(IR.getRangeOfBits(6, 1));
                 RSR.setBits(IR.getRangeOfBits(7, 2));  //I reuse the RSR register since it serves the same function
                 alu.AL.setBits(IR.getRangeOfBits(9, 1));
                 alu.COUNT.setBits(IR.getRangeOfBits(12, 4));
                 System.out.println("Shift/Rotate: OP" + OP.get() + " LR " + alu.LR.get() + " R" + RSR.get() + " AL " + alu.AL.get() + " XX Count:" + alu.COUNT.get() );
                 break; 
                 
             default: 
                 // Invalid opcode, halts the processor.
                 System.out.println("OPCODE is HLT or unrecognized: halt processor.");
                 HL.set(1);
                 mem.setFault(2);
                 break;                
             
            }   
             
    }
    /**
     * Converts a binary array to an integer
     * @param binary
     * @return
     */
    public static int getIntFromBinaryArray(int[] binary) {

        int value = 0;
        
               
            
  
           for (int i = 0; i <= binary.length - 1; i++) {  //We have a positive integer
               value = value * 2;
               value += binary[i];
            }
           if (binary.length == 16 && binary[0] == 1) {  //We have a negative Integer
               value = (value - 32768) * -1;
            }
         
        return value;
    }
    
    /**
     * Converts an integer to a binary string.
     * Adapted from http://www.asiplease.net/computing/delphi/programs/denary_binary.htm
     * @param value
     * @return
     */
    public static int[] getBinaryArrayFromInt(int value, int length) {
        
        int[] binaryArray = new int[length];
        int current = length - 1;
    
        while (value != 0) {
            binaryArray[current] = value % 2;
            value = value / 2;
            current--;
        }

        return binaryArray;
    }
    /**
     * Calculates the effective address of a location in memory.
     * Accounts for the presence of index/indirect bits.
     * @return effective address as an int.
     */
    public int calculateEA() {
        
        // indexing and indirect bits are 0
        if (IX.get() == 0 && IND.get() == 0) {
          //  System.out.println("Indexing/indirect is off.");
            return ADDR.get();
        }
        
        // indexing is on, indirect is off
        else if (IX.get() == 1 && IND.get() == 0) {
            System.out.println("Indexing is on, indirect is off.");
            return ADDR.get() + XO.get();
        }
        
        else if (IX.get() == 0 && IND.get() == 1) {
            System.out.println("Indexing is off, indirect is on.");
            mem.MAR.set(ADDR.get());
            mem.MBR.set(mem.accessMemory(mem.MAR.get()));
            return mem.MBR.get();
        }
        
        else if (IX.get() == 1 && IND.get() == 1) {           
            System.out.println("Indexing is on and indirect is on.");
            mem.MAR.set(ADDR.get() + XO.get());
            mem.MBR.set(mem.accessMemory(mem.MAR.get()));
            return mem.MBR.get();
        }

        // if none of these succeed, there's an error, so halt the processor
        else {
            HL.set(1);
            System.out.println("ADDR is: " + ADDR);
            return ADDR.get();
        }
    }
    
    /**
     * Processes the CPU function.
     * @param opcode
     */
    public void execute(int opcode) {
        
        switch (opcode) {
        
        case (HLT):
            System.out.println("Case HLT");
            HL.set(1);
            mem.setFault(2);
            break;
            
        case (LDR):
            //System.out.println("Executing LDR, ADDR is " + ADDR.get());
            mem.MAR.set(calculateEA());
            //System.out.println("MAR is " + mem.MAR.get());
            int temp = mem.accessMemory(mem.MAR.get());
            mem.MBR.set(temp);
            RF.set(RSR.get(), mem.MBR.get());
            incrementPC();
            System.out.println("===LDR loaded R" + RSR.get() + " with value " + RF.getSigned(RSR.get()));
            break;
            
        case (STR):
           // System.out.println("Executing STR.");
            mem.MAR.set(calculateEA());
            mem.MBR.set(RF.getContents(RSR.get()));
            mem.store(mem.MAR.get(), mem.MBR.getSigned());
            System.out.println("===STR stored " + mem.MBR.getSigned() + " into Memory: " + mem.MAR.get());  
            incrementPC();
            break;
            
        case (LDA):
            //System.out.println("Executing LDA.");
            mem.MAR.set(calculateEA());
            RF.set(RSR.get(), mem.MAR.get());
            System.out.println("===LDA loaded R" + RSR.get() + " with value " + RF.get(RSR.get()));
            incrementPC();
            break;
            
        case (LDX):
            //System.out.println("Executing LDX.");
            mem.MAR.set(calculateEA());
            mem.MBR.set(mem.accessMemory(mem.MAR.get()));
            XO.set(mem.MBR.get());
            System.out.println("===LDX Loaded X0 with " + mem.MBR.get());
            incrementPC();
            break;
            
        case (STX):
           // System.out.println("Executing STX.");
            mem.MAR.set(calculateEA());
            mem.MBR.set(XO.get());
            mem.store(mem.MAR.get(), mem.MBR.get());
            System.out.println("===STX stored XO value " + XO.get() + " into memory: " + mem.MAR.get());
            incrementPC();
            break;
        
        case (JZ):
           // System.out.println("Executing JZ.");
            if(RF.getContents(RSR.get()) == 0) { // if c(r) = 0
              mem.MAR.set(calculateEA());
              PC.set(mem.MAR.get());             // PC = EA
              System.out.println("===Register is zero, JZ Jumping to: " + PC.get());
             }
            else {                               // else PC = PC + 1
              incrementPC();
              System.out.println("===Register is not Zero, JZ Not Jumping, PC+=1: " + PC.get());
             }
             break;
        
        case (JNE):
           //System.out.println("Executing JNE:");
            if(RF.getContents(RSR.get()) != 0) { // if c(r) != 0
              mem.MAR.set(calculateEA());
              PC.set(mem.MAR.get());             // PC = EA
              System.out.println("===Register not zero, JNE Jumping to: " + PC.get());
             }
            else {                               // else PC = PC + 1
              incrementPC();
              System.out.println("===Register is Zero, JNE Not Jumping, PC+=1: " + PC.get());
             }
             break;       
        
        case (JCC):
           //System.out.println("Executing JCC.");
           if(alu.CC.get() == 1) {         //if cc = 1
             mem.MAR.set(calculateEA());
             PC.set(mem.MAR.get());    //PC = EA
             System.out.println("===CC is 1, JCC Jumping to: " + PC.get());
            }
           else {                      //else PC = PC + 1
             incrementPC();
             System.out.println("===CC is not 1, JCC Not Jumping, PC+=1: " + PC.get());
            }
           break;
           
        case (JMP):
           mem.MAR.set(calculateEA());
           PC.set(mem.MAR.get());
           System.out.println("===JMP to " + PC.get());
           break;
           
        case (JSR):  // Not sure what "R0 should contain pointer to arguments" means
           RF.get(3).set(PC.get() + 1 );     //Save return address to Register Three           
           mem.MAR.set(calculateEA());       //Calculate EA and assign to PC
           PC.set(mem.MAR.get());
           System.out.println("===JSR jumping to " + PC.get() + " R3 return addres: " + RF.get(3));           
           break;
         
        case (RFS):       
           RF.get(0).set(ADDR.get());   // Save Immediate to Register Zero           
           PC.set(RF.getContents(3));           // Set PC to Register Three
           System.out.println("===RFS returning to " + PC.get() + " stored return code in R0: " + RF.get(0));
           break;
        
        case (SOB):
          // System.out.println("Executing SOB.");
           alu.data1.set(RF.getContents(RSR.get()));  //Load Register into ALU
           alu.data2.set(1);                          //Load 1 into ALU
           alu.execute(opcode);                       // Send to ALU to execute data1 - data2 = alu.ARR
           RF.set(RSR.get(), (alu.ARR.get()));       // Load result back into the same register
            if(RF.getContents(RSR.get()) > 0) { // if c(r) != 0
              mem.MAR.set(calculateEA());
              PC.set(mem.MAR.get());             // PC = EA
              System.out.println("=**=SOB looping " + RF.getSigned(RSR.get()) + " more time(s)=**= Current Closest Value is at memory location =========>>>>>>" + RF.getSigned(3));
             }
            else {                               // else PC = PC + 1
              incrementPC();
              System.out.println("====SOB Exiting loop====");
             }
             break;              
                 
        case (AMR):       //AMR and SMR load up the data in the same way.             
        case (SMR):
          // System.out.println("Executing " + opcode);
           mem.MAR.set(calculateEA());
           mem.MBR.set(mem.accessMemory(mem.MAR.get()));
          // System.out.println("SMR Reg Value " + RF.getContents(RSR.get()));

           alu.data1.set(RF.getContents(RSR.get()));
           alu.data2.set(mem.MBR.get());
           alu.execute(opcode);
            if (alu.CC.getBit(0) == 1) {  //CC(1) which is CC register bit 0, represents overflow
                HL.set(1);
               break;  //error message was already set from ALU
             }           
           RF.get(RSR.get()).set(alu.ARR.getSigned());
           System.out.println("====AMR/SMR Added/Subtracted " + alu.data1.get() + " +/- " + alu.data2.get() + "; R" + RSR.get() + " = " + RF.getSigned(RSR.get()) );
           incrementPC();
           break;
        
        case (ISR):   //ISR and IAR load up the data for the ALU in the same way
        case (IAR):
          // System.out.println("Executing " + opcode);
           if ( ADDR.get() != 0) {   //If immed = 0, does nothing
               if ( RF.getContents(RSR.get()) == 0) {  //if c(r) = 0, load r with immed
                   RF.set(RSR.get(), ADDR.get());
                }
               else {   //Immed and r have values, so add them
                   alu.data1.set(RF.getContents(RSR.get()));
                   alu.data2.set(ADDR.get());
                   alu.execute(opcode);  
                   if (alu.CC.getBit(0) == 1) {  //CC(1) which is CC register bit 0, represents overflow.  
                       HL.set(1);
                       break;  //error message was already set from ALU
                    }
                  RF.set(RSR.get(), (alu.ARR.getSigned()));
                }
            }
            System.out.println("====ISR/IAR subtracted/added " + alu.data1.get() + " -/+ " + alu.data2.get() + " = Reg" + RSR.get() + " value: " + RF.getSigned(RSR.get())); 
            incrementPC();
            break;
        
        case (TST):
           // System.out.println("Executing " + opcode);
            alu.data1.set(RF.getContents(RX.get()));
            alu.data2.set(RF.getContents(RY.get()));
            alu.execute(TST);
            System.out.println("====TST tested " + alu.data1.get() + " == " + alu.data2.get() + " and set EQUALNOT CC(4) to :" + alu.CC.getBit(3));  //bit 3 = CC4
            incrementPC();
            break;
           
        case (AND):
        case (OR):
        case (NOT):
            //System.out.println("Executing " + opcode);
            alu.data1.set(RF.getContents(RX.get()));
            alu.data2.set(RF.getContents(RY.get()));  //This part isn't used by NOT, but it should be harmless
            alu.execute(opcode);
            RF.set(RX.get(), (alu.ARR.get()));
            System.out.println("====AND/OR/NOT set R" + RX.get() + " to value " + RF.getSigned(RX.get()));
            incrementPC();
            break;
            
        case (MUL):
           alu.data1.set(RF.getContents(RX.get()));
           alu.data2.set(RF.getContents(RY.get()));
           alu.execute(opcode);
           RF.set(RX.get(), alu.ARR.getSigned());  //Higher order bits have a sign
           RF.set((RX.get() +1), alu.ARR2.get());        //Lower order bits are unsigned
           System.out.println("=====MUL set R" + RX.get() + " to signed value " + RF.getSigned(RX.get()) + " And R" + (RX.get() +1) + " to unsigned value " + RF.getContents((RX.get() + 1)));
           incrementPC();
           break;
        
        case (DIV):
           alu.data1.set(RF.getContents(RX.get()));
           alu.data2.set(RF.getContents(RY.get()));
           alu.execute(opcode);
           if(alu.CC.getBit(2) == 1) {  //If ALU returns with CC(3) = 1 then it is a division by zero error.  
               HL.set(1);
               break;
            }
           RF.set(RX.get(), alu.ARR.getSigned());
           RF.set((RX.get() + 1), alu.ARR2.get());
           System.out.println("=====Div returned Quotient R" + RX.get() + " to signed value " + RF.getSigned(RX.get()) + " And remainder R" + (RX.get() +1) + " to unsigned value " + RF.getContents((RX.get() + 1)));
           incrementPC();
           break;
            
        case (SRC):
           // System.out.println("Executing " + opcode);
            alu.data1.set(RF.getContents(RSR.get()));
            alu.execute(SRC);
            RF.get(RSR.get()).set(alu.ARR.get());
            System.out.println("====SRC set R" + RSR.get() + " to value " + RF.get(RSR.get()));
            incrementPC();
            break;   
        
        case (RRC):
            alu.data1.set(RF.getContents(RSR.get()));
            alu.execute(RRC);
            RF.get(RSR.get()).set(alu.ARR.get());
            System.out.println("=====RRC set R" + RSR.get() + " to value " + RF.get(RSR.get()));
            incrementPC();
            break;
            
        default: 
            // Invalid opcode, halts the processor.
            System.out.println("Default case: halts processor.");
            HL.set(1);
            mem.setFault(2);
            break;
        }
        
    }
    
    /**
     * Increments the program counter.
     */
    public void incrementPC() {
        PC.set(PC.get() + 1);
    }

}
