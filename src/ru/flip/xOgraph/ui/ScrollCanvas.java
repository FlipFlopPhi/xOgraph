/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.Polygon;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Region;

/**
 * The ScrollCanvas is a JScrollPane containing the HexCanvas. 
 * This class only concerns the management of the viewport, for graphical features of the map itself, HexCanvas is used.
 * @author Vizu
 *
 */
public class ScrollCanvas extends JScrollPane {

	/**
	 * 
	 */
	public ScrollCanvas() {
		super(Project.canvas);
		createHorizontalScrollBar();
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		createVerticalScrollBar();
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
	}
	
	/**
	 * Moves and zooms the viewport so that the entire region is in it without distorting anything.
	 * @param region the region to be focussed on
	 */
	public void focusOn(Region region) {
		Polygon polygon = region.toPolygon(Project.canvas.radius);
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		for(int x : polygon.xpoints) {
			minX = Math.min(x, minX);
			maxX = Math.max(x, maxX);
		}
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for(int y: polygon.ypoints) {
			minY = Math.min(y, minY);
			maxY = Math.max(y, maxY);
		}
		double widthRatio = (double)getViewport().getWidth() / (double)(maxX - minX);
		double heightRatio = (double)getViewport().getHeight() / (double)(maxY - minY);
		double minRatio = Math.min(widthRatio, heightRatio);
		Project.scale *= minRatio;
		Project.zoomPanel.changeValue((int)(Project.scale*100));
		Project.repaint();
		java.awt.Point topLeft = new java.awt.Point((int)(minX*minRatio), (int)(minY*minRatio));
		this.getViewport().setViewPosition(topLeft);
		
	}
	
	/**
	 * Moves the current viewport so that the submitted java.awt.Point is as much in the middle of it, as possible.
	 * (This does not change the size of the viewport, only its position.
	 * @param center the point to be centered on
	 */
	public void centerOn(java.awt.Point center) {
		center.x -= this.getViewport().getWidth()/2;
		center.y -= this.getViewport().getHeight()/2;
		this.getViewport().setViewPosition(center);
	}
	
	/**
	 * Returns the coordinates of the center of the current viewport.
	 * @return a java.awt.Point with the coordinates of the center of the current viewpoint
	 */
	public java.awt.Point getCenter() {
		java.awt.Point out = this.getViewport().getViewPosition();
		out.x += this.getViewport().getWidth()/2;
		out.y += this.getViewport().getHeight()/2;
		return out;
	}

}
