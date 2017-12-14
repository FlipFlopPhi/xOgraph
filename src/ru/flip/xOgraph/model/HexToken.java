/**
 * 
 */
package ru.flip.xOgraph.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Hextokens represent the type of terrain a Hex has.
 * Sometimes also refered to as color.
 * @author Vizu
 * 
 */
public class HexToken {

	public final static HexToken SeaToken = new HexToken("Water", "waterHex.png",'w');
	public final static HexToken PlainToken = new HexToken("Plain", "plainHex.png",'p');
	public final static HexToken ForestToken = new HexToken("Forest", "forestHex.png",'f');
	public final static HexToken SandToken = new HexToken("Sand", "sandHex.png",'s');
	public final static HexToken MountainToken = new HexToken("Mountain", "mountainHex.png", 'm');
	public final static HexToken HillToken = new HexToken("Hill", "hillHex.png", 'h');
	
	/**
	 * An array including all the standard hextokens.
	 */
	public final static HexToken[] ALL = {SeaToken, PlainToken, ForestToken, SandToken, MountainToken, HillToken};
	
	private String name;
	private Image image;
	private char symbol;
	
	/**
	 * The standard constructor for a hextoken.
	 * @param name The name used to represent this token to the user.
	 * @param url The url used to locate the image that is used for this terrain.
	 * @param symbol The character used to represent this token in save files.
	 */
	public HexToken(String name, String url, char symbol) {
		this.name = name;
		System.out.println();
		try {
			image = ImageIO.read(new File("assets/hexes/"+url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.symbol = symbol;
	}
	
	public String getName() {return name;}
	public Image getImage() {return image;}
	public char getSymbol() {return symbol;}

	/**
	 * Returns the hextoken that uses the same symbol as the parameter.
	 * @param symbol The symbol representing the HexToken we want to retrieve.
	 * @return The HexToken that we want to retrieve.
	 */
	public static HexToken getToken(char symbol) {
		for(HexToken token : ALL) {
			if(token.symbol==symbol)
				return token;
		}
		return null;
	}
	
	
}
