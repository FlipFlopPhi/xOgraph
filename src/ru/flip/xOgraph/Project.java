/**
 * 
 */
package ru.flip.xOgraph;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ru.flip.xOgraph.actions.AbstractAction;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.HexToken;
import ru.flip.xOgraph.model.Line;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Location;
import ru.flip.xOgraph.model.Region;
import ru.flip.xOgraph.tools.AbstractTool;
import ru.flip.xOgraph.tools.BorderTool;
import ru.flip.xOgraph.tools.EditTool;
import ru.flip.xOgraph.tools.FillTool;
import ru.flip.xOgraph.tools.POITool;
import ru.flip.xOgraph.tools.PaintTool;
import ru.flip.xOgraph.tools.RoadTool;
import ru.flip.xOgraph.ui.LocationOptionPane;
import ru.flip.xOgraph.ui.RegionOptionPane;
import ru.flip.xOgraph.ui.ScrollCanvas;
import ru.flip.xOgraph.ui.canvas.HexCanvas;
import ru.flip.xOgraph.ui.menubar.XZoomSlider;
import ru.flip.xOgraph.ui.windows.DistanceWindow;

/**
 * @author Vizu
 *
 */
public final class Project {
	
	public static final JFrame frame = new JFrame();
	public static final HexCanvas canvas = new HexCanvas();
	//public static final XZoomSlider zoomSlider = new XZoomSlider();
	public static final XZoomSlider zoomPanel = new XZoomSlider();
	public static final ScrollCanvas scrollCanvas = new ScrollCanvas();
	public static final JLabel descriptionLabel = new JLabel();
	public static final LocationOptionPane locationOptions;
	public static final RegionOptionPane regionOptions;
	
	public static final DistanceWindow distanceWindow;
	
	public static final DefaultListModel<Region> listModel = new DefaultListModel<>();
	
	public enum Tool {PAINT, EDIT, POI, FILL, BORDER, ROAD, LINE};
	public static AbstractTool currentTool;
	private static List<AbstractTool> tools = new LinkedList<AbstractTool>();;
	
	public static BufferedImage bgImage;
	public static boolean imageOnForeground = false;
	
	private static Map map;
	public static HexToken selectedToken;
	public static Region selectedRegion;
	
	private static List<AbstractAction> actionLog = new LinkedList<AbstractAction>();
	private static int logIterator = 0;
	
	public static double scale = 1;
	public static int drawDepth = 0;
 
	public static List<Hex> selection;

	

	public static boolean drawGrid = true;
	public static List<Line> lines = new ArrayList<Line>();
	

	
	static {
		locationOptions = new LocationOptionPane(frame);
		regionOptions = new RegionOptionPane(frame);
		distanceWindow = new DistanceWindow(frame);
		
		selection = new LinkedList<Hex>();
		
		tools.add(new EditTool(canvas));
		tools.add(new PaintTool(canvas));
		tools.add(new POITool(canvas));
		tools.add(new FillTool(canvas));
		tools.add(new BorderTool(canvas));
		tools.add(new RoadTool(canvas));
		setTool(Tool.PAINT);
	}
	
	public static void setMap(Map map) {
		selection.clear();
		actionLog.clear();
		logIterator = 0;
		Project.map = map;
	}
	
	public static Map getMap() {
		return map;
	}
	
	public static void setBGImage(BufferedImage img) {
		bgImage = img;
		repaint(new int[] {Map.MODIFIED_BGIMAGE});
	}
	
	public static void repaint(int[] fieldsModified) {
		int oldRadius = canvas.radius;
		canvas.radius = (int) (32 * scale);
		if (oldRadius!= canvas.radius)
			distanceWindow.update();
		canvas.setSize(canvas.getPreferredSize());
		
		canvas.setFieldsModified(fieldsModified);
		canvas.repaint();
		
	}
	
	

	public static void updateListModels() {
		listModel.clear();
		for(Region region : map.regions) {
			listModel.addElement(region);
		}
	}

	public static void select(Hex hex) {
		hex.selected = true;
		if (!selection.contains(hex))
			selection.add(hex);
	}
	
	public static void deselect(Hex hex) {
		hex.selected = false;
		selection.remove(hex);
	}

	public static void setTool(Tool toolID) {
		for(AbstractTool tool : tools) {
			if (tool.getID() == toolID) {
				currentTool = tool;
				descriptionLabel.setText(tool.getDescription());
				return;
			}
		}
	}
	
	public static AbstractTool[] getTools() {
		AbstractTool[] array = new AbstractTool[tools.size()];
		return tools.toArray(array);
	}

	public static void clearSelection() {
		for(Hex hex: selection) {
			hex.selected = false;
		}
		selection.clear();
	}
	
	public static void commit(AbstractAction action) {
		action.commit();
		for(int i = logIterator; i < actionLog.size(); i++) {
			actionLog.remove(i);
		}
		actionLog.add(action);
		logIterator++;
		
		repaint(action.getModifications());
		
	}
	
	public static void undo() {
		if (logIterator == 0)
			return;
		AbstractAction action = actionLog.get(logIterator-1);
		action.undo();
		logIterator--;
		repaint(action.getModifications());
	}
	
	public static void redo() {
		if (!canRedo())
			return;
		AbstractAction action = actionLog.get(logIterator);
		action.commit();
		logIterator++;
		repaint(action.getModifications());
	}
	
	public static int getIterator() {return logIterator;}

	public static boolean canRedo() {
		return logIterator < actionLog.size();
	}

	public static void changeDrawDepth(int newDepth) {
		drawDepth = newDepth;
		for(Region region : map.regions) {
			region.changeDepth(drawDepth);
		}
		repaint(new int[] {Map.MODIFIED_ALL});
	}

	
}
