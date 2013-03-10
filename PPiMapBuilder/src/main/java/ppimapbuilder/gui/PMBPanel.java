package ppimapbuilder.gui;

import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PMBPanel extends JPanel {

	private static final long serialVersionUID = 1;
	
	private static PMBPanel _instance = null; // Instance of the ppimapbuilder panel to prevent several instances 
	
	/* Panel components  */
	private JLabel selectedNode;
	// [!] Need to be completed...
	/* ---------------- */
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Creates a panel and add the different components
	 */
	private PMBPanel() {
		super();
		this.setName("PPiMapBuilder"); // Change the name of the panel
		
		selectedNode = new JLabel(); // Creation of a label
		this.add(selectedNode); // Add the label into the panel
	}
	
	/**
	 * Method to access the unique instance of PMBPanel
	 * @return _instance
	 */
	public static PMBPanel Instance() {
		if (_instance == null)
			_instance = new PMBPanel();
		return _instance;
	}
	
	/**
	 * Method which modify the panel display according to the selected nodes
	 */
	public void update() {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the current network
		if (current_network != null) { // If this network exits
			
			@SuppressWarnings("unchecked") // Delete the warnings for the getSelectedNodes() method
			Set<CyNode> selectedNodes = current_network.getSelectedNodes(); // Retrieve selected Nodes
			
			if (selectedNodes.size() != 0) { // If nodes are selected
				
				/* Example of the panel update */
				String str = new String("");
				Iterator<CyNode> it = selectedNodes.iterator(); // Put the selected nodes in a list
				while (it.hasNext()) { // For each node
					CyNode aNode = it.next();
					str+=aNode.getIdentifier()+" "; // Add its name to the string
				}
				selectedNode.setText(str); // change the label according to the string to print the node names
				/* ---------------------------- */
				
			}
		}
	}
}
