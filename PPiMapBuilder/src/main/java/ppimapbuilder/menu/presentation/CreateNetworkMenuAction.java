package ppimapbuilder.menu.presentation;

import cytoscape.util.*;
import java.awt.event.ActionEvent;
import ppimapbuilder.menu.PMBMenuControl;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class CreateNetworkMenuAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ActionPerformed which create the network and focus on the ppimapbuilder panel
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		PMBMenuControl.openNetworkCreationFrame(); // Create the network creation frame and focus on the PMB panel
	}
	
}
