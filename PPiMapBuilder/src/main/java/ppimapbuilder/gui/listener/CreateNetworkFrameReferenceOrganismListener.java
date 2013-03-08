package ppimapbuilder.gui.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import ppimapbuilder.gui.CreateNetworkFrame;

public class CreateNetworkFrameReferenceOrganismListener implements ActionListener{
	private CreateNetworkFrame window;
	private JCheckBox previous = null;
	
	public CreateNetworkFrameReferenceOrganismListener(CreateNetworkFrame window) {
		this.window = window;
	}
	
	public void actionPerformed(ActionEvent e) {
		JComboBox select = (JComboBox)e.getSource();
		JCheckBox check = window.getOrganisms().get((String)select.getSelectedItem());
		
		if(previous == null)
			previous = (JCheckBox)window.getOrganisms().values().toArray()[0];

		previous.setEnabled(true);
		
		check.setSelected(true);
		check.setEnabled(false);
		
		previous = check;
	}
}
