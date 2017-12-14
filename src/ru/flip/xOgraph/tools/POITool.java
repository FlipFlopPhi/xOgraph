/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The PoItool is used to add, edit and remove points of interest on the map.
 * @author Vizu
 *
 */
public class POITool extends AbstractTool {

	public POITool(HexCanvas canvas) {
		super(canvas, "POIIcon.png");
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#onClick(java.awt.event.MouseEvent)
	 */
	@Override
	public void onClick(MouseEvent me, MouseEvent me2) {
		int mouseX = me.getX();
		int mouseY = me.getY();
		Hex hex = getHexClicked(mouseX, mouseY);
		
		switch(me.getButton()) {
		case MouseEvent.BUTTON1 :
			if (hex.getPOI() == null) {
				Project.addPoint(hex);
			} else {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove this \n  point of interest?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			    if (response == JOptionPane.YES_OPTION) {
			    	Project.removePoint(hex);
			    }
			}
			
			break;
		case MouseEvent.BUTTON2 :
			break;
		case MouseEvent.BUTTON3 :
			if (hex.getPOI() != null)
				Project.locationOptions.showDialog(hex.getPOI());
			break;
		default:
			break;
		}
	}

	@Override
	public String getDescription() {
		return "Left click a tile to add a Location to it, left click again to remove one. Right click to edit it.";
	}

	@Override
	public Tool getID() {
		return Tool.POI;
	}

	@Override
	public String getToolTip() {
		return "Location Tool";
	}

}
