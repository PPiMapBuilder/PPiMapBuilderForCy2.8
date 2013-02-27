package PPiMapBuilder;

import cytoscape.data.Semantics;
import cytoscape.util.*;
import cytoscape.view.cytopanels.CytoPanelImp;
import java.awt.event.ActionEvent;
import cytoscape.*;
import javax.swing.*;

/**
 * 
 * @author pidupuis
 *
 */
public class PPiMapBuilderCreateNetworkAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public PPiMapBuilderCreateNetworkAction() {
		super();
	}
	
	/**
	 * ActionPerformed which contains the actions which are triggered when you click on your launcher
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Creation of the panel
		CytoPanelImp ctrlPanel = (CytoPanelImp) Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST); // Retrieve the Cytoscape control panel
		ctrlPanel.add(PPiMapBuilderPanel.Instance(), 1); // Add the new panel at the index 1 (so at the second position in the control panel)
		ctrlPanel.setSelectedIndex(ctrlPanel.indexOfComponent(PPiMapBuilderPanel.Instance())); // Specify that the panel selected by default is our panel
			
		// Creation of the network
		CyNetwork myNetwork = Cytoscape.createNetwork("network", false); // Creation of a network
		
		// Creation of the network components
		CyNode myNode1 = Cytoscape.getCyNode("PPID", true); // Create a first node
		CyNode myNode2 = Cytoscape.getCyNode("HSP90", true); // Create a second node
		myNetwork.addNode(myNode1); // Add the first node to the current network
		myNetwork.addNode(myNode2); // Add the second node to the current network
		CyEdge myEdge = Cytoscape.getCyEdge(myNode1, myNode2, Semantics.INTERACTION, "pp", true); // Create a link between the two nodes and indicate that it is an interaction between two proteins (pp)
		myNetwork.addEdge(myEdge); // Add the link in the network
		
		// Creation of the mediator
		PPiMapBuilderMediator myMediator = new PPiMapBuilderMediator(myNetwork);
		
	}
	
}
