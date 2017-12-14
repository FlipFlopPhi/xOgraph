/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.IO.MapIO;
import ru.flip.xOgraph.model.Map;

/**
 * @author Vizu
 *
 */
public class FileMenu extends JMenu {

	public FileMenu() {
		super("File");
		
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("xOgraph map", "xmp"));
		
		JMenuItem itemNewMap = new JMenuItem("New");
		itemNewMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Project.setMap( new Map(8,6));
				Project.updateListModels();
				Project.repaint();
			}
		});
		this.add(itemNewMap);
		
		JMenuItem itemOpenMap = new JMenuItem("Open");
		itemOpenMap.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Map map = MapIO.readMap(file);
					Project.setMap(map);
					Project.updateListModels();
					Project.repaint();
				}
			}
		});
		this.add(itemOpenMap);
		
		JMenuItem itemSaveMap = new JMenuItem("Save");
		itemSaveMap.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					MapIO.writeMap(Project.getMap(), file);
				}
			}
		});
		this.add(itemSaveMap);
	}
}
