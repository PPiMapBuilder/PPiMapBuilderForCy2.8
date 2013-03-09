package ppimapbuilder.gui.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import ppimapbuilder.gui.CreateNetworkFrame;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class CreateNetworkFrameReferenceOrganismListener implements ActionListener{
	private CreateNetworkFrame window;
	private JCheckBox previous = null;
	
	/**
	 * Create a reference organism combobox listener with reference to its parent window
	 * @param window
	 */
	public CreateNetworkFrameReferenceOrganismListener(CreateNetworkFrame window) {
		this.window = window;
	}
	
	/**
	 * Select and disable checkbox corresponding to the selected element in combobox
	 */
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> select = (JComboBox<String>)e.getSource();
		
		JCheckBox check = null;
		
		// Get the checkbox corresponding to the selected element in combobox
		for(JCheckBox c : window.getOrganisms().values())
			if(c.getText().equals(select.getSelectedItem()))
				check = c;
		
		if(check != null)  {
			if(previous == null)
				previous = (JCheckBox)window.getOrganisms().values().toArray()[0];

			previous.setEnabled(true);
			
			check.setSelected(true);
			check.setEnabled(false);
			
			previous = check;
		}
	}
}
