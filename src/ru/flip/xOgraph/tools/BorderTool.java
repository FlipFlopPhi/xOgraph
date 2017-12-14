/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.actions.BorderAction;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexUtil;
import ru.flip.xOgraph.model.Position;
import ru.flip.xOgraph.model.Region;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * The bordertool is used for drawing and editing regions on the map.
 * @author Vizu
 *
 */
public class BorderTool extends AbstractTool {

	private Region currentBorder;

	/**
	 * @param canvas
	 */
	public BorderTool(HexCanvas canvas) {
		super(canvas, "regionIcon.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.flip.xOgraph.tools.AbstractTool#onClick(java.awt.event.MouseEvent,
	 * java.awt.event.MouseEvent)
	 */
	@Override
	public void onClick(MouseEvent first, MouseEvent last) {
		// TODO refactor and improve code
		int mouseX = first.getX();
		int mouseY = first.getY();

		Position position = HexUtil.getPositionAt(mouseX, mouseY, Project.drawDepth, canvas.radius);

		if (position == null)
			return;
		switch (first.getButton()) {
		case MouseEvent.BUTTON1:
			if (first.isControlDown()) {
				if (Project.selectedRegion != null && Project.selectedRegion.contains(position)) {
					Position to = HexUtil.getPositionAt(last.getX(), last.getY(), Project.drawDepth, canvas.radius);
					Project.selectedRegion.disLocate(position, to);
				}
				return;
			}
			Hex hex = Project.getMap().getHex(position);
			if (hex == null)
				return;
			if (currentBorder == null) {
				currentBorder = new Region(hex.getPosition());
				return;
			}
			if (currentBorder.corners.get(0) == hex.getPosition()) {
				Project.commit(new BorderAction(currentBorder));
				currentBorder = null;
				return;
			}
			if (!currentBorder.contains(hex.getPosition())) {
				currentBorder.extend(hex.getPosition());
			}
			break;
		case MouseEvent.BUTTON3:
			if (first.isControlDown()) {
				if (Project.selectedRegion != null) {

					// Project.selectedRegion.removePoint(point);
				}
			}
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.tools.AbstractTool#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Left click to start drawing a region border. Left click on a corner to remove it, Left click on the first corner to finish drawing a region border.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.tools.AbstractTool#getID()
	 */
	@Override
	public Tool getID() {
		return Tool.BORDER;
	}

	@Override
	public String getToolTip() {
		return "Region edit Tool";
	}

	@Override
	public void paint(Graphics g) {
		if (Project.selectedRegion != null) {
			for (Position point : Project.selectedRegion.corners) {
				g.drawOval(point.getCanX(canvas.radius), point.getCanY(canvas.radius), 32, 32);
			}
		}
		if (currentBorder == null)
			return;
		int borderSize = currentBorder.getBorderLength();
		int[] xs = new int[borderSize];
		int[] ys = new int[borderSize];
		for (int i = 0; i < borderSize; i++) {
			Position position = currentBorder.get(i);
			xs[i] = position.getCanX(canvas.radius);
			ys[i] = position.getCanY(canvas.radius);
		}
		g.setColor(new Color(0xf, 0xf, 0xf, 55));
		g.fillPolygon(xs, ys, borderSize);
		g.setColor(Color.BLACK);
	}

}
