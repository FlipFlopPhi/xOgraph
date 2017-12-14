/**
 * 
 */
package ru.flip.xOgraph.model;

public class MicroPosition {
	
	public int q;
	public int r;
	public int depth;
	
	/**
	 * 
	 */
	public MicroPosition(int q, int r, int depth) {
		this.q = q;
		this.r = r;
		this.depth = depth;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MicroPosition))
			return false;
		MicroPosition other = (MicroPosition) o;
		return (q == other.q & r == other.r & depth == other.depth);
	}

	public void enhanceTo(int newDepth) {
		for(int d = depth; d > newDepth; d--) {
			r = (3*r + q)/7;
			q = (q-r)/3;
			depth--;
		}
		for(int d = depth; d < newDepth; d++) {
			int newQ = 3*q + r;
			int newR = 2*r - q;
			q = newQ;
			r = newR;
			depth++;
		}
	}
	
	public String toString() {
		return ""+q+","+r+" in "+depth;
	}

}
