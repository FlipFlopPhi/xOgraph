/**
 * 
 */
package ru.flip.xOgraph.model;

import java.util.LinkedList;
import java.util.List;

/**
 * A map object contains all the information the map should have. It also contains all objects (Points of Interest, Regions, etc.) present on the map.
 * @author Vizu
 *
 */
public class Map {
	
	private Hex[][] hexes;
	public final List<Point> points;
	public final List<Region> regions;
	public final List<Line> lines;
	/**
	 * The distance between two hexes at depth 0 in meters.
	 */
	public double singleHexDistance = 40_000;
	
	/**
	 * Creates a (col x rows) map.
	 * @param cols The amount of columns this map should have.
	 * @param rows The amount of rows this map should have.
	 */
	public Map(int cols, int rows) {
		hexes = new Hex[cols][rows];
		for(int x=0; x<cols; x++) {
			for(int y=0; y<rows; y++) {
				hexes[x][y] = new Hex(x,y);
			}
		}
		
		points = new LinkedList<Point>();
		regions = new LinkedList<Region>();
		lines = new LinkedList<Line>();
	}
	
	/**
	 * Creates a resized version of the old map.
	 * @param oldMap
	 * @param newCols
	 * @param newRows
	 */
	public Map(Map oldMap, int newCols, int newRows) {
		hexes = new Hex[newCols][newRows];
		for(int x=0; x < newCols; x++) {
			for(int y=0; y < newRows; y++) {
				if( x >= getWidth() | y >= getHeight() )
					hexes[x][y] = new Hex(x,y);
				else
					hexes[x][y] = oldMap.hexes[x][y];
			}
		}
		
		points = new LinkedList<Point>();
		for (Point point : oldMap.points) {
			if(point.getPosition().getCol() < hexes.length
					& point.getPosition().getRow() < hexes[0].length) {
				points.add(point);
			}
		}
		
		regions = new LinkedList<Region>();
		for (Region region : oldMap.regions) {
			Region newRegion = region;
			boolean regionModified = false;
			for (Position corner: region.corners) {
				if (corner.getCol() >= hexes.length
						| corner.getRow() >= hexes[0].length) {
					if(!regionModified) {
						newRegion = region.clone();
						regionModified = true;
					}
					newRegion.removePoint(corner);
				}
			}
			regions.add(newRegion);
		}
		
		lines = new LinkedList<Line>();
		for (Line line : oldMap.lines) {
			Line newLine = line;
			boolean lineModified = false;
			for (Position corner: line.pieces) {
				if (corner.getCol() >= hexes.length
						| corner.getRow() >= hexes[0].length) {
					if(!lineModified) {
						newLine = line.clone();
						lineModified = true;
					}
					newLine.pieces.remove(corner);
				}
			}
			lines.add(newLine);
		}
	}

	/**
	 * Returns the amount of columns (at depth 0) this map has.
	 * @return the amount of columns this map has.
	 */
	public int getWidth() {
		return hexes.length;
	}
	
	/**
	 * Returns the amount of rows (at depth 0) this map has.
	 * @return the amount of rows this maps has
	 */
	public int getHeight() {
		return hexes[0].length;
	}

	/**
	 * Returns the hex at (col, row, depth=0)
	 * @param col the requested hex' x-coordinate
	 * @param row the requested hex' y-coordinate
	 * @return the hex at (col, row, depth=0)
	 */
	public Hex getHex(int col, int row) {
		if (col >=0 & col <  hexes.length
				& row >=0 && row < hexes[col].length)
			return hexes[col][row];
		else 
			return null;
	}
	
	/**
	 * Returns the hex at the given position.
	 * @param position 
	 * @return the hex at the given position
	 */
	public Hex getHex(Position position) {
		int col = position.getCol();
		int row = position.getRow();
		if (col >=0 & col <  hexes.length
				& row >=0 && row < hexes[col].length) {
			Hex hex = hexes[position.getCol()][position.getRow()];
			for(int i=0; i< position.getDepth(); i++) {
				if (!hex.isEnhanced())
					return hex;
				hex = hex.getChild(position.getShift(i));
			}
			return hex;
		}
		return null;
	}
	
	/**
	 * Changes the size of the current map to col x row. Old hexes which position falls outside of the new map are removed.
	 * @param col the amount of columns the new map should have
	 * @param row the amount of rows the new map should have
	 */
	public void resize(int col, int row) {
		Hex[][] newHexes = new Hex[col][row];
		for(int x=0; x<col; x++) {
			for(int y=0; y<row; y++) {
				if( x >= getWidth() | y >= getHeight() )
					newHexes[x][y] = new Hex(x,y);
				else
					newHexes[x][y] = hexes[x][y];
			}
		}
		hexes = newHexes;
	}

	/**
	 * Adds the current point of interest to the map.
	 * @param point the point of interest that is added
	 */
	public void addPoint(Point point) {
		points.add(point);
	}

	/**
	 * Remove a point of interest from the given hex and the map.
	 * @param hex the hex from which we want to remove the point of interest
	 */
	public void removePointFromHex(Hex hex) {
		Point point = hex.getPOI();
		if (point == null)
			return;
		hex.setPOI(null);
		points.remove(point);
	}

	/**
	 * Returns an array of all points on this map. Editing the array does not change the points on this map;
	 *  changing the points on the map should be done through addPoint and removePointFromHex.
	 * @return an array of all points on this map
	 */
	public Point[] getPoints() {
		Point[] out = new Point[points.size()];
		return points.toArray(out);
	}

}
