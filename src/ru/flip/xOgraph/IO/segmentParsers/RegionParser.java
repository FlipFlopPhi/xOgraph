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
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Position;
import ru.flip.xOgraph.model.Region;

/**
 *  This segmentparser takes cares of instances of Region on the map.
 * @author Vizu
 *
 */
public abstract class RegionParser extends SegmentParser {


	public RegionParser(int[] version) {
		super("Core:Region", version);
	}
	
	public final static RegionParser v1000 = new RegionParser(new int[] {1,0}) {

		@Override
		public void readElement(Map map, String[] values) {
			String[] pieces = values[0].split(">");
			List<Position> positions = new LinkedList<>();
			for(String piece : pieces) {
				positions.add(IOUtil.parsePosition(piece));
			}
			String name = values[1];
			map.regions.add(new Region(positions, name));
		}

		@Override
		public void writeElement(Object object, Writer writer) throws IOException {
			Region region = (Region) object;
			String out = "";
			for(Position position : region.corners) {
				out += position.toMicroString() + ">";
			}
			out += ";"+ region.name + '\n';
			writer.write(out);
		}
	};
	
	
}
