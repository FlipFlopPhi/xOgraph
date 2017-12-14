/**
 * 
 */
package ru.flip.xOgraph.model;

/**
 * The Position object is used to describe the position 
 *  of something (g.e. a Hex or a point of interest) on the hexagonal grid.
 * @author Vizu
 */
public class Position {

	
	private int col, row;
	private Shift[] shifts;
	
	/**
	 * Constructs an object at depth 0 given a column and row number.
	 * @param col the the nr of the column (starts at 0)
	 * @param row the nr of the row (starts at 0)
	 */
	public Position(int col, int row) {
		shifts = new Shift[0];
		this.col = col;
		this.row = row;
	}
	
	/**
	 * Constructs a position at 1 depth lower with the given offset.
	 * @param position the original position
	 * @param shift the new positions offset
	 */
	public Position(Position position, Shift shift) {
		col = position.getCol();
		row = position.getRow();
		int depth = position.getDepth();
		shifts = new Shift[depth+1];
		for(int i=0; i<depth; i++) {
			shifts[i] = position.getShift(i);
		}
		shifts[depth] = shift;
	}
	
	/**
	 * Constructs a new position that is a translation from the old position at the new depth.
	 * @param position the old position
	 * @param newDepth the depth that the position is translated to
	 */
	public Position(Position position, int newDepth) {
		col = position.col;
		row = position.row;
		shifts = new Shift[newDepth];
		int oldDepth = position.getDepth();
		for(int i=0; i<newDepth; i++) {
			if (newDepth >= oldDepth)
				shifts[i] = null;
			else
				shifts[i] = position.getShift(i);
		}
	}
	
	/**
	 * Constructs a position at the given column row and depth.
	 * Note that at lower depths (>0) the columns and rows are no longer horizontal and vertical.
	 * @param q the number of the column (starts at 0 and can also be negative)
	 * @param r the number of the row (starts at 0 and can also be negative)
	 * @param depth the depth
	 */
	public Position(int q, int r, int depth) {
		int[] qs = new int[depth+1];
		int[] rs = new int[depth+1];
		qs[depth] = q;
		rs[depth] = r;
		for(int i=depth; i>0; i--) {
			double dr = (qs[i]+3d*rs[i])/7;
			qs[i-1] = (int)Math.round(2d*dr- rs[i]);
			rs[i-1] = (int)Math.round(dr);
		}
		col = qs[0];
		row = rs[0] + (qs[0] - (qs[0]&1)) / 2;
		
		shifts = new Shift[depth];
		for(int i=0; i < depth; i++) {
			int qOffset = qs[i+1]-(3*qs[i] + rs[i]);
			int rOffset = rs[i+1]-(-1*qs[i] + 2 *rs[i]);
			if (qOffset==0 & rOffset==0)
				shifts[i] = null;
			else if(qOffset==-1)
				shifts[i] = Shift.valueOf(1+rOffset);
			else if(qOffset==0 & rOffset==1) 
				shifts[i] = Shift.BOTTOM;
			else if(qOffset==0 &rOffset==-1)
				shifts[i] = Shift.HEAD;
			else 
				shifts[i] = Shift.valueOf(4-rOffset);
		}
	}

	/**
	 * Translates a microposition into a position.
	 * @param micro the to-be translated microposition
	 */
	public Position(MicroPosition micro) {
		this(micro.q, micro.r, micro.depth);
	}

	public int getRow() {return row;}
	public int getCol() {return col;}

	/**
	 * 
	 * @return the amount of shifts in this position.
	 */
	public int getDepth() {
		return shifts.length;
	}
	
	public Shift getShift(int depth) {
		return shifts[depth];
	}

	@Override
	public String toString() {
		String out = "("+col + ","+row+")";
		for(Shift shift : shifts) {
			if (shift == null)
				continue;
			out += ">"+shift.toInt();
		}
		return out;
	}
	
	/**
	 * Returns the same string as if .toMicroPosition().toString() were called, however no instance of MicroPosition is created.
	 * @return  the same string as if .toMicroPosition().toString().
	 */
	public String toMicroString() {
		int q = col;
		int r = row - (col - (col&1)) /2;
		for(Shift iteratedShift : shifts) {
			int newQ = 3*q + r;
			int newR = 2*r - q;
			
			q = newQ; 
			r = newR;
			if (iteratedShift == null)
				continue;
			q += iteratedShift.getQIncrement();
			r += iteratedShift.getRIncrement();
		}
		return "("+q+","+r+","+shifts.length+")";
	}

	public int getCanX(double hexagonRadius) {
		double radius = hexagonRadius;
		double angle = 0;
		int x = (int) (Math.round(col*(radius*1.5)) + radius);
		for(Shift shift : shifts) {
			angle +=  Math.atan(1.5d/(2.5d*Math.sqrt(3)));
			radius *= Math.sqrt(3)/(Math.sqrt(21));
			if (shift == null)
				continue;
			x = (int) (x+(Math.sqrt(3)*radius)*Math.cos((Math.PI/6d - angle)
					+ 2*Math.PI/6 + Math.PI/3 + (shift.toInt()-1)*Math.PI/3));
		}
		return x;
	}

	public int getCanY(double hexagonRadius) {
		double radius = hexagonRadius;
		double angle = 0;
		int y = (int) (Math.round(row*radius*Math.sqrt(3) + (col%2)*(radius/2)*Math.sqrt(3)) + radius);
		for(Shift shift : shifts) {
			angle +=  Math.atan(1.5d/(2.5d*Math.sqrt(3)));
			radius *= Math.sqrt(3)/(Math.sqrt(21));
			if (shift==null)
				continue;
			y = (int) (y+(Math.sqrt(3)*radius)*-Math.sin((Math.PI/6d - angle)
					+ 2*Math.PI/6 + Math.PI/3 + (shift.toInt()-1)*Math.PI/3));
		}
		return y;
	}
	
	public Position createNeighbour(Shift shift) {
		int q = col;
		int r = row - (col - (col&1)) /2;
		for(Shift iteratedShift : shifts) {
			int newQ = 3*q + r;
			int newR = 2*r - q;
			
			q = newQ; 
			r = newR;
			if (iteratedShift == null)
				continue;
			q += iteratedShift.getQIncrement();
			r += iteratedShift.getRIncrement();
		}
		
		return new Position(q + shift.getQIncrement()
			, r + shift.getRIncrement()
			, shifts.length);
	}
	
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof Position))
			return false;
		Position pos = (Position) o;
		if (col != pos.col | row != pos.row)
			return false;
		return (shifts.length == pos.shifts.length);
	}
	
	public boolean contains(Position position) {
		if (col != position.col | row != position.row 
				| shifts.length < position.shifts.length)
			return false;
		for(int i=0; i < shifts.length; i++) {
			if (shifts[i] == position.shifts[i])
				continue;
			else
				return false;
		}
		return true;
	}

	public MicroPosition toMicroPosition() {
		int q = col;
		int r = row - (col - (col&1)) /2;
		for(Shift iteratedShift : shifts) {
			int newQ = 3*q + r;
			int newR = 2*r - q;
			
			q = newQ; 
			r = newR;
			if (iteratedShift == null)
				continue;
			q += iteratedShift.getQIncrement();
			r += iteratedShift.getRIncrement();
		}
		return new MicroPosition(q, r, shifts.length);
	}

}
