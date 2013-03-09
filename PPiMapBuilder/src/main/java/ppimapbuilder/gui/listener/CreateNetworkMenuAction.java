package ppimapbuilder.gui.listener;

import cytoscape.util.*;
import cytoscape.view.cytopanels.CytoPanelImp;
import java.awt.event.ActionEvent;
import cytoscape.*;
import javax.swing.*;
import ppimapbuilder.gui.CreateNetworkFrame;
import ppimapbuilder.gui.PMBPanel;

/**
 * 
 * @author pidupuis
 *
 */
public class CreateNetworkMenuAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;
	
	private PMBPanel myPanel = PMBPanel.Instance(); // Instance of the ppimapbuilder panel to prevent several instances 
	private CreateNetworkFrame myFrame; // Creation of the thread 
	/**
	 * Default constructor
	 */
	public CreateNetworkMenuAction() {
		super();
	}
	
	/**
	 * ActionPerformed which create the network and add the ppimapbuilder panel
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		myFrame = CreateNetworkFrame.Instance();
		myFrame.setVisible(true);

		/* PANEL */
		
		// Creation of the panel
		CytoPanelImp ctrlPanel = (CytoPanelImp) Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST); // Retrieve the Cytoscape control panel
		ctrlPanel.add(myPanel, 1); // Add the new panel at the index 1 (so at the second position in the control panel)
		ctrlPanel.setSelectedIndex(ctrlPanel.indexOfComponent(myPanel)); // Specify that the panel selected by default is our panel
		
		
	}
	
}
