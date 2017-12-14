/**
 * 
 */
package ru.flip.xOgraph.IO;

import java.util.LinkedList;
import java.util.List;

/**
 * Parserbanks are used to store and keep track of different versions of a segmentParser.
 * @author Vizu
 *
 */
public class ParserBank {

	private final List<SegmentParser> parsers;
	
	/**
	 * The ID is the identifier used for the type of segment that this bank and its parsers are made to parse.
	 */
	public final String id;
	
	/**
	 * Constructs a ParserBank that should keep track of different versions 
	 *  of segmentParsers made for parsing segments of the target id.
	 * @param id The ID is the identifier used for the type of segment that 
	 * this bank and its parsers are made to parse
	 */
	public ParserBank(String id) {
		parsers = new LinkedList<>();
		this.id = id;
	}
	
	/**
	 * Adds the SegmentParser to the ordered list of segmentParsers.
	 * @param parser the segmentParser to be added
	 */
	public void addSegment(SegmentParser parser) {
		int i=0;
		while(i < parsers.size() && parser.succeeds(parsers.get(i)))
			i++;
		parsers.add(i, parser);
	}
	
	/**
	 * Returns the segmentParser in this bank with the newest/highest version-number.
	 * @return the segmentParser with the highest version-number 
	 */
	public SegmentParser getNewest() {
		return parsers.get(parsers.size()-1);
	}
	
	
	/**
	 * Returns the segmentParser in this bank with the exact same version-number.
	 * @param version of the segmentParser to return
	 * @return the segmentParser with the exact same version-number
	 * @nullable returns null if there are no segmentParsers with a matching version-number
	 */
	public SegmentParser getParser(int[] version) {
		SegmentParser out = null;
		for (SegmentParser parser :  parsers) {
			if (parser.hasVersion(version)) {
				out = parser;
				break;
			}
		}
		return out;
	}

}
