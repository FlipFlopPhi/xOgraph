/**
 * 
 */
package ru.flip.xOgraph.IO.segmentParsers;

import java.io.IOException;
import java.io.Writer;

import ru.flip.xOgraph.IO.IOUtil;
import ru.flip.xOgraph.IO.SegmentParser;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Location;
import ru.flip.xOgraph.model.Position;

/**
 *  This segmentparser takes cares of instances of Point (points of interest) on the map.
 * @author Vizu
 *
 */
public abstract class LocationParser extends SegmentParser {

	public LocationParser(int[] version) {
		super("Core:Location", version);
	}
	
	public final static LocationParser v100 = new LocationParser(new int[] {1,0,0}) {
		
		@Override
		public void readElement(Map map, String[] values) {
			Position position = IOUtil.parsePosition(values[0]);
			Location point = new Location(position);
			map.points.add(point);
			point.name = values[1];
		}

		@Override
		public void writeElement(Object element, Writer writer) throws IOException {
			Location point = (Location) element;
			String out = "";
			out += point.getPosition().toMicroString();
			out += ";" + point.name;
			writer.write(out + '\n');
		}
	};
	
}
