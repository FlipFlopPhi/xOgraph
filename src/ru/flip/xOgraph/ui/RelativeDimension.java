/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Vizu
 *
 */
public class RelativeDimension extends Dimension{

	private Component parent;
	private float wRatio;
	private float hRatio;
	
	public RelativeDimension(Component parent, float wRatio, float hRatio) {
		this.parent = parent;
		this.wRatio = wRatio;
		this.hRatio = hRatio;
		
		super.width = (int)getWidth();
		super.height = (int)getHeight();
	}
	
	@Override
	public double getWidth() {
		return parent.getWidth() * wRatio;
	}
	
	@Override
	public double getHeight() {
		return parent.getHeight() * hRatio;
	}
	
	
}
