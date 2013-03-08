package ppimapbuilder;

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
public class CreateNetworkAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;
	
	private PMBPanel myPanel = PMBPanel.Instance(); // Instance of the ppimapbuilder panel to prevent several instances 
	
	private String poiName; // Name of the protein of interest

	/**
	 * Default constructor
	 */
	public CreateNetworkAction() {
		super();
	}
	
	/**
	 * ActionPerformed which create the network and add the ppimapbuilder panel
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		/* NETWORK CREATION */
		// [!] This part creates a example network so this will change to call the creation frame...
		
		//Creation of the protein of interest
		poiName = "PPID";
		
		// Creation of the network
		CyNetwork myNetwork = Cytoscape.createNetwork(poiName+"_interactom", false); // Creation of a network (with the name of the poi)
		
		// Creation of the network components
		PMBNode poiNode = new PMBNode(Cytoscape.getCyNode(poiName, true)); // Create the ppimapbuilder nodes
		PMBNode myNode1 = new PMBNode(Cytoscape.getCyNode("HSP90", true));
		myNetwork.addNode(poiNode); // Add the first node to the current network
		myNetwork.addNode(myNode1); // Add the second node to the current network
		CyEdge myEdge = Cytoscape.getCyEdge(poiNode, myNode1, Semantics.INTERACTION, "pp", true); // Create a link between the two nodes and indicate that it is an interaction between two proteins (pp)
		myNetwork.addEdge(myEdge); // Add the link in the network
		
		// Add to the mediator
		Mediator.Instance().addNetwork(myNetwork);
		
		
		/* PANEL */
		
		// Creation of the panel
		CytoPanelImp ctrlPanel = (CytoPanelImp) Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST); // Retrieve the Cytoscape control panel
		ctrlPanel.add(myPanel, 1); // Add the new panel at the index 1 (so at the second position in the control panel)
		ctrlPanel.setSelectedIndex(ctrlPanel.indexOfComponent(myPanel)); // Specify that the panel selected by default is our panel
		
	}
	
}
