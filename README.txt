Run the jar file from the command line with 

java -jar MadeyAndersonProject.jar

During the program you can see the memory location of the current closest value at each SOB loop command.  

At the end of the program the program will dump the results of R0-R1 and tell you what each value means.  R0 will be the user input, R1 will be the closest value and R3 will be the address of the closest value. 

The program is loaded in the CPU class (see sample below).  To change the user input change the value of 4416 to whatever value you want to enter.  You can also change the 20 integers starting right below that.   



==========================================================
 // load memory with data
     
        mem.store(26, 20);  //Used to initialize the loop counter, Loaded to R2 with first instruction  
        mem.store(27, 0);  //TMP variable for copying registers and whatnot
        mem.store(28, 32767); //SmallestDifference variable keeps track of difference between CLOSEST and NEXTVALUE, 
        mem.store(29, 31);  //This is where we keep track of the NEXTVALUE
--->    mem.store(30, 4416);  //This is the user input
        mem.store(31, 2342);  //First value of 20
        mem.store(32, 465);
        mem.store(33, 2052);
        mem.store(34, 5016);