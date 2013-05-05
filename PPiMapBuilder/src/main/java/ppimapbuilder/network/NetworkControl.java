package ppimapbuilder.network;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.JOptionPane;
import ppimapbuilder.LoadingWindow;
import ppimapbuilder.network.presentation.PMBNode;
import ppimapbuilder.network.presentation.PMBView;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameControl;
import ppimapbuilder.panel.PMBPanelControl;
import ppimapbuilder.ppidb.api.SQLResult;
import cytoscape.CyEdge;
import cytoscape.CyNode;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;
import ding.view.DGraphView;

/**
 *  
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class NetworkControl implements PropertyChangeListener {
	
	private static final CyNetworkView DGraphView = null;

	private static NetworkControl _instance = null; // Instance for singleton pattern
	
	private LinkedHashMap<CyNetwork, ArrayList<PMBNode>> myNetworks;
	
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
		this.myNetworks.put(myNetwork, new ArrayList<PMBNode>()); // We stock the network
	}
	
	private void addViewToNetwork(CyNetwork myNetwork) {
		new PMBView(myNetwork); // We create a view for this network
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
	 * Get all the nodes of a network managed by the plugin
	 * @param myNetwork
	 * @return
	 */
	public ArrayList<PMBNode> getMyNodes(CyNetwork myNetwork) {
		return myNetworks.get(myNetwork);
	}
	
	/**
	 * Get a node in a network by its identifier
	 * @param myNetwork
	 * @param identifier
	 * @return
	 */
	public PMBNode getNode(CyNetwork myNetwork, String identifier) {
		for (PMBNode n : this.getMyNodes(myNetwork)) {
			if (n.getGeneName().equals(identifier)) {
				return n;
			}
		}
		return null;
	}
	
	public void createNetwork(final ArrayList<String> poiList, final ArrayList<String> dbList, final ArrayList<Integer> orgaList, final int refOrganism) {
		
		// Will display the loading window until the treatment is done
		new LoadingWindow("Generating network...") {
			public void process() {
				
				NetworkCreationFrameControl.closeFrame();
				
				// Creation of the network
				myNetwork = Cytoscape.createNetwork("network", false); // Creation of a network // TODO : change the name
				addNetwork(myNetwork);
								
				Cytoscape.getEdgeAttributes().setUserEditable("Source database", false);
				Cytoscape.getEdgeAttributes().setUserEditable("Origin", false);
				Cytoscape.getEdgeAttributes().setUserEditable("Experimental system", false);
				Cytoscape.getEdgeAttributes().setUserEditable("Pubmed id", false);
				Cytoscape.getEdgeAttributes().setUserEditable("Predicted from", false);
				Cytoscape.getEdgeAttributes().setUserEditable("canonicalName", false);
				
				// Creation of the network components
				for (String id : poiList) { // For each protein of interest
					
					SQLResult res = NetworkAbstraction.getAllData(id, dbList, orgaList, refOrganism);
					
					if (res != null) {
						
						PMBNode A, B;
						CyEdge interaction;
						LinkedHashMap<String, String> fields;

						// For each line
						for(String row: res.keySet()) {
							//Get fields
							fields = res.getData(row);

							//Create Nodes
							try {

								A = new PMBNode(Cytoscape.getCyNode(fields.get("p1_gene_name"), true), fields.get("p1_uniprot_id"), fields.get("p1_taxid"));
								B = new PMBNode(Cytoscape.getCyNode(fields.get("p2_gene_name"), true), fields.get("p2_uniprot_id"), fields.get("p2_taxid"));
								myNetwork.addNode(A);
								myNetwork.addNode(B);

								addNode(myNetwork, A);
								addNode(myNetwork, B);

								//Create Edges
								interaction = Cytoscape.getCyEdge(A, B, Semantics.INTERACTION, "pp", true);

//								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Source database", (String) fields.get("srcdb"));
//								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Experimental system", (String) fields.get("expsys"));
//								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Pubmed id", (String) fields.get("pubmed"));
//
//								String organismA = (String) fields.get("orgaA");
//								String organismB = (String) fields.get("orgaB");							
//
//								if (organismA.equalsIgnoreCase(organismB)) { // If these proteins come from the same organism
//									if (Integer.parseInt(fields.get("taxidA")) == refOrganism ) { // If this organism is the reference one
//										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", organismA);
//										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", "");
//									}
//									else { // If this organism is another organism
//										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");
//										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", organismA);
//									}
//								}
//								else {
//									Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");
//									Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", organismA+"/"+organismB);
//								}

								myNetwork.addEdge(interaction);

							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Error : "+e1.getLocalizedMessage());
								e1.printStackTrace();
								return;
							}
						}

					}
				}
				
				if (myNetwork.getNodeCount() == 0) {
					myNetworks.remove(myNetwork);
					Cytoscape.destroyNetwork(myNetwork);
				}
				else {
					// Add a view to the network
					addViewToNetwork(myNetwork);
				}
			}
		};
	}
	
	/** Method handling clicks on the network */
	public void mousePressed(Point targetedPoint) {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the current network

		Set<CyNode> selectedNodes = current_network.getSelectedNodes(); // Retrieve selected Nodes
		DGraphView graph = ((DGraphView)Cytoscape.getCurrentNetworkView());
		
		if(graph.getPickedEdgeView(targetedPoint) != null) {
			PMBPanelControl.updatePanel();
		}
		else if (graph.getPickedNodeView(targetedPoint) != null) { // If the click is on a node
			PMBNode myNode = getNode(current_network, selectedNodes.iterator().next().getIdentifier());
			PMBPanelControl.updatePanel(myNode);
		}
		else PMBPanelControl.updatePanel(); //Clear the panel
		
		 // We update the panel
	}

}
