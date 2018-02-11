/**
 * 
 */
package ru.flip.xOgraph.actions;

import java.util.LinkedList;
import java.util.List;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexToken;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Shift;

/**
 * This action colors an area of adjacent and matching hexes.
 * @author Vizu
 */
public class FillAction extends AbstractAction {

	private List<Hex> targets;
	private HexToken oldColor;
	private HexToken newColor;

	/**
	 * This action will color the area of all connected hexes with the same color as the origin when commited.
	 * @param origin the area's origin
	 * @param token the area's new color
	 */
	public FillAction(Hex origin, HexToken token) {
		oldColor = origin.token;
		newColor = token;
		targets = new LinkedList<Hex>();
		targets.add(origin);
		for (int i = 0; i < targets.size(); i++) {
			Hex hex = targets.get(i);
			for (Shift shift : Shift.values()) {
				Hex neighbour = Project.getMap().getHex(hex.getPosition().createNeighbour(shift));
				if (neighbour == null || neighbour.token != oldColor)
					continue;
				if (!targets.contains(neighbour))
					targets.add(neighbour);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		for (Hex target : targets)
			target.token = newColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		for (Hex target : targets)
			target.token = oldColor;
	}

	@Override
	public int[] getModifications() {
		return new int[] {Map.MODIFIED_HEXES};
	}

}
