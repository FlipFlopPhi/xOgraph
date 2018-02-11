/**
 * 
 */
package ru.flip.xOgraph.actions;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Region;

/**
 * The action used to add a new Region to the map.
 * 
 * @author Vizu
 *
 */
public class RegionAction extends AbstractAction {

	
	private Region region;
	private static int regionNr = 0;

	/**
	 * This action will add the region to the map when commited.
	 * @param currentBorder the region that is added to the map
	 */
	public RegionAction(Region currentBorder) {
		region = currentBorder;
		region.name = "Region " + regionNr;
		regionNr++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		Project.getMap().regions.add(region);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		Project.getMap().regions.remove(region);
	}
	
	@Override
	public int[] getModifications() {
		return new int[] {Map.MODIFIED_REGION};
	}	

}
