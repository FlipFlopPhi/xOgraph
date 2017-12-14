/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * @author Vizu
 *
 */
public class UIUtil {

	public static enum VerticalAlignment {TOP, CENTER, BOTTOM};
	public static enum HorizontalAlignment {LEFT, CENTER, RIGHT};
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	public static void drawCenteredString(Graphics g, int x, int y, String text) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
	    // Determine the X coordinate for the text
	    int centerX = x - metrics.stringWidth(text)/2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int centerY = y - metrics.getHeight()/2 + metrics.getAscent();
	    // Draw the String
	    g.setColor(Color.GRAY);
	    g.fillRect(centerX, centerY - metrics.getAscent()
	    		,metrics.stringWidth(text),  metrics.getHeight());
	    g.setColor(Color.BLACK);
	    g.drawString(text, centerX, centerY);
	}
	

	public static void drawAlignedString(Graphics g, int x, int y, String text, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		// Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
	    // Determine the X coordinate for the text
	    switch(horizontalAlignment) {
		case LEFT:
			break;
		case RIGHT:
			x -= metrics.stringWidth(text);
			break;
		case CENTER:
			 x -= metrics.stringWidth(text)/2;
			break;
	    }
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    switch(verticalAlignment) {
		case BOTTOM:
			break;
		case TOP:
			y -= metrics.getHeight() - metrics.getAscent();
			break;
		case CENTER:
			y -= metrics.getHeight()/2 - metrics.getAscent();
			break;
	    }
	    // Draw the String
	    g.drawString(text, x, y);
	}
}
