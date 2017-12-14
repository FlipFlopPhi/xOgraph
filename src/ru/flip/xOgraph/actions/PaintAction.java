/**
 * 
 */
package ru.flip.xOgraph.actions;

import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexToken;

/**
 * This action recolors one single hex.
 * @author Vizu
 */
public class PaintAction extends AbstractAction {

	private Hex target;
	private HexToken oldToken;
	private HexToken newToken;

	/**
	 * This action will recolor the given hex with the given color when committed.
	 * @param hex the to-be recolored hex
	 * @param token the hex' new color
	 */
	public PaintAction(Hex hex, HexToken token) {
		target = hex;
		oldToken = target.token;
		newToken = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		target.token = newToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		target.token = oldToken;
	}

}
