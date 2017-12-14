/**
 * 
 */
package ru.flip.xOgraph.model;

import java.util.List;

/**
 * A list of adjacent Positions.
 * @author Vizu
 *
 */
public class Line {

	/**
	 * A list containg all the corners on this line. (The list can also contain corners that create a 180 degree angle.)
	 */
	public List<Position> pieces;
	
	/**
	 * Creates a line from position a to b.
	 */
	public Line(Position a, Position b) {
		pieces = HexUtil.getLine(a, b);
		pieces.add(b);
	}

	public Line(List<Position> positions) {
		pieces = positions;
	}
	
	@Override
	public Line clone() {
		return new Line(pieces);
	}

}
