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
import ru.flip.xOgraph.model.Point;

/**
 * @author Vizu
 *
 */
public class LocationOptionPane extends JDialog{

	private Point location;
	
	private JTextField nameField;
	private JButton confirmButton;
	private JButton cancelButton;
	
	public LocationOptionPane(JFrame frame) {
		super(frame, true);
		JPanel panel = new JPanel();
		this.add(panel);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameField = new JTextField();
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeDialog(true);
				Project.repaint();
			}
		});
		cancelButton = new JButton("Cancel");
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

	public void showDialog(Point poi) {
		if (poi == null)
			return;
		location = poi;
		nameField.setText(poi.name);
		setVisible(true);
	}
	
	public void closeDialog(Boolean doSave) {
		if (doSave) { 
			location.name = nameField.getText();
		}
		setVisible(false);
	}
}
