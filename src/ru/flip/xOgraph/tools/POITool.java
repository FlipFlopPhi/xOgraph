/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.actions.ListRemoveAction;
import ru.flip.xOgraph.actions.LocationAction;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.Location;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Position;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The PoItool is used to add, edit and remove points of interest on the map.
 * 
 * @author Vizu
 *
 */
public class POITool extends AbstractTool {

	public POITool(HexCanvas canvas) {
		super(canvas, "POIIcon.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.tools.AbstractTool#onClick(java.awt.event.MouseEvent)
	 */
	@Override
	public void onClick(MouseEvent me, MouseEvent me2) {
		int mouseX = me.getX();
		int mouseY = me.getY();
		Hex hex = getHexClicked(mouseX, mouseY);
		if (hex == null)
			return;
		Position position = hex.getPosition();
		Location location = null;
		for (Location locationFromList : Project.getMap().points) {
			if (locationFromList.getPosition().equals(position)) {
				location = locationFromList;
				break;
			}
		}
		switch (me.getButton()) {
		case MouseEvent.BUTTON1:
			if (location == null) {
				Project.commit(new LocationAction(new Location(position)));
			} else {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to remove this \n  point of interest?",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					Project.commit(new ListRemoveAction<Location>(Project.getMap().points, location,
							new int[] { Map.MODIFIED_LOCATIONS }));
				}
			}

			break;
		case MouseEvent.BUTTON2:
			break;
		case MouseEvent.BUTTON3:

			if (location != null)
				Project.locationOptions.showDialog(location);
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
