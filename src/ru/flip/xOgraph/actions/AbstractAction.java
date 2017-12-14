/**
 * 
 */
package ru.flip.xOgraph.actions;

/**
 * Action objects are used to represent and execute changes to the current map
 * model. Actions are saved so the user can revert his/her alteration if he/she
 * is not contempt with it. Actions are always executed in sequential order.
 * 
 * @author Vizu
 *
 */
public abstract class AbstractAction {

	/**
	 * Commits this action.
	 */
	public abstract void commit();

	/**
	 * Makes this action undone. Note that simply executing the undo command
	 * does not check whether the action has also been commiting. So when used
	 * incorrectly this can cause the action to undo another actions work.
	 */
	public abstract void undo();
}
