/**
 * 
 */
package ru.flip.xOgraph;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.lang.instrument.Instrumentation;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.flip.xOgraph.model.HexToken;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.ui.RegionTab;
import ru.flip.xOgraph.ui.XToolBar;
import ru.flip.xOgraph.ui.menubar.XMenuBar;
import ru.flip.xOgraph.ui.windows.DistanceWindow;

/**
 * @author Vizu
 *
 */
public class Main {

	private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
    
	
	/**
	 * @param args
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		
		//Help me
		JFrame frame = Project.frame;
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(854,480));
		
		Project.setMap(new Map(6, 8));
		
		frame.add(new XMenuBar(), BorderLayout.NORTH);
		frame.add(new XToolBar(), BorderLayout.WEST);
		frame.add(Project.scrollCanvas);
		
		JTabbedPane selectionView = new JTabbedPane();
		selectionView.setSize(new Dimension(300,frame.getHeight()));
		
		JList<HexToken> hexList = new JList<HexToken>(HexToken.ALL);
		hexList.setMaximumSize(new Dimension(240, 640));
		hexList.setCellRenderer(new DefaultListCellRenderer() {
			
			@Override
		    public Component getListCellRendererComponent(
		            JList<?> list, Object value, int index,
		            boolean isSelected, boolean cellHasFocus) {

		        JLabel label = (JLabel) super.getListCellRendererComponent(
		                list, value, index, isSelected, cellHasFocus);
		        HexToken token = (HexToken) value;
		        label.setIcon( new ImageIcon(
		        		token.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH) 
		        		) );
		        label.setText(token.getName());
		        return label;
		    }
		});
		hexList.setFixedCellWidth(180);
		hexList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent lse) {
				JList<HexToken> list = (JList<HexToken>)lse.getSource();
				Project.selectedToken =
						list.getSelectedValue();
		}
		});
		selectionView.addTab("Hexes", new JScrollPane(hexList));
		selectionView.addTab("Regions", new RegionTab(selectionView));
		
		frame.add(selectionView, BorderLayout.EAST);
		
		Project.descriptionLabel.setBorder(BorderFactory.createEtchedBorder());
		frame.add(Project.descriptionLabel, BorderLayout.PAGE_END);
		frame.pack();
		frame.setVisible(true);
		
		
		
		if(instrumentation!=null) {
			System.out.println(getObjectSize(Project.getMap()));
		}
	}
	

}
