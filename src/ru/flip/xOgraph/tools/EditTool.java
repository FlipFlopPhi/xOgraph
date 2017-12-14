package ru.flip.xOgraph.tools;

import java.awt.event.MouseEvent;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.ui.CanvasPopUpMenu;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The edittool is used for selecting and deselecting hexes and editing objects on the map.
 * @author Vizu
 *
 */
public class EditTool extends AbstractTool {
	

	
	private CanvasPopUpMenu context;
	
	private Hex hex;
	
	public EditTool(HexCanvas canvas) {
		super(canvas, "selectionIcon.png");
		
		context = new CanvasPopUpMenu() ;
		
	}

	@Override
	public void onClick(MouseEvent me, MouseEvent me2) {
		int mouseX = me.getX();
		int mouseY = me.getY();
		
		hex = getHexClicked(mouseX, mouseY);
		if (hex==null)
			return;
		switch(me.getButton()) {
		case MouseEvent.BUTTON1:
			if (hex.selected)
				Project.deselect(hex);
			else
				Project.select(hex);
			break;
		case MouseEvent.BUTTON3:
			context.hexClicked = hex;
			context.show(canvas, mouseX, mouseY);
			break;
		case MouseEvent.BUTTON2:
			break;
		default:
			break;
		}
	}

	@Override
	public String getDescription() {
		return "Left click to add/remove a hex to/from the selection. Right click to show options.";
	}

	@Override
	public Tool getID() {
		return Tool.EDIT;
	}

	@Override
	public String getToolTip() {
		return "Paint";
	}

}
