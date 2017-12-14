/**
 * 
 */
package ru.flip.xOgraph.model;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vizu
 * TODO: Rework this whole class
 * Requirements:
 * 1. Regions should maintain a list of corners
 * 2. The List of corners should be maintained ordered and clockwise or counterclockwise
 * 3. Regions should be able to look wether a position is within the region
 * 4. Regions should be able to maintain
 * TODO: finish this list of requirements
 */
public class Region {
	//TODO handle drawdistance
	
	public final List<Position> corners;
	public String name;
	
	private int drawDepth;
	public final List<List<Position>> borderPieces;
	
	

	/**
	 * Creates a region with the given list of points as its corners.
	 * A copy of the submitted list is used, thus when the list is later altered it will not affect this region.
	 * @param points a list of the positions that are used as the regions corners
	 * @param name the region's name
	 */
	public Region(List<Position> points, String name) {
		this.corners = new ArrayList<Position>(points);
		borderPieces = new LinkedList<>();
		for(int i=0; i<points.size()-1; i ++) {
			List<Position> segment = 
					HexUtil.getLine(points.get(i), points.get(i+1), drawDepth);
			borderPieces.add(segment);
		}
		borderPieces.add(
				HexUtil.getLine(points.get(points.size()-1), points.get(0), drawDepth)
				);
		this.name = name;
	}
	
	/**
	 * Creates a region starting from the submitted point. The extend method can then later be used to add additional corners to the region.
	 * @param point the first position/corner of this regions border
	 */
	public Region(Position point) {
		corners = new LinkedList<>();
		corners.add(point);
		borderPieces = new LinkedList<>();
		borderPieces.add( new LinkedList<Position>());
	}
	
	/**
	 * Adds a new corner to the list of this region's corners.
	 * @param point corner to be added to the list of corners
	 */
	public void extend(Position point) {
		borderPieces.remove(borderPieces.size()-1);
		borderPieces.add(
				HexUtil.getLine(corners.get(corners.size()-1), point, drawDepth)
				);
		borderPieces.add(
				HexUtil.getLine(point, corners.get(0), drawDepth)
				);
		corners.add(point);
	}
	
	/**
	 * Removes a corner from the list of corners of this region.
	 * If the submitted position is not a corner of this region, nothing happens.
	 * @param point the to be removed corner
	 */
	public void removePoint(Position point) {
		int index = corners.indexOf(point);
		if (index == -1)
			return;
		borderPieces.remove(index);
		corners.remove(index);
		if (index == 0)
			index = borderPieces.size();
		borderPieces.set(index-1, HexUtil.getLine(
				corners.get(index-1)
				, corners.get(index % borderPieces.size())
				, drawDepth)
			);
		
	}
	
	/**
	 * Moves a specific corner to a new position.
	 * @param oldPoint the corner's old position
	 * @param newPoint the corner's new position
	 */
	public void disLocate(Position oldPoint, Position newPoint) {
		int index = corners.indexOf(oldPoint);
		if (index != -1) {
			borderPieces.remove(index);
			corners.remove(index);
			if(index==0)
				index = borderPieces.size();
			borderPieces.add(index
					, HexUtil.getLine(newPoint, corners.get((index+1) % (corners.size()+1)), drawDepth)
					);
			borderPieces.set(index-1
					, HexUtil.getLine(corners.get(index-1), newPoint, drawDepth)
					);
			corners.add(index, newPoint);
		} else {
			
			for(int i=0; i < borderPieces.size(); i++) {
				if (borderPieces.get(i).contains(oldPoint)) {
					index = i;
					borderPieces.remove(index);
					break;
				}
			}
			if (index == -1)
				return;
			borderPieces.add(index
					, HexUtil.getLine(newPoint, corners.get((index+1) % corners.size()), drawDepth)
					);
			borderPieces.add(index
					, HexUtil.getLine(corners.get(index), newPoint, drawDepth)
					);
			corners.add(index+1, newPoint);
		}
	}
	
	/**
	 * Enhances the current region to a different depth
	 * @param newDepth the region's new depth
	 */
	public void changeDepth(int newDepth) {
		//TODO change internal code, so that it uses getLine(Position, Position, int)
		Position[] newPoints = new Position[corners.size()];
		int i = 0;
		for (Position point : corners) {
			newPoints[i] = new Position(point, newDepth);
			i++;
		}
		borderPieces.clear();
		for(int j=0; j<newPoints.length-1; j++) {
			List<Position> segment = 
					HexUtil.getLine(newPoints[j], newPoints[j+1]);
			borderPieces.add(segment);
		}
		borderPieces.add(
				HexUtil.getLine(newPoints[newPoints.length-1], newPoints[0])
				);
	}
	
	/**
	 * Returns a position on the border with index i.
	 * TODO: might want to change the usage of this method to make it more intuitive
	 * @param index the position's index on this region's border
	 * @return the position with index i
	 */
	public Position get(int index) {
		int i=0;
		for(List<Position> borderPiece : borderPieces) {
			for (Position position : borderPiece) {
				if (i==index)
					return position;
				i++;
			}
		}
		return null;
	}
	
	/**
	 * Checks whether the given position is on the border of this region.
	 * TODO: change this methods name and make a method that checks whether a point is within the regions borders
	 * @param point the position 
	 * @return true when a position is located on this region's border
	 */
	public boolean contains(Position point) {
		for(List<Position> borderPiece : borderPieces) {
			for (Position position : borderPiece) {
				if (position.contains(point))
					return true;
			}
		}
		return false;
	}

	/**
	 * Calculates the amount of hexes that are on this region's border.
	 *  The returned value might be different depending on the drawing depth.
	 * @return the number of hexes on this region's border.
	 */
	public int getBorderLength() {
		int total = 0;
		for(List<?> borderPiece : borderPieces) {
			total += borderPiece.size();
		}
		return total;
	}
	
	/**
	 * Translates the region to a polygon containing the drawing coordinates of each of its corners.
	 * @param hexagonRadius the radius with which a hexagon at depth 0 is drawn
	 * @return a polygon with the drawing coordinates of all of this region's corners
	 */
	public Polygon toPolygon(double hexagonRadius) {
		int borderSize = getBorderLength();
		int[] xs = new int[borderSize];
		int[] ys = new int[borderSize];
		int i=0;
		for(List<Position> borderPiece : borderPieces) {
			for (Position position : borderPiece) {
				xs[i] = position.getCanX(hexagonRadius);
				ys[i] = position.getCanY(hexagonRadius);
				i++;
			}
		}
		return new Polygon(xs, ys, borderSize);
	}
	
	@Override
	public Region clone() {
		return new Region(corners, name);
		
	}

	
}
