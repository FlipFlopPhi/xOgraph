/**
 * 
 */
package ru.flip.xOgraph.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import ru.flip.xOgraph.IO.segmentParsers.LineParser;
import ru.flip.xOgraph.IO.segmentParsers.LocationParser;
import ru.flip.xOgraph.IO.segmentParsers.RegionParser;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexToken;
import ru.flip.xOgraph.model.Map;


/**
 * This class is used to take care of saving and loading maps.
 * Maps are stored in a .xmp file. .xmp files use plaintext to represent everything.
 * The syntax used for .xmp files can be found at the git page.
 * TODO put the syntax up on git.
 * @author Vizu
 *
 */
public class MapIO {

	public static final String VERSION = "1.0.0";
	
	private static final ParserBank locationBank = new ParserBank("Core:Location");
	private static final ParserBank regionBank = new ParserBank("Core:Region");
	private static final ParserBank lineBank = new ParserBank("Core:Line");
	private static final ParserBank distanceBank = new ParserBank("Core:Distance");
	
	static {
		locationBank.addSegment(LocationParser.v100);
		
		regionBank.addSegment(RegionParser.v1000);
		
		lineBank.addSegment(LineParser.v100);
	}
	
	public static final List<ParserBank> parserBanks = new LinkedList<>();
	
	static {
		parserBanks.add(locationBank);
		parserBanks.add(regionBank);
		parserBanks.add(lineBank);
		parserBanks.add(distanceBank);
	}
	
	/**
	 * Writes the submitted map to the target file-location.
	 * @param map the to be saved map
	 * @param file the file location at which the map is stored
	 */
	public static void writeMap(Map map, File file) {
		try {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			PrintWriter writer = new PrintWriter(file);
			//Writing standard Map information
			writer.write(map.getWidth()+","+map.getHeight() + '\n');
			writer.write(map.singleHexDistance +"\n");
			//Writing Hexes
			writer.write("==\n");
			for(int x=0; x<map.getWidth(); x++) {
				for(int y=0; y<map.getHeight(); y++) {
					Hex hex = map.getHex(x, y);
					writeHex(hex, writer);
				}
				writer.write('\n');
			}
			
			//Locations
			SegmentParser locationParser = locationBank.getNewest();
			locationParser.writeSegment(map.points, writer);
			
			//Regions
			SegmentParser regionParser = regionBank.getNewest();
			regionParser.writeSegment(map.regions, writer);
			
			//Lines
			SegmentParser lineParser = lineBank.getNewest();
			lineParser.writeSegment(map.lines, writer);
			
			writer.close();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void writeHex(Hex hex, PrintWriter writer) {
		if (hex.isEnhanced()) {
			writer.write('(');
			if (hex.token != null) {
				writer.write(hex.token.getSymbol());
			} else
				writer.write('.');
			for(int i=0; i<7; i++) {
				writeHex(hex.getChild(i), writer);
			}
			writer.write(')');
		} else if (hex.token != null) {
			writer.write(hex.token.getSymbol());
		} else
			writer.write('.');
		
	}

	/**
	 * Reads a .xmp file from target file location and translates it into a map object.
	 * @param file the target file location
	 * @return the map that was saved at the file location
	 */
	public static Map readMap(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			List<String> content = new LinkedList<String>();
			while (reader.ready()) {
				String line = reader.readLine();
				content.add(line);
			}
			reader.close();
			fr.close();
			
			List<List<String>> subContents 
				= new LinkedList<List<String>>();
			subContents.add(new LinkedList<String>());
			int i=0;
			for(String line : content) {
				if (line.matches("==")) {
					subContents.add(new LinkedList<String>());
					i++;
				} else {
					subContents.get(i).add(line);
				}
			}
			
			Map map = createMap(subContents.remove(0));
			
			readHexes(map, subContents.remove(0));
			
			//Parse segments
			for(List<String> subContent : subContents) {
				String[] segmentValues = subContent.remove(0).split(";");
				for(ParserBank parserBank : parserBanks) {
					if (parserBank.id.matches(segmentValues[0])) {
						System.out.println(segmentValues[1]);
						String[] versionString = segmentValues[1].split("\\.");
						int[] version = new int[versionString.length];
						for (int j=0; j < versionString.length; j ++) {
							version[j] = Integer.parseInt(versionString[j]);
						}
						parserBank.getParser(version).readSegment(map, subContent);
						break;
					}
				}
			}
			return map;
		} catch (IOException e) {
			
		}
		return new Map(1,1);
	}



	private static Map createMap(List<String> content) {
		String size = content.remove(0);
		String[] coordinates = size.split(",");
		Map map = new Map(Integer.parseInt(coordinates[0])
				, Integer.parseInt(coordinates[1])
				);
		map.singleHexDistance = Double.parseDouble(content.remove(0));
		return map;
	}

	private static void readHexes(Map map, List<String> content) {
		int col = 0;
		while(!content.isEmpty() & col<map.getWidth()) {
			String line = content.remove(0);
			for(int row=0; line.length()!=0 & row<map.getHeight(); row++) {
				char symbol = line.charAt(0);
				if (symbol == '(') {
					line = readHex(map.getHex(col, row), line.substring(1));
				} else {
					map.getHex(col, row).token = HexToken.getToken(symbol);
					line = line.substring(1);
				}
			}
			col++;
		}
	}

	/**
	 * Parses the enhanced hex described at target location
	 * @param hex the hex that is enhanced
	 * @param line the line describing the hex so: ('t01234567)...'
	 * @return the part of the line that does not contain this hex. The '...' part
	 */
	private static String readHex(Hex hex, String line) {
		hex.enhance();
		char ownSymbol = line.charAt(0);
		hex.token = HexToken.getToken(ownSymbol);
		String out = line.substring(1);
		int i=0;
		while(out.charAt(0)!=')') {
			char symbol = out.charAt(0);
			if (symbol == '(') {
				out = readHex(hex.getChild(i), out.substring(1));
			} else {
				hex.getChild(i).token = HexToken.getToken(symbol);
				out = out.substring(1);
			}
			i++;
			
		}
		return out.substring(1);
	}
}
