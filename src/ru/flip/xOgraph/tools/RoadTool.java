/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.event.MouseEvent;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.actions.LineAction;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The roadtool is used to add, edit and remove roads on the map.
 * @author Vizu
 *
 */
public class RoadTool extends AbstractTool {

	/**
	 * @param canvas
	 */
	public RoadTool(HexCanvas canvas) {
		super(canvas, "selectionIcon.png");
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#onClick(java.awt.event.MouseEvent, java.awt.event.MouseEvent)
	 */
	@Override
	public void onClick(MouseEvent first, MouseEvent last) {
		Hex hex1 = getHexClicked(first.getX(), first.getY());
		Hex hex2 = getHexClicked(last.getX(), last.getY());
		if (hex1 == null | hex2 == null | hex1 == hex2 )
			return;
		Project.commit(new LineAction(hex1, hex2));
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Drag a line to create a road";
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#getID()
	 */
	@Override
	public Tool getID() {
		return Project.Tool.ROAD;
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return "Road Tool";
	}

}
