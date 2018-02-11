/**
 * 
 */
package ru.flip.xOgraph.actions;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Map;

/**
 * This action is used to resize the map. The action will try to retain as much from the old map as possible.
 * @author Vizu
 */
public class ResizeAction extends AbstractAction {

	private Map oldMap;
	private Map newMap;
	
	/**
	 * When committed, this action will create a new map with the given dimensions, while trying to retain as much from the old map as possible.
	 * @param newCol
	 * @param newRow
	 */
	public ResizeAction(int newCol, int newRow) {
		oldMap = Project.getMap();
		newMap = new Map(oldMap, newCol, newRow);
	}
	
	@Override
	public void commit() {
		Project.setMap(newMap);
	}

	@Override
	public void undo() {
		Project.setMap(oldMap);
	}

	@Override
	public int[] getModifications() {
		return new int[] {Map.MODIFIED_ALL};
	}
	

}
