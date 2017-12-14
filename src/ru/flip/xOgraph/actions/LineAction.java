/**
 * 
 */
package ru.flip.xOgraph.actions;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.Line;

/**
 * This action adds a line to the map.
 * @author Vizu
 */
public class LineAction extends AbstractAction {

	private Line line;
	
	/**
	 * This action will add a line (from hex1 to hex2) to the map when commited.
	 * @param hex1 the begin of the line
	 * @param hex2 the end of the line
	 */
	public LineAction(Hex hex1, Hex hex2) {
		line = new Line(hex1.getPosition(), hex2.getPosition());
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		Project.getMap().lines.add(line);
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		Project.getMap().lines.remove(line);
	}

}
