/**
 * 
 */
package ru.flip.xOgraph.ui.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.ui.DragListener;
import ru.flip.xOgraph.ui.UIUtil;

/**
 * @author Vizu
 *
 */
public class DistanceWindow extends Window {

	private int xOffset = 16;
	private int yOffset = 24;
	
	private double singleHexDistance;
	private double fourHexDistance;
	private double adjacencyDistance;
	private int prettyFourHexDistance;
	private int barLength;
	
	private boolean resizing = true;
	
	public DistanceWindow(Frame owner) {
		super(owner);
		setSize(400, 40);
		setLocation(50, 40);
		
		//Adding the dragListener
		DragListener drag = new DragListener();
		addMouseListener( drag );
		addMouseMotionListener( drag );
		
		//Translucency
		setBackground(Color.darkGray);
		this.setOpacity(0.60f);
		
	}
	
	public void update() {
		resizing = true;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		if (resizing) {
			singleHexDistance = Project.getMap().singleHexDistance;
			fourHexDistance = singleHexDistance * 4d;
			/*The Adjacency-distance is the amount of pixels between the 
				center of 1 hex and its adjacent neighbor */
			adjacencyDistance = (double)Project.canvas.radius *Math.sqrt(3);
			prettyFourHexDistance = getPrettyCeiling(fourHexDistance);
			barLength =  (int) (prettyFourHexDistance/singleHexDistance*adjacencyDistance);
			resizing = false;
			setSize(new Dimension(2*xOffset + barLength + g.getFontMetrics().stringWidth(prettyFourHexDistance+"m")
					,40) 
					);
		}
		g2d.setColor(Color.WHITE);
		
		g2d.drawLine(xOffset, yOffset, xOffset + barLength, yOffset);
		g2d.drawLine(xOffset, yOffset-4, xOffset, yOffset+4);
		for(int n=1; n<=4; n++)
			g2d.drawLine(xOffset + n*(int)Math.round(adjacencyDistance), yOffset
					, xOffset + n*(int)Math.round(adjacencyDistance), yOffset+3);
		g2d.drawLine(xOffset + barLength, yOffset-4, xOffset + barLength, yOffset+4);
		
		//Drawing text
		UIUtil.drawAlignedString(g2d, xOffset + barLength + 4, yOffset, prettyFourHexDistance+"m"
				, UIUtil.HorizontalAlignment.LEFT, UIUtil.VerticalAlignment.CENTER);
		
	}
	
	/**
	 * Finds the closest larger distance that is considered pretty.
	 *  Pretty distances are numbers in the shape of X*10^y, with X= 1,2,3 or 5.
	 */
	private int getPrettyCeiling(double distance) {
		int digits = 0;
		while (distance >= Math.pow(10, digits+1)) {
			digits++;
		}
		if(distance >= 2d*Math.pow(10, digits)) {
			if (distance >= 3d*Math.pow(10, digits)) {
				if (distance >= 5d*Math.pow(10, digits))
					return (int) (Math.pow(10, digits+1));
				else
					return (int) (5*Math.pow(10, digits));
			} else
				return (int) (3*Math.pow(10, digits));
		} else
			return (int) (2*Math.pow(10, digits));
	}
}
