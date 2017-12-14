package ru.flip.xOgraph.ui.canvas;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import ru.flip.xOgraph.MapListener;
import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Hex;
import ru.flip.xOgraph.model.Line;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Point;
import ru.flip.xOgraph.model.Position;
import ru.flip.xOgraph.model.Region;
import ru.flip.xOgraph.ui.UIUtil;

public class HexCanvas extends JPanel implements Scrollable{

	
	public int radius = 32;//HOOK
	
	
	private List<HexDrawOperation> drawQueue;
	
	public HexCanvas() {
		super();
		drawQueue = new LinkedList<HexDrawOperation>();
		addMouseListener(new MapListener(this));
		setSize(740, 620);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int r = radius;
		g.setColor(Color.black);
		//draw background
		if (!Project.imageOnForeground)
			drawBackground(g, r);
		
		//draw grid
		Map map = Project.getMap();
		for(int x=0; x < map.getWidth(); x++) {
			for(int y=0; y < map.getHeight(); y++) {
				int oX, oY;
				oX = (int) Math.round(x*(r*1.5)) + r;
				oY = (int) Math.round(y*r*Math.sqrt(3) + (x%2)*(r/2)*Math.sqrt(3)) + r;
				drawHex(g, map.getHex(x, y), oX, oY, r, 0);
			}
		}
		while(!drawQueue.isEmpty()) {
			drawQueue.remove(0).execute(g);
		}
		
		//draw locations
		g.drawString(map.getWidth()+":"+map.getHeight(), 32, 32);
		for(Point location: map.getPoints()) {
			int x = location.getPosition().getCanX(radius);
			int y = location.getPosition().getCanY(radius);
			
			Font oldFont = g.getFont();
			g.setFont(new Font("arial bold", Font.PLAIN, 16));
			UIUtil.drawCenteredString(g, x, y, location.name);
			g.setFont(oldFont);
		}
		
		//draw regions
		for(Region region : Project.getMap().regions) {
			drawRegion((Graphics2D) g, region);
		}
		
		//draw lines
		g.setColor(Color.BLACK);
		for(Line line : Project.getMap().lines) {
			for(int i=0; i<line.pieces.size()-1; i++) {
				Position pos1 = line.pieces.get(i);
				Position pos2 = line.pieces.get(i+1);
				g.drawLine(pos1.getCanX(radius), pos1.getCanY(radius)
						, pos2.getCanX(radius), pos2.getCanY(radius));
			}
		}
		
		//draw tool
		Project.currentTool.paint(g);
		
		//draw foreground
		if (Project.imageOnForeground) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			drawBackground(g, r);
		}
		
		
		
		//draw graphic scale
	}
	
	private class HexDrawOperation {
		private Hex hex;
		private int oX, oY, r;
		private double angle;
		HexDrawOperation(Hex hex, int oX, int oY,int r, double angle) {
			this.hex = hex;
			this.oX = oX;
			this.oY = oY;
			this.r = r;
			this.angle = angle;
		}
		
		private void execute(Graphics g) {
			drawHex(g, hex, oX, oY, r, angle);
		}
	}
	
	

	private void drawRegion(Graphics2D g, Region region) {
		Polygon polygon = region.toPolygon(radius);
		g.setColor(new Color(0xf, 0xf, 0xf, 55));
		g.fillPolygon(polygon);
		g.setColor(Color.BLACK);
		
		Stroke oldStroke = g.getStroke();
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
				0, new float[] {9}, 0);
		g.setStroke(dashed);
		g.drawPolygon(polygon);
		g.setStroke(oldStroke);
		
		//draw text
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		for(int x : polygon.xpoints) {
			minX = Math.min(x, minX);
			maxX = Math.max(x, maxX);
		}
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for(int y: polygon.ypoints) {
			minY = Math.min(y, minY);
			maxY = Math.max(y, maxY);
		}
		Font oldFont = g.getFont();
		g.setFont(new Font("arial bold", Font.PLAIN, 16));
		UIUtil.drawCenteredString(g, (minX +maxX)/2, (minY + maxY)/2, region.name);
		g.setFont(oldFont);
	}

	private void drawBackground(Graphics g, int r) {
		Image img = Project.bgImage;
		if (img == null)
			return;
		Map map = Project.getMap();
		double mapW = (.5+ map.getWidth()*1.5)*r;
		double mapH = (map.getHeight()*2)*Math.sqrt(3)*r*.5 + r;
		
		double rW = img.getWidth(null) / mapW;
		double rH = img.getHeight(null) / mapH;
		if (rW <= rH) {
			g.drawImage(img, 0, 0, (int)(mapW), (int)(img.getHeight(null)/rW), null);
		} else {
			g.drawImage(img, 0, 0, (int)(img.getWidth(null) /rH), (int)mapH, null);
		}
	}
	
	private void drawHex(Graphics g,Hex hex, int oX, int oY,int r, double angle) {
		
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];
		for(int i=0; i < 6; i++) {
			xPoints[i] = (int) Math.round(oX + r*Math.cos(angle + i * Math.PI / 3));
			yPoints[i] = (int) Math.round(oY + r*Math.sin(angle + i * Math.PI / 3));
		}
		g.setColor(Color.BLACK);
		
		//draw terrain
		if (hex.token != null) {
			Shape oldClip = g.getClip();
			Rectangle bounds = g.getClipBounds();
			g.setClip(new Polygon(xPoints, yPoints, 6));
			g.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);
			g.drawImage(hex.token.getImage(), oX-r, oY-r
					, 2*r, 2*r, null);
			g.setClip(oldClip);
		}
		
		if (hex.isEnhanced()) {
			double newR = Math.sqrt(3)/(Math.sqrt(21)) * r;
			double newAngle = angle + Math.atan(1.5d/(2.5d*Math.sqrt(3)));
			drawHex(g, hex.getChild(0), oX, oY, (int)newR, newAngle);
			for(int i=0; i<6; i++) {
				drawQueue.add(new HexDrawOperation(
						hex.getChild(i+1)
						,(int) (oX+(Math.sqrt(3)*newR)*Math.cos((Math.PI/6d - newAngle) + 2*Math.PI/6 + Math.PI/3 +  i*Math.PI/3))
						,(int) (oY+(Math.sqrt(3)*newR)*-Math.sin((Math.PI/6d - newAngle) + 2*Math.PI/6 + Math.PI/3 + i*Math.PI/3))
						,(int)(newR), newAngle)
					);
			
			}
			return;
		}
		
		
		
		Point point;
		if ((point = hex.getPOI()) != null) {
			Shape oldClip = g.getClip();
			Rectangle bounds = g.getClipBounds();
			g.setClip(new Polygon(xPoints, yPoints, 6));
			g.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);
			g.drawImage(point.getImage(), oX-r, oY-r
					, 2*r, 2*r, null);
			g.setClip(oldClip);
		}
		//draw grid
		if (Project.drawGrid)
			g.drawPolygon(xPoints, yPoints, 6);
		
		if (hex.selected) {
			g.setColor(new Color(0, 0xf, 0xff, 55));
			g.fillPolygon(xPoints, yPoints, 6);
			g.setColor(Color.BLACK);
		}
		
		
	}
	

	
	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = super.getPreferredSize();
		Map map = Project.getMap();
		
		double mapW = (.5+ map.getWidth()*1.5)*radius +1;
		double mapH = (map.getHeight()*2)*Math.sqrt(3)*radius*.5 + radius +2;
		Image img = Project.bgImage;
		if (img == null)
			return new Dimension((int)Math.max(mapW, dimension.getWidth())
					,(int)Math.max(mapH, dimension.getHeight()));
		
		double rW = img.getWidth(null) / mapW;
		double rH = img.getHeight(null) / mapH;
		if (rW <= rH) {
			mapH = (int)(img.getHeight(null)/rW);
		} else {
			mapW = (int)(img.getWidth(null) /rH);
		}
		return new Dimension((int)Math.max(mapW, dimension.getWidth())
				,(int)Math.max(mapH, dimension.getHeight()));
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(640,480);
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return (int)(10*Project.scale);
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 10;
	}
}
