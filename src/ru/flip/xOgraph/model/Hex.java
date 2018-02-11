/**
 * 
 */
package ru.flip.xOgraph.model;

/**
 * 
 * @author Vizu
 *
 */
public class Hex {

	/**
	 * The hextoken of this hex.
	 */
	public HexToken token;
	
	/**
	 * Whether the current hex is selected, should only be modified via Project.select and Project.deselect.
	 */
	public boolean selected = false;
	
	private Hex parent;
	private Hex[] children;
	private Position position;
	
	/**
	 * Create a hex with position (x,y) at depth 0.
	 * @param x the column
	 * @param y the row
	 */
	public Hex(int x, int y) {
		position = new Position(x, y);
	}
	
	/**
	 * Create a child hex of the parent hex at the shifted position.
	 * @param parent the parent hex.
	 * @param shift the the hexical offset.
	 */
	public Hex(Hex parent, Shift shift) {
		this.parent = parent;
		position = new Position(parent.getPosition(), shift);
	}

	/**
	 * Splits the current hex into 7 smaller hexes, if it didn't have any.
	 */
	public void enhance() {
		if (children != null)
			return;
		children = new Hex[7];
		for(int i = 0; i<7; i++) {
			children[i] = new Hex(this, Shift.valueOf(i));
			children[i].token = token;
		}
	}
	
	/**
	 * Removes the children hexes of this hex.
	 */
	public void dehance() {
		children = null;
	}
	
	/**
	 * Returns whether this hex is currently split into 7 smaller hexes.
	 */
	public boolean isEnhanced() {
		return (children != null);
	}

	/**
	 * Get the child hex at position i
	 * @param i 
	 */
	public Hex getChild(int i) {
		return children[i];
	}
	
	/**
	 * Get this hex' child hex at the shifted position
	 * @param shift the shifted position, null returns the middle child hex.
	 */
	public Hex getChild(Shift shift) {
		if (shift == null)
			return children[0];
		return children[shift.toInt()];
	}

	public Position getPosition() {return position;}
	public Hex getParent() {return parent;}

	
}
