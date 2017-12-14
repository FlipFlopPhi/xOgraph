package ru.flip.xOgraph.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import ru.flip.xOgraph.Project;

public class ViewMenuBG extends JMenuItem{

	private JFileChooser fileChooser;
	
	public ViewMenuBG() {
		super("Background Image");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "bmp","png"));
		
		final Component comp = this;
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev)  {
				if (fileChooser.showOpenDialog(comp) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						Project.setBGImage(ImageIO.read(file));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
}
