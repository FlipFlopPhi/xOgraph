/**
 * 
 */
package ru.flip.xOgraph.model;

import java.util.LinkedList;
import java.util.List;

/**
 * A class containing useful static functions to do complicated calculations using hexes and potitions.
 * @author Vizu
 *
 */
public class HexUtil {

	/**
	 * Creates a list of all elements that would be on the line from a to b (excluding b).
	 * @param a position a
	 * @param b position b.
	 * @return a list consisting of all positions that are on the line from a to b (excluding b).
	 * @deprecated
	 */
	public static List<Position> getLine2(Position a, Position b) {
		List<Position> pieces = new LinkedList<Position>();
		MicroPosition current = a.toMicroPosition();
		MicroPosition finish = b.toMicroPosition();
		if (current.depth < finish.depth) {
			current.enhanceTo(finish.depth);
		} else if (finish.depth < current.depth) {
			finish.enhanceTo(current.depth);
		}
		
		while( !current.equals(finish)) {
			int dq = finish.q - current.q;
			int dr = finish.r - current.r;
			Shift shift;
			if (-dq + dr >= 0 & dq + 2*dr < 0)
				shift = Shift.LARM;
			else if (dq+2*dr >=0 & 2*dq +dr < 0)
				shift = Shift.LLEG;
			else if (dq-dr<0 & 2*dq+dr>=0)
				shift = Shift.BOTTOM;
			else if (dq-dr>=0 & -dq-2*dr < 0)
				shift = Shift.RLEG;
			else if (-dq-2*dr >= 0 & -2*dq -dr < 0)
				shift = Shift.RARM;
			else
				shift = Shift.HEAD;
			pieces.add(new Position(current));
			current.q += shift.getQIncrement();
			current.r += shift.getRIncrement();
		}
		return pieces;
	}
	
	/**
	 * Creates a list of all positions that would be on the line from a to b (excluding b), as though a and b where at the specified depth.
	 * @param a position a
	 * @param b position b
	 * @param depth during calculations, a and b are enhanced to the specified depth
	 * @return a list consisting of all positions that are on the line from a to b (excluding b)
	 */
	public static List<Position> getLine(Position a, Position b, int depth) {
		List<Position> pieces = new LinkedList<Position>();
		MicroPosition current = a.toMicroPosition();
		MicroPosition finish = b.toMicroPosition();
		current.enhanceTo(depth);
		finish.enhanceTo(depth);
		
		int dq = finish.q - current.q;
		int dr = finish.r - current.r;
		ShiftStruct struct1;
		ShiftStruct struct2;
		
		if (dr <= 0 & dq < 0) {
			struct1 = new ShiftStruct(Shift.LARM, -dq +1);
			struct2 = new ShiftStruct(Shift.HEAD, -dr +1);
		} else if (dr + dq <= 0 & dr > 0) {
			struct1 = new ShiftStruct(Shift.LLEG, dr +1);
			struct2 = new ShiftStruct(Shift.LARM, -dr-dq +1);
		} else if (dq <= 0 & dr + dq > 0) {
			struct1 = new ShiftStruct(Shift.BOTTOM, dr+dq +1);
			struct2 = new ShiftStruct(Shift.LLEG, -dq +1);
		} else if (dr >= 0 & dq > 0) {
			struct1 = new ShiftStruct(Shift.RLEG, dq +1);
			struct2 = new ShiftStruct(Shift.BOTTOM, dr +1);
		} else if (dr + dq >= 0 & dr < 0) {
			struct1 = new ShiftStruct(Shift.RARM, -dr +1);
			struct2 = new ShiftStruct(Shift.RLEG, dr+dq +1);
		} else {
			struct1 = new ShiftStruct(Shift.HEAD, -dq-dr +1);
			struct2 = new ShiftStruct(Shift.RARM, dq +1);
		}
		
		while(!current.equals(finish)) {
			pieces.add(new Position(current));
			ShiftStruct struct;
			if (struct1.next() >= struct2.next()) {
				struct = struct1;
			} else {
				struct = struct2;
			}
			struct.shift(current);
			struct.incr();
		}
		return pieces;
	}
	
	/**
	 * Creates a list of all positions that would be on the line from a to b (excluding b), as though a and b where at the same depth (the deepest).
	 * @param a position a
	 * @param b position b
	 * @return a list consisting of all positions that are on the line from a to b (excluding b)
	 */
	public static List<Position> getLine(Position a, Position b) {
		int depth = Math.max(a.getDepth(), b.getDepth());
		return getLine(a, b, depth);
	}
	
	private static class ShiftStruct {
		
		Shift shift;
		double ratio;
		double ratioIncr;
		
		ShiftStruct(Shift shift, int amount) {
			this.shift = shift;
			ratio = 1d;
			ratioIncr = 1d / (double)amount;
		}
		
		double next() {
			return ratio - ratioIncr;
		}
		
		void shift(MicroPosition micro) {
			micro.q += shift.getQIncrement();
			micro.r += shift.getRIncrement();
		}
		
		void incr() {
			ratio -= ratioIncr;
		}
	}
	
	/**
	 * Translates the position of the mouse on the canvas to a Position object.
	 * @param mouseX the mouse's x coordinate
	 * @param mouseY the mouse's y coordinate
	 * @param depth the maximum depth we want to look at
	 * @param hexagonRadius the radius of hexagons on the current canvas
	 * @return a Position object that the mouse's coordinates translate to.
	 */
	public static Position getPositionAt(int mouseX, int mouseY, int depth, double hexagonRadius) {
		double radius = hexagonRadius;
		double angle = 0;
		double vecX = mouseX - radius;
		double vecY = mouseY - radius;
		for(int i = depth; i>0; i--) {
			radius *= Math.sqrt(3)/(Math.sqrt(21));
			angle += Math.atan(1.5d/(2.5d*Math.sqrt(3)));
		}
		
		double q = (vecX * Math.cos(-angle) - vecY * Math.sin(-angle))
				/((double)radius *3/2);
		double r = (vecX * Math.cos(-angle - 2*Math.PI / 3) - vecY * Math.sin(-angle - 2*Math.PI / 3)) 
				/ ((double)radius *3/2);
		//convert axial to cube
		double x = q;
		double z = r;
		double y = -x-z;
		
		//rounding cube
		double rx = Math.round(x);
		double ry = Math.round(y);
		double rz = Math.round(z);
		
		double dx = Math.abs(rx - x);
		double dy = Math.abs(ry - y);
		double dz = Math.abs(rz - z);
		
		if (dx > dy & dx > dz)
			rx = -ry-rz;
		else if (dy > dz)
			ry = -rx-rz;
		else
			rz = -rx-ry;
		
		//convert cube to axial
		int newQ = (int) rx;
		int newR = (int) rz;
		
		return new Position(newQ, newR, depth);
		
	}
}
