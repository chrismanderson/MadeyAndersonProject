package cpu;

public class RegisterFile {

	private Register[] registerFile;
	
	public RegisterFile(int size) {
		
		registerFile = new Register[size];
		
		for (int i = 0; i < size; i++) {
			registerFile[i] = new Register(CPU.DEFAULTSIZE);
		}
	}
	
//	public Register get(int i) {
//		return registerFile[i];
//	}
	
	public Register get(int i) {
		Register temp = registerFile[i];
		return temp;
	}
	
	public int getSigned(int i) {
	   return registerFile[i].getSigned();
    }
	
	public int getContents(int i) {
		return registerFile[i].get();
	}
	
	public void set(int register, int value) {
		Register temp = registerFile[register];
		temp.set(value);
	}
	
	
}
