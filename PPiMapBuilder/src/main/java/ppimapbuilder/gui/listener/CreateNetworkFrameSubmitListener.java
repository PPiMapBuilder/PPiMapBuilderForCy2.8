package ppimapbuilder.gui.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import ppimapbuilder.Mediator;
import ppimapbuilder.PMBNode;
import ppimapbuilder.gui.CreateNetworkFrame;

public class CreateNetworkFrameSubmitListener implements ActionListener{
	
	private CreateNetworkFrame myFrame; // Frame which is contains the submit
	private ArrayList<String> poiList; // List of proteins of interest
	//private ArrayList<String> dbList; // List of selected organisms
	//private ArrayList<String> orgaList; // List of selected databases
	private CyNetwork myNetwork; // Result network
	private PMBNode poiNode, node2; // nodes

	
	public CreateNetworkFrameSubmitListener(CreateNetworkFrame myFrame) {
		this.myFrame = myFrame;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		
		try {
			poiList = myFrame.getIdentifiers(); // Retrieve the identifier list
		}
		catch (ArrayStoreException e2) {
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "The identifier list is empty.");
			return;
		}
		
		myFrame.close();
		
		//dbList = myFrame.getDatabaseValues(); // Retrieve the database list
		//orgaList = myFrame.getOrganismValues(); // Retrieve the organism list
		

		/* NETWORK CREATION */
		// [!] This part creates a example network so this will change to call the creation frame...
		
		// Creation of the network
		myNetwork = Cytoscape.createNetwork("", false); // Creation of a network (with the name of the poi)

		// Creation of the network components
		for (String id : poiList) { // For each protein of interest
			
				poiNode = new PMBNode(Cytoscape.getCyNode(id, true));
				myNetwork.addNode(poiNode);
				
				// Call the database connector
				// For each interaction, we create another node and one edge between them
				
				// [TEST]
				poiNode = new PMBNode(Cytoscape.getCyNode(id, true)); // Same node to test if there are redundancies
				myNetwork.addNode(poiNode);
				
				node2 = new PMBNode(Cytoscape.getCyNode("plop", true)); // Another node to have interactions
				myNetwork.addNode(poiNode);
				CyEdge myEdge = Cytoscape.getCyEdge(poiNode, node2, Semantics.INTERACTION, "pp", true); // Create a link between the two nodes and indicates that it is an interaction between two proteins (pp)
				myNetwork.addEdge(myEdge); // Add the link in the network
				// [/TEST]
		}
		
		// Add the network to the mediator
		Mediator.Instance().addNetwork(myNetwork);
		
		
	}
		
}
