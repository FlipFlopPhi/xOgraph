/**
 * 
 */
package ru.flip.xOgraph.actions;

import java.util.List;

/**
 * @author Vizu
 *
 */
public class ListRemoveAction<T> extends AbstractAction {

	private List<T> list;
	private T element;
	private final int[] modifications;
	
	public ListRemoveAction(List<T> list, T element, int[] modifications) {
		this.list = list;
		this.element = element;
		this.modifications = modifications;
	}
	
	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#commit()
	 */
	@Override
	public void commit() {
		list.remove(element);
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#undo()
	 */
	@Override
	public void undo() {
		list.add(element);
	}

	/* (non-Javadoc)
	 * @see ru.flip.xOgraph.actions.AbstractAction#getModifications()
	 */
	@Override
	public int[] getModifications() {
		return modifications;
	}

}
