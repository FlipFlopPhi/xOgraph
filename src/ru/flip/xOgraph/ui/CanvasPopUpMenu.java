/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.Region;

/**
 * @author Vizu
 *
 */
public class CanvasPopUpMenu extends JPopupMenu {
	
	private List<Region> regionsClicked;
	public Hex hexClicked;
	private JMenu regionMenu;
	
	
	public CanvasPopUpMenu() {
		regionsClicked = new LinkedList<>();
		
		JMenuItem zoomItem = new JMenuItem("Zoom");
		zoomItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!hexClicked.isEnhanced())
					hexClicked.enhance();
				Project.repaint();
			}
		});
		this.add(zoomItem);
		JMenuItem zoomAllItem = new JMenuItem("Zoom Selection");
		zoomAllItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				while(!Project.selection.isEmpty()) {
					Hex hex = Project.selection.get(0);
					hex.enhance();
					Project.deselect(hex);
				}
				Project.repaint();
			}
		});
		this.add(zoomAllItem);
		JMenuItem dezoomItem = new JMenuItem("Dezoom");
		dezoomItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Hex parent = hexClicked.getParent();
				if (parent != null) {
					parent.dehance();
				}
				Project.repaint();
			}
		});
		this.add(dezoomItem);
		this.addSeparator();
		JMenuItem editPoiItem = new JMenuItem("Edit POI") {
			@Override
			public boolean isEnabled() {
				return (hexClicked != null && hexClicked.getPOI() != null);
			}
		};
		editPoiItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Project.locationOptions.showDialog(hexClicked.getPOI());
			}
		});
		this.add(editPoiItem);
		this.addSeparator();
		regionMenu = new JMenu("Regions") {
			@Override
			public boolean isEnabled() {
				return (regionsClicked.size() != 0);
			}
		};
		this.add(regionMenu);
		this.addSeparator();
		JMenuItem clearSelItem = new JMenuItem("Clear Selection") {
			
			@Override
			public boolean isEnabled() {
				return Project.selection.size() != 0;
			}
		};
		clearSelItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Project.clearSelection();
				Project.repaint();
			}
		});
		this.add(clearSelItem);
	}
	
	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
		regionsClicked.clear();
		for(Region region: Project.getMap().regions) {
			if (region.toPolygon(Project.canvas.radius).contains(x, y)) {
				regionsClicked.add(region);
			}
		}
		regionMenu.removeAll();
		for(final Region region : regionsClicked) {
			JMenuItem menuItem = new JMenuItem(region.name);
			menuItem.addActionListener( new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Project.regionOptions.showDialog(region);
				}
			});
			regionMenu.add(menuItem);
		}
		
	}

}
