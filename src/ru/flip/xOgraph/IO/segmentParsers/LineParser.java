/**
 * 
 */
package ru.flip.xOgraph.IO.segmentParsers;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import ru.flip.xOgraph.IO.IOUtil;
import ru.flip.xOgraph.IO.SegmentParser;
import ru.flip.xOgraph.model.Line;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Position;

/**
 * This segmentparser takes cares of instances of Line on the map.
 * @author Vizu
 *
 */
public abstract class LineParser extends SegmentParser {

	public LineParser(int[] version) {
		super("Core:Line", version);
	}
	
	public static final LineParser v100 = new LineParser( new int[]{1,0,0}) {

		@Override
		public void readElement(Map map, String[] values) {
			String[] pieces = values[0].split(">");
			List<Position> positions = new LinkedList<>();
			for(String piece : pieces) {
				positions.add(IOUtil.parsePosition(piece));
			}
			Line line = new Line(positions);
			map.lines.add(line);
		}

		@Override
		public void writeElement(Object element, Writer writer) throws IOException {
			Line line = (Line) element;
			String out = "";
			for(Position piece : line.pieces) {
				out += piece.toMicroString() + ">";
			}
			writer.write(out + '\n');
		}
	};

}
