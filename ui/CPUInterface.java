package ui;

import cpu.CPU;

public class CPUInterface {

	public static int TEST = 3;


	// this will be the main loop of the interface
	public static void main(String[] args) {
		CPU cpu = new CPU();
		System.out.println(cpu.RF.get(1));
		System.out.println("Welcome to the CPU!");
		cpu.runCPU(TEST);

	}
	
}
