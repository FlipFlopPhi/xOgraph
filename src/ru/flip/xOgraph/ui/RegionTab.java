/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Region;

/**
 * @author Vizu
 *
 */
public class RegionTab extends JPanel {
	
	private RegionOptionPane regionOptionPane;
	
	@SuppressWarnings("serial")
	public RegionTab(Component parent) {
		super();
		regionOptionPane = new RegionOptionPane(Project.frame);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(new RelativeDimension(parent, 1, 1));
		//Scrollable list
		final JList<Region> regionList = new JList<Region>(Project.listModel);
		regionList.setMaximumSize(new RelativeDimension(this, 1f, 0.5f));
		regionList.setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object region,
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, region, index, isSelected, cellHasFocus
						);
				label.setText(((Region) region).name);
				return label;
			}
			
		});
		regionList.addListSelectionListener( new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				Project.selectedRegion = regionList.getSelectedValue();
			}
		});
		JScrollPane scrollableRegionList = new JScrollPane();
		scrollableRegionList.getViewport().setView(regionList);
		this.add(scrollableRegionList);
		
		//Buttons
		
		JButton editButton = new JButton("Edit");
		editButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Region selected = regionList.getSelectedValue();
				if (selected != null) {
					regionOptionPane.showDialog(selected);
					Project.repaint(new int[] {Map.MODIFIED_REGION});
				}
			}
		});
		this.add(editButton);
		JButton focusButton = new JButton("Focus on");
		focusButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Region selected = regionList.getSelectedValue();
				if (selected != null) {
					Project.scrollCanvas.focusOn(selected);
					Project.repaint(new int[] {Map.MODIFIED_ALL});
				}
			}
		});
		this.add(focusButton);
		JButton removeButton = new JButton("remove");
		removeButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Region selected = regionList.getSelectedValue();
				if (selected != null) {
					Project.getMap().regions.remove(selected);
					Project.repaint(new int[] {Map.MODIFIED_REGION});
				}
			}
		});
		this.add(removeButton);
	}
}
