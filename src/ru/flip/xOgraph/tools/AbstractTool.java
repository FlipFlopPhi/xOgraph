/**
 * 
 */
package ru.flip.xOgraph.tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.Project.Tool;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexUtil;
import ru.flip.xOgraph.ui.canvas.HexCanvas;

/**
 * @author Vizu
 *
 */
public abstract class AbstractTool {

	protected HexCanvas canvas;
	private Icon icon;
	
	public AbstractTool(HexCanvas canvas, String iconURL) {
		this.canvas = canvas;
		icon = loadIcon(iconURL);
	}
	
	public abstract void onClick(MouseEvent first,  MouseEvent last);
	
	/**
	 * Returns the text that is displayed on the small bar at the bottom of the window; 
	 *  this text should display what the user can do with the tool.
	 *  This string should not contain nextline characters.
	 * @return a string that contains instructions for the user on how to use the tool
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the id of this tool.
	 * TODO the usage of the Tool enum, and thus it's dependency on the Project object could and should probably be removed.
	 */
	public abstract Tool getID();
	
	protected Hex getHexClicked(int mouseX, int mouseY) {
		return Project.getMap().getHex(
				HexUtil.getPositionAt(
						mouseX, mouseY, Project.drawDepth, canvas.radius)
				);
	}

	/**
	 * Returns the icon that is used to represent this tool.
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * This method is used for loading tool icons.
	 */
	private Icon loadIcon(String url) {
		try {
			Image image;
			image = ImageIO.read(new File("assets/icons/"+url));
			return new ImageIcon(image.getScaledInstance(18, 18, 0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns this tools tooltip.
	 */
	public abstract String getToolTip();
	
	/**
	 * This method can be used to handle any possible additional drawing actions that need to be
	 *  taken when using this specific tool.
	 */
	public void paint(Graphics g) {}
}
