package PPiMapBuilder;

import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;

/**
 * 
 * @author pidupuis
 *
 */
public class PPiMapBuilderPanel extends JPanel {

	private static final long serialVersionUID = 1;
	private static PPiMapBuilderPanel _instance = null; // Instance for singleton pattern
	private JLabel selectedNode;
	
	private PPiMapBuilderPanel() {
		this.setName("PPiMapBuilder"); // Change the name of the panel
		
		selectedNode = new JLabel(); // Creation of a label
		this.add(selectedNode);
	}
	
	// Instance for singleton pattern
	public static PPiMapBuilderPanel Instance() {
		if (_instance == null)
			_instance = new PPiMapBuilderPanel();
		return _instance;
	}
	
	public void update() {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the currentNetwork
		if (current_network != null) { // If exists
			
			Set<CyNode> selectedNodes = current_network.getSelectedNodes(); // Retrieve selected Nodes
			
			if (selectedNodes.size() != 0) { // If nodes are selected
				
				// We update the panel label
				String str = new String("");
				Iterator<CyNode> it = selectedNodes.iterator();
				while (it.hasNext()) {
					CyNode aNode = it.next();
					str+=aNode.getIdentifier()+" ";
				}
				selectedNode.setText(str);
			}
		}
	}
}
