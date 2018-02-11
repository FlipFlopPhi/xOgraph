/**
 * 
 */
package ru.flip.xOgraph.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.flip.xOgraph.Project;
import ru.flip.xOgraph.model.Map;
import ru.flip.xOgraph.model.Region;

/**
 * @author Vizu
 *
 */
public class RegionOptionPane extends JDialog {
	
	private JTextField nameField;
	
	private Region region;
	/**
	 * 
	 */
	public RegionOptionPane(JFrame frame) {
		super(frame, true);
		JPanel panel = new JPanel();
		this.add(panel);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameField = new JTextField();
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeDialog(true);
				Project.repaint(new int[] {Map.MODIFIED_REGION});
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeDialog(false);
			}
		});
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addComponent(nameLabel)
					.addComponent(nameField)
					)
				.addGroup(layout.createSequentialGroup()
					.addComponent(confirmButton)
					.addComponent(cancelButton)
					)
			);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(nameLabel)
					.addComponent(nameField)
					)
				.addGroup(layout.createParallelGroup()
					.addComponent(confirmButton)
					.addComponent(cancelButton)
					)
			);
		this.pack();
	}
	
	public void showDialog(Region region) {
		if (region == null)
			return;
		this.region = region;
		nameField.setText(region.name);
		setVisible(true);
	}
	
	public void closeDialog(Boolean doSave) {
		if (doSave) { 
			region.name = nameField.getText();
		}
		setVisible(false);
	}

}
