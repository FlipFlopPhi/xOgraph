package ru.flip.xOgraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ru.flip.xOgraph.ui.canvas.HexCanvas;


public class MapListener implements MouseListener {
	
	private MouseEvent entered;
	
	
	
	public MapListener(HexCanvas map) {
		
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		entered = me;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		Project.currentTool.onClick(entered, me);
	}

	
}
