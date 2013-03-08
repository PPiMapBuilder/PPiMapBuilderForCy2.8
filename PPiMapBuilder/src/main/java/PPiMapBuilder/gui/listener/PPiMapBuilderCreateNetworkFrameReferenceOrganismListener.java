package PPiMapBuilder.gui.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import PPiMapBuilder.gui.PPiMapBuilderCreateNetworkFrame;

public class PPiMapBuilderCreateNetworkFrameReferenceOrganismListener implements ActionListener{
	private PPiMapBuilderCreateNetworkFrame window;
	private JCheckBox previous = null;
	
	public PPiMapBuilderCreateNetworkFrameReferenceOrganismListener(PPiMapBuilderCreateNetworkFrame window) {
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
