/**
 * 
 */
package ru.flip.xOgraph.model;

/**
 * @author Vizu
 *
 */
public enum Shift {

	LARM(-1,0,1), LLEG(-1,1,2), BOTTOM(0,1,3), RLEG(1,0,4), RARM(1,-1,5), HEAD(0,-1,6);
	
	int qIncrement;
	int rIncrement;
	int number;
	
	private Shift(int qIncrement, int rIncrement, int nr) {
		this.qIncrement = qIncrement;
		this.rIncrement = rIncrement;
		number = nr;
	}
	
	public int getQIncrement() {
		return qIncrement;
	}
	
	public int getRIncrement() {
		return rIncrement;
	}
	
	public static Shift valueOf(int i) {
		for(Shift shift : Shift.values()) {
			if (shift.number == i)
				return shift;
		}
		return null;
	}

	public int toInt() {
		return number;
	}
}
