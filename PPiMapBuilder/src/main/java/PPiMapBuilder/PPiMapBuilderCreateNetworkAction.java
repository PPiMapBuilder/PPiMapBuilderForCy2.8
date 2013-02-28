package PPiMapBuilder;

import cytoscape.data.Semantics;
import cytoscape.util.*;
import cytoscape.view.CyNetworkView;
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
		
		//Creation of the protein of interest
		String poiName = "PPID";
		
		// Creation of the network
		CyNetwork myNetwork = Cytoscape.createNetwork(poiName+"_interactom", false); // Creation of a network (with the name of the poi)
		
		// Creation of the network components
		PPiMapBuilderNode poiNode = new PPiMapBuilderNode(Cytoscape.getCyNode(poiName, true));
		//CyNode myNode1 = Cytoscape.getCyNode("PPID1", true); // Create a first node
		PPiMapBuilderNode myNode1 = new PPiMapBuilderNode(Cytoscape.getCyNode("HSP90", true));
		//CyNode myNode2 = Cytoscape.getCyNode("HSP90", true); // Create a second node
		myNetwork.addNode(poiNode); // Add the first node to the current network
		myNetwork.addNode(myNode1); // Add the second node to the current network
		CyEdge myEdge = Cytoscape.getCyEdge(poiNode, myNode1, Semantics.INTERACTION, "pp", true); // Create a link between the two nodes and indicate that it is an interaction between two proteins (pp)
		myNetwork.addEdge(myEdge); // Add the link in the network
		
		// Creation of the mediator
		PPiMapBuilderMediatorView myView = new PPiMapBuilderMediatorView(myNetwork);

	}
	
}
