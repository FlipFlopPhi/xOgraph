/**
 * 
 */
package ru.flip.xOgraph.IO;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import ru.flip.xOgraph.model.Map;

/**
 * A Segment parser is a parser used to parse a specific type of segment.
 * 
 * @author Vizu
 */
public abstract class SegmentParser {

	/**
	 * The ID used to show which segment this parser should work with.
	 */
	public final String id;
	
	/**
	 * The version number of this segmentparser.
	 */
	public final int[] version;

	/**
	 * Constructs a segmentparser with a given id and versionnumber.
	 * @param id the segmentparser's ID
	 * @param version the segmentparser's versionnumber
	 */
	public SegmentParser(String id, int[] version) {
		this.id = id;
		this.version = version.clone();
	}

	/**
	 * Applies the readElement function on each given line.
	 * @param map the map to which the elements are added
	 * @param lines a list of Strings, each containing exactly one element
	 */
	public void readSegment(Map map, List<String> lines) {
		for (String line : lines) {
			String[] values = line.split(";");
			readElement(map, values);
		}
	}

	/**
	 * Turns the submitted values into the segment's element and adds it to the map.
	 * @param map the map to which the elements are added
	 * @param values an array of values that are used to create the element
	 */
	public abstract void readElement(Map map, String[] values);

	/**
	 * Writes down the current segment.
	 * @param elements
	 * @param writer
	 * @throws IOException
	 */
	public final void writeSegment(Iterable<?> elements, Writer writer) throws IOException {
		writer.write("==\n");
		writer.write(id + ";");
		String out = "";
		for (int n : version) {
			out += n + ".";
		}
		writer.write(out.substring(0, out.length() - 1) + '\n');

		for (Object element : elements) {
			writeElement(element, writer);
		}
	}

	/**
	 * Writes down the element through the writer
	 * @param element
	 * @param writer
	 * @throws IOException
	 */
	public abstract void writeElement(Object element, Writer writer) throws IOException;

	/**
	 * Checks whether this segmentparser has a newer version than the submitted one.
	 * @param parser the other segmentparser to compare versions to
	 * @return True when this segmentsparsers version is higher than the submitted one.
	 */
	public final boolean succeeds(SegmentParser parser) {
		int i = 0;
		while (parser.version[i] == version[i]) {
			i++;
		}
		return (version[i] > parser.version[i]);
	}

	/**
	 * Checks whether the versions match.
	 * @return returns true if the versions match exactly
	 */
	public boolean hasVersion(int[] version2) {
		if (version.length != version2.length)
			return false;
		for (int i = 0; i < version.length; i++) {
			if (version[i] != version2[i])
				return false;
		}
		return true;
	}
}
