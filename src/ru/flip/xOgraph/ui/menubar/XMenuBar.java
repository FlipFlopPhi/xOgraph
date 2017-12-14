/**
 * 
 */
package ru.flip.xOgraph.ui.menubar;

import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.actions.HexDistanceChangeAction;
import ru.flip.xOgraph.actions.ResizeAction;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.ui.FileMenu;
import ru.flip.xOgraph.ui.ViewMenuBG;

/**
 * @author Vizu
 *
 */
@SuppressWarnings("serial")
public class XMenuBar extends JMenuBar {

	// File Menu Items

	// Map Menu Items

	// View Menu Items

	public XMenuBar() {
		super();

		this.add(new FileMenu());

		// Map Menu
		createMapMenu(this);
		// View Menu
		JMenu viewMenu = new JMenu("View");
		viewMenu.add(new ViewMenuBG());
		JMenuItem itemClearImage = new JMenuItem("Clear Image");
		itemClearImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Project.bgImage == null)
					return;
				Project.bgImage.flush();
				Project.bgImage = null;
				Project.repaint();
			}
		});
		viewMenu.add(itemClearImage);

		final JRadioButton imageToggle = new JRadioButton("Show image on foreground") {

			@Override
			public boolean isEnabled() {
				return (Project.bgImage != null);
			}
		};
		imageToggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Project.imageOnForeground = imageToggle.isSelected();
				Project.repaint();
			}
		});
		viewMenu.add(imageToggle);
		this.add(viewMenu);
		JMenu windowMenu = new JMenu("Window");
		JRadioButton distanceWindowToggle = new JRadioButton("Distance Graph");
		distanceWindowToggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Project.distanceWindow.setVisible(!Project.distanceWindow.isVisible());
			}
		});
		windowMenu.add(distanceWindowToggle);
		this.add(windowMenu);
		// Zoom
		this.add(Project.zoomPanel);
		JButton undoButton = new JButton("<") {

			@Override
			public boolean isEnabled() {
				return Project.getIterator() > 0;
			}
		};
		undoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Project.undo();
				Project.repaint();
			}
		});
		this.add(undoButton);
		JButton redoButton = new JButton(">") {

			@Override
			public boolean isEnabled() {
				return Project.canRedo();
			}
		};
		redoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Project.redo();
				Project.repaint();
			}
		});
		this.add(redoButton);
		this.add(new JLabel("Draw grid: "));
		final JRadioButton gridRadio = new JRadioButton();
		gridRadio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Project.drawGrid = gridRadio.isSelected();
				Project.repaint();
			}
		});
		gridRadio.setSelected(true);
		this.add(gridRadio);

		// BorderOptions
		final JSpinner depthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
		depthSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				Project.changeDrawDepth((Integer) depthSpinner.getValue());
			}
		});
		this.add(depthSpinner);
	}

	private void createMapMenu(JMenuBar menu) {
		
		
		
		JMenu mapMenu = new JMenu("Map");

		JMenuItem itemSetDistance = new JMenuItem("Change distance");
		itemSetDistance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newSingleHexDistance = JOptionPane.showInputDialog( Project.frame
						,"Give the distance between two adjacent hexes on depth 0 in meters:"
						, Project.getMap().singleHexDistance);
				if (newSingleHexDistance == null)
					return;
				try {
					double singleHexDistance = Double.parseDouble(newSingleHexDistance);
					Project.commit(new HexDistanceChangeAction(singleHexDistance));
					Project.distanceWindow.repaint();
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog( Project.frame
							, "The submitted value could not be parsed \n As a result the single-hex-distance has not been altered."
							, "Parsing Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mapMenu.add(itemSetDistance);
		
		mapMenu.addSeparator(); // ===================================
		
		JMenuItem itemRescale = new JMenuItem("Rescale");
		itemRescale.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double scale = Double.parseDouble(JOptionPane.showInputDialog("Scale:", "1"));
				Map map = Project.getMap();
				map.resize((int) Math.ceil(map.getWidth() * scale), (int) Math.ceil(map.getHeight() * scale));
				Project.repaint();
			}
		});
		mapMenu.add(itemRescale);

		mapMenu.addSeparator(); // ===================================

		JMenuItem itemResize = new JMenuItem("Resize");
		itemResize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Allow the user to chane the maps size to a new size.
			}
		});
		mapMenu.add(itemResize);

		JMenuItem itemAddColumn = new JMenuItem("Add Column");
		itemAddColumn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Map map = Project.getMap();
				Project.commit(new ResizeAction(map.getWidth() + 1, map.getHeight()));
				Project.repaint();
			}
		});
		mapMenu.add(itemAddColumn);

		JMenuItem itemRemoveColumn = new JMenuItem("Remove Column");
		itemRemoveColumn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Map map = Project.getMap();
				if (map.getWidth() > 1) {
					Project.commit(new ResizeAction(map.getWidth() - 1, map.getHeight()));
					Project.repaint();
				}
			}
		});
		mapMenu.add(itemRemoveColumn);

		JMenuItem itemAddRow = new JMenuItem("Add Row");
		itemAddRow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Map map = Project.getMap();
				Project.commit(new ResizeAction(map.getWidth(), map.getHeight() + 1));
				Project.repaint();
			}
		});
		mapMenu.add(itemAddRow);

		JMenuItem itemRemoveRow = new JMenuItem("Remove Row");
		itemRemoveRow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Map map = Project.getMap();
				if (map.getHeight() > 1) {
					Project.commit(new ResizeAction(map.getWidth(), map.getHeight() - 1));
					Project.repaint();
				}
			}
		});

		mapMenu.add(itemRemoveRow);
		menu.add(mapMenu);
	}

}
