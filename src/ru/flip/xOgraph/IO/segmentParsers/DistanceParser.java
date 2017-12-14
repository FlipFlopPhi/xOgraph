package ru.flip.xOgraph.IO.segmentParsers;

import java.io.IOException;
import java.io.Writer;

import ru.flip.xOgraph.IO.SegmentParser;
import ru.flip.xOgraph.model.Map;

public abstract class DistanceParser extends SegmentParser{

	public DistanceParser(int[] version) {
		super("Core:Distance", version);
	}

	public final static SegmentParser v100 = new DistanceParser(new int[] {1,0,0}) {

		@Override
		public void readElement(Map map, String[] values) {
			
		}

		@Override
		public void writeElement(Object element, Writer writer) throws IOException {
			
		}
		
	};
}
