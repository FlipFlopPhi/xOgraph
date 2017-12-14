/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.tools.AbstractTool;

/**
 * @author Vizu
 *
 */
public class XToolBar extends JPanel {

	public XToolBar() {
		ButtonGroup toolButtons = new ButtonGroup();
		
		for (final AbstractTool tool : Project.getTools()) {
			JToggleButton toggleButton = new JToggleButton();
			toggleButton.setBorder(BorderFactory.createEtchedBorder());
			toggleButton.setIcon(tool.getIcon());
			toggleButton.setToolTipText(tool.getToolTip());
			toggleButton.addActionListener( new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Project.setTool(tool.getID());
				}
			});
			toolButtons.add(toggleButton);
			this.add(toggleButton);
		}
	}
}
