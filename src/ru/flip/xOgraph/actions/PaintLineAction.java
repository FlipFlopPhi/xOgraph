/**
 * 
 */
package ru.flip.xOgraph.actions;

import java.util.LinkedList;
import java.util.List;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexToken;
import ru.flip.xOgraph.model.Position;

/**
 * This action will paint all hexes on a given line.
 * @author Vizu
 */
public class PaintLineAction extends AbstractAction {

	private class Target {
		private final HexToken oldColor;
		private final Hex hex;
		
		private Target(Hex hex) {
			this.hex = hex;
			oldColor = hex.token;
		}
	}
	
	private List<Target> targets;
	private HexToken newColor;
	
	/**
	 * This action will paint all the hexes on the given line into the given color when commited
	 * @param line the line on which all hexes are repainted
	 * @param selectedToken the new color for all the hexes on the line
	 */
	public PaintLineAction(List<Position> line, HexToken selectedToken) {
		targets = new LinkedList<>();
		lineLoop:
		for(Position position : line) {
			Hex hex = Project.getMap().getHex(position);
			for (Target target : targets) {
				if (target.hex == hex)
					continue lineLoop;
			}
			targets.add(new Target(hex));
		}
		newColor = selectedToken;
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		for(Target target : targets) {
			target.hex.token = newColor;
		}
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		for(Target target : targets) {
			target.hex.token = target.oldColor;
		}
	}

}
