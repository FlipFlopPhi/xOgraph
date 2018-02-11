/**
 * 
 */
package ru.flip.xOgraph.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Vizu
 *
 */
public class Location {

	private Position position;
	private Image image;
	public String name = "";
	
	public Location(Position position) {
		setImage(new File("assets/icons/POIIcon.png"));
	}
	
	public void setImage(File url) {
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Image getImage() {
		return image;
	}

	public Position getPosition() {
		return position;
	}

}
