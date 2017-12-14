package ru.flip.xOgraph.IO;

import ru.flip.xOgraph.model.Position;

/**
 * A class containing useful static functions to do complicated calculations that are used for the IO side.
 * @author Vizu
 *
 */
public class IOUtil {

	/**
	 * Parses the submitted string into a Position object.
	 * @param string the to be parsed string
	 * @return a position corresponding to the one contained in the string
	 */
	public static Position parsePosition(String string) {
		String[] segments = string.substring(1, string.length()-1).split(",");
		int depth = 0;
		if (segments.length == 3) {
			depth = Integer.parseInt(segments[2]);
		}
		int q = Integer.parseInt(segments[0]);
		int r = Integer.parseInt(segments[1]);
		return new Position(q, r, depth);
	}

}
