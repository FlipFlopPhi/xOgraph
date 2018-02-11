/**
 * 
 */
package ru.flip.xOgraph.actions;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Location;
import ru.flip.xOgraph.model.Map;

/**
 * @author Vizu
 *
 */
public class LocationAction extends AbstractAction {

	private Location location;
	private final int[] modifies = new int[] {Map.MODIFIED_LOCATIONS};
	
	public LocationAction(Location location) {
		this.location = location;
	}
	
	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		Project.getMap().points.add(location);
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		Project.getMap().points.remove(location);
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#getModifications()
	 */
	@Override
	public int[] getModifications() {
		return modifies;
	}

}
