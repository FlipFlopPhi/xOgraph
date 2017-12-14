/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.event.MouseEvent;
import java.util.List;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.actions.PaintAction;
import ru.flip.xOgraph.actions.PaintLineAction;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexUtil;
import ru.flip.xOgraph.model.Position;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The painttool is used to color in hexes on the map.
 * @author Vizu
 *
 */
public class PaintTool extends AbstractTool{

	public PaintTool(HexCanvas canvas) {
		super(canvas, "paintIcon.png");
	}

	@Override
	public void onClick(MouseEvent me, MouseEvent me2) {
		int mouseX = me.getX();
		int mouseY = me.getY();
		
		Hex hex = getHexClicked(mouseX, mouseY);
		if (hex == null)
			return;
		switch(me.getButton()) {
		case MouseEvent.BUTTON1:
			if (me.isShiftDown()) {
				Hex hex2 = getHexClicked(me2.getX(), me2.getY());
				List<Position> line = HexUtil.getLine(hex.getPosition(), hex2.getPosition());
				line.add(hex2.getPosition());
				Project.commit(new PaintLineAction(line, Project.selectedToken));
				break;
			}
			
			if (Project.selectedToken == hex.token ) {
				if (hex.token !=null)
					Project.commit( new PaintAction(hex, null));
			} else
				Project.commit( new PaintAction(hex, Project.selectedToken));
			break;
		case MouseEvent.BUTTON3:
			break;
		default:
			break;
		}
	}

	@Override
	public String getDescription() {
		return "Left click to paint a tile with the selected hex. Left click again to clear a tile. Shift+Left drag to draw a straight line.";
	}

	@Override
	public Tool getID() {
		return Tool.PAINT;
	}

	@Override
	public String getToolTip() {
		return "Draw";
	}

}
