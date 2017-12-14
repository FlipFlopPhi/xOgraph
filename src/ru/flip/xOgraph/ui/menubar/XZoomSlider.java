/**
 * 
 */
package ru.flip.xOgraph.ui.menubar;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.flip.xOgraph.Project;

/**
 * @author Vizu
 * TODO this class should only concern the interactivity of the zoomslider itself, anything changing the viewport should be moved to ScrollCanvas itself.
 */
public class XZoomSlider extends JPanel{

	private boolean adjustScale = true;
	private final JSpinner scaleSpinner;
	
	/**
	 * 
	 */
	public XZoomSlider() {
		super();
		this.setMaximumSize(new Dimension(120, 32));
		this.setBorder(BorderFactory.createEtchedBorder());
		JLabel zoomText = new JLabel("Zoom: ");
		this.add(zoomText);
		scaleSpinner = new JSpinner();
		scaleSpinner.getModel().setValue(Integer.valueOf(100));
		scaleSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (!adjustScale)
					return;
				Point point = Project.scrollCanvas.getCenter();
				double oldScale = Project.scale;
				Project.scale = (double)(Integer)scaleSpinner.getValue() / 100d;
				point.x *= Project.scale/oldScale;
				point.y *= Project.scale/oldScale;
				Project.scrollCanvas.centerOn(point);
				Project.repaint();
			}
		});
		this.add(scaleSpinner);
		
	}
	
	/**
	 * Changes the value, without adjusting the projects scale.
	 * @param value the new value of the slider.
	 */
	public void changeValue(int value) {
		adjustScale = false;
		scaleSpinner.setValue(value);
		adjustScale = true;
	}

}
