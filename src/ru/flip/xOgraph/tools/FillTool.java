/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.event.MouseEvent;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.actions.FillAction;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The fillTool is used to fill entire areas with the same color, similar to the paintbucket in paint.
 * @author Vizu
 *
 */
public class FillTool extends AbstractTool {

	public FillTool(HexCanvas canvas) {
		super(canvas, "fillIcon.png");
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#onClick(java.awt.event.MouseEvent, java.awt.event.MouseEvent)
	 */
	@Override
	public void onClick(MouseEvent me, MouseEvent me2) {
		int mouseX = me.getX();
		int mouseY = me.getY();
		
		Hex hex = getHexClicked(mouseX, mouseY);
		if (hex == null)
			return;
		switch(me.getButton()) {
		case MouseEvent.BUTTON1:
			if (Project.selectedToken != null)
				Project.commit( new FillAction(hex, Project.selectedToken));
			break;
		case MouseEvent.BUTTON3:
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Left click to fill an area with the selected tile.";
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.tools.AbstractTool#getID()
	 */
	@Override
	public Tool getID() {
		return Tool.FILL;
	}

	@Override
	public String getToolTip() {
		return "Fill bucket";
	}

}
