package ppimapbuilder.network;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;

import ppimapbuilder.LoadingWindow;
import ppimapbuilder.network.presentation.PMBNode;
import ppimapbuilder.network.presentation.PMBView;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameAbstraction;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameControl;
import ppimapbuilder.panel.PMBPanelControl;
import ppimapbuilder.panel.presentation.PMBPanel;
import ppimapbuilder.ppidb.api.SQLResult;
import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;

/**
 *  
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class NetworkControl implements PropertyChangeListener {
	
	private static NetworkControl _instance = null; // Instance for singleton pattern
	
	//private ArrayList<CyNetwork> myNetworks = new ArrayList<CyNetwork>(); // List of networks created by the plugin
	//private ArrayList<PMBNode> myNodes = new ArrayList<PMBNode>();
	
	private LinkedHashMap<CyNetwork, ArrayList<PMBNode>> myNetworks;
	
	//private PMBPanel myPanel;
	//private PMBView myView; // View for a network (for creation or update)
	
	private CyNetwork myNetwork; // Result network
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Add the property changes managed by the mediator
	 */
	private NetworkControl() {
		Cytoscape.getDesktop().getSwingPropertyChangeSupport().addPropertyChangeListener(CytoscapeDesktop.NETWORK_VIEW_CREATED, this); // We add the property to handle the view creation
		myNetworks = new LinkedHashMap<CyNetwork, ArrayList<PMBNode>>();
	}
	
	/**
	 * Method to access the unique instance of NetworkControl
	 * @return _instance
	 */
	public static NetworkControl Instance() {
		if (_instance == null)
			_instance = new NetworkControl();
		return _instance;
	}
	
	/**
	 * PropertyChange which handle the network_view_created event to add the specific ppimapbuilder view
	 */
	public void propertyChange(PropertyChangeEvent e) {
		
		// If a network view is destroyed and then recreated, we have to add the link between this view and the panel :
		if (e.getPropertyName().equalsIgnoreCase(CytoscapeDesktop.NETWORK_VIEW_CREATED)) { // If a view is created for a network
			
			/* check keySet...*/
			for (CyNetwork n : myNetworks.keySet()) { // We look if this network is one of the plugin network
				if (((CyNetworkView)e.getNewValue()).getNetwork() == n) {
					new PMBView(((CyNetworkView)e.getNewValue()).getNetwork()); // If it is the case, we create a particular view for this network
				}
			}
		}
	}
	
	/**
	 * Method which add a network to the network list
	 * @param myNetwork
	 */
	public void addNetwork(CyNetwork myNetwork) {
		System.out.println("#1");
		this.myNetworks.put(myNetwork, new ArrayList<PMBNode>()); // We stock the network
		System.out.println("#2");
	}
	
	private void addViewToNetwork(CyNetwork myNetwork) {
		new PMBView(myNetwork); // We create a view for this network
		System.out.println("#3");
	}
	
	/**
	 * Method which add a node to the network list
	 * @param myNode
	 */
	public void addNode(CyNetwork myNetwork, PMBNode myNode) {
		/* check if myNetwork exists */
		this.myNetworks.get(myNetwork).add(myNode); // We stock the node
	}
	
	/**
	 * Method which call the update() method from the ppimapbuilder panel
	 */
	public void updatePanel() {
		// For now there is one action, but we can extend this method with different conditions according that we click on node or edge for example
		PMBPanelControl.updatePanel();
	}
	
	public ArrayList<PMBNode> getMyNodes(CyNetwork myNetwork) {
		System.out.println("#4");
		return myNetworks.get(myNetwork);
	}
	
	public PMBNode getNode(CyNetwork myNetwork, String identifier) {
		System.out.println("#5");
		for (PMBNode n : this.getMyNodes(myNetwork)) {
			System.out.println("#6");
			if (n.getGeneName().equals(identifier)) {
				System.out.println("#7");
				return n;
			}
		}
		return null;
	}
	
	// TODO : distinguish the reference organism !	
	public void createNetwork(final ArrayList<String> poiList, final ArrayList<String> dbList, final ArrayList<Integer> orgaList) {
		
		// Will display the loading window until the treatment is done
		new LoadingWindow("Generating network...") {
			public void process() {
			
				NetworkCreationFrameControl.closeFrame();
				
				// Creation of the network
				myNetwork = Cytoscape.createNetwork("network", false); // Creation of a network // TODO : change the name
				addNetwork(myNetwork);
				
				// Creation of the network components
				for (String id : poiList) { // For each protein of interest
					
					SQLResult res = NetworkAbstraction.getAllData(id, dbList, orgaList);
					
					PMBNode A, B;
					CyEdge interaction;
					LinkedHashMap<String, String> fields;
					
					// For each line
					for(String row: res.keySet()) {
						//Get fields
						fields = res.getData(row);
						
						//Create Nodes
						try {
							
							A = new PMBNode(Cytoscape.getCyNode(fields.get("interactorA"), true), fields.get("uniprotidA"));
							B = new PMBNode(Cytoscape.getCyNode(fields.get("interactorB"), true), fields.get("uniprotidB"));
							myNetwork.addNode(A);
							myNetwork.addNode(B);
							
							
							addNode(myNetwork, A);
							addNode(myNetwork, B);
							
							//Create Edges
							interaction = Cytoscape.getCyEdge(A, B, Semantics.INTERACTION, "pp", true);
							myNetwork.addEdge(interaction);
						
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Error : "+e1.getLocalizedMessage());
							e1.printStackTrace();
							return;
						}
					}
				}
				
				// Add a view to the network
				addViewToNetwork(myNetwork);
			}
		};
	}


}
