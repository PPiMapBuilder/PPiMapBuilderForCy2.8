package ppimapbuilder.network;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import ppimapbuilder.LoadingWindow;
import ppimapbuilder.network.presentation.PMBNode;
import ppimapbuilder.network.presentation.PMBView;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameControl;
import ppimapbuilder.panel.PMBPanelControl;
import ppimapbuilder.ppidb.api.SQLResult;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.dbx.go.Go;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import cytoscape.CyEdge;
import cytoscape.CyNode;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
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
		if(!this.myNetworks.get(myNetwork).contains(myNode)) 
			this.myNetworks.get(myNetwork).add(myNode); // We store the node
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
	
	/**
	 * 
	 * @param poiList
	 * @param dbList
	 * @param orgaList
	 * @param refOrganism
	 */
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
				
				// Store final protein IDs 
				ArrayList<Integer> ptnIDs = new ArrayList<Integer>();
				
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
								
								if (!fields.get("p1_uniprot_id").equals(id) && !ptnIDs.contains(Integer.parseInt(fields.get("p1_id"))))
										ptnIDs.add(Integer.parseInt(fields.get("p1_id")));
								if (!fields.get("p2_uniprot_id").equals(id) && !ptnIDs.contains(Integer.parseInt(fields.get("p2_id"))))
									ptnIDs.add(Integer.parseInt(fields.get("p2_id")));

								A = new PMBNode(Cytoscape.getCyNode(fields.get("p1_gene_name"), true), fields.get("p1_uniprot_id"), fields.get("p1_taxid"));
								B = new PMBNode(Cytoscape.getCyNode(fields.get("p2_gene_name"), true), fields.get("p2_uniprot_id"), fields.get("p2_taxid"));
								myNetwork.addNode(A);
								myNetwork.addNode(B);

								addNode(myNetwork, A);
								addNode(myNetwork, B);

								//Create Edges
								interaction = Cytoscape.getCyEdge(A, B, Semantics.INTERACTION, "pp", true);

								String idInt = interaction.getIdentifier();
								CyAttributes attrInt = Cytoscape.getEdgeAttributes();
								String[] attrs = {"Source database", "Experimental system", "Pubmed id"};
								String[] fieldNames = {"srcdb", "expsys", "pubmed"};
								
								for(int i = 0; i < attrs.length; i++){
									String attr = ((String)attrInt.getAttribute(idInt, attrs[i]));
									String field = (String) fields.get(fieldNames[i]);
									
									if(attr != null && !attr.isEmpty() && !attr.contains(field)) attr += "; "+field;
									else attr = field;
									
									attrInt.setAttribute(idInt, attrs[i], attr);
								}

								String organismA = (String) fields.get("p1_org_name");
								String organismB = (String) fields.get("p2_org_name");							

								if (organismA.equalsIgnoreCase(organismB)) { // If these proteins come from the same organism
									if (Integer.parseInt(fields.get("p1_taxid")) == refOrganism ) { // If this organism is the reference one
										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", organismA);
										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", "");
									}
									else { // If this organism is another organism
										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");
										Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", organismA);
									}
								}
								else {
									Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");
									Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", organismA+"/"+organismB);
								}

								myNetwork.addEdge(interaction);

							} catch (UnknownHostException e) {
								JOptionPane.showMessageDialog(Cytoscape.getDesktop(),"Connection error!", "Connection to Uniprot database failed", JOptionPane.ERROR_MESSAGE);
								myNetworks.remove(myNetwork);
								Cytoscape.destroyNetwork(myNetwork);
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
					// Add secondary interactions
					CyNode A, B;
					CyEdge interaction;
					HashMap<String, String> fieldsSecond;
					
					HashMap<Integer, HashMap<String, String>> resSecond = NetworkAbstraction.getSecondInteractors(ptnIDs);
					// For each line
					for(Integer row: resSecond.keySet()) {
						//Get fields
						fieldsSecond =  resSecond.get(row);
												
						A = Cytoscape.getCyNode(fieldsSecond.get("interactorA"));
						B = Cytoscape.getCyNode(fieldsSecond.get("interactorB"));
						
						interaction = Cytoscape.getCyEdge(A, B, Semantics.INTERACTION, "pp", true);
						
						String idInt = interaction.getIdentifier();
						CyAttributes attrInt = Cytoscape.getEdgeAttributes();
						String[] attrs = {"Source database", "Experimental system", "Pubmed id"};
						String[] fieldNames = {"srcdb", "expsys", "pubmed"};
						
						for(int i = 0; i < attrs.length; i++){
							String attr = ((String)attrInt.getAttribute(idInt, attrs[i]));
							String field = (String) fieldsSecond.get(fieldNames[i]);
							
							if(attr != null && !attr.isEmpty() && !attr.contains(field)) attr += "; "+field;
							else attr = field;
							
							attrInt.setAttribute(idInt, attrs[i], attr);
						}
						
						String organismA = (String) fieldsSecond.get("p1_org_name");
						String organismB = (String) fieldsSecond.get("p2_org_name");							

						if (organismA.equalsIgnoreCase(organismB)) { // If these proteins come from the same organism
							if (Integer.parseInt(fieldsSecond.get("taxidA")) == refOrganism ) { // If this organism is the reference one
								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", organismA);
								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", "");
							}
							else { // If this organism is another organism
								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");
								Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", organismA);
							}
						}
						else {
							Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");
							Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Predicted from", organismA+"/"+organismB);
						}
						
						myNetwork.addEdge(interaction);	
					}
					
					// Add a view to the network
					addViewToNetwork(myNetwork);
					
					(Executors.newFixedThreadPool(1)).submit(new Runnable() {
						public void run() {
							fillGOData(myNetwork);
						}
					});
				}
			}
		};
	}
	
	/** Method handling clicks on the network */
	public void mousePressed(Point targetedPoint) {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the current network

		DGraphView graph = ((DGraphView)Cytoscape.getCurrentNetworkView());
		
		if (graph.getPickedNodeView(targetedPoint) != null) { // If the click is on a node
			Set<CyNode> selectedNodes = current_network.getSelectedNodes(); // Retrieve selected Nodes
			PMBNode myNode = getNode(current_network, selectedNodes.iterator().next().getIdentifier());
			PMBPanelControl.updatePanel(myNode);
		}
		else if(graph.getPickedEdgeView(targetedPoint) != null) {
			Set<CyEdge> selectedEdge = current_network.getSelectedEdges(); // Retrieve selected Nodes
			PMBPanelControl.updatePanel(selectedEdge.iterator().next());
		}
		else PMBPanelControl.updatePanel(); //Clear the panel
		
		 // We update the panel
	}
	
	/**
	 * 
	 * @param network
	 */
	public void fillGOData( CyNetwork network) {
		//System.out.println("Searching gene ontologies...");
		
		try {
			//Create entry retrieval service
			EntryRetrievalService entryRetrievalService = UniProtJAPI.factory.getEntryRetrievalService(); 
			
			String proteinDescription;
			ArrayList<String> componentList;
			ArrayList<String> processList;
			ArrayList<String> functionList;
			
			ArrayList<String> componentIdList;
			ArrayList<String> processIdList;
			ArrayList<String> functionIdList;

//			int[] indices = network.getNodeIndicesArray();
//			for(int i : network.getNodeIndicesArray()) {
//				PMBNode node = (PMBNode)network.getNode(i);
//				PMBPanelControl.setStatus("Loading GO ("+i+"/"+indices.length+")...");
//				
			ArrayList<PMBNode> nodes = myNetworks.get(network);
			for(PMBNode node : nodes) {
				if(!node.isGoLoaded()) {
					PMBPanelControl.setStatus("Loading GO ("+nodes.indexOf(node)+"/"+nodes.size()+")...");
					UniProtEntry entry = (UniProtEntry) entryRetrievalService.getUniProtEntry(node.getUniprotId()); //Retrieve UniProt entry by its accession number

					if (entry != null) { // If there is an entry
						if(entry.getProteinDescription().getRecommendedName().getFields().size() > 0)
							proteinDescription = entry.getProteinDescription().getRecommendedName().getFields().get(0).getValue();
						else proteinDescription = "";

						// Instantiates every gene ontology list
						componentList = new ArrayList<String>();
						processList = new ArrayList<String>();
						functionList = new ArrayList<String>();
						
						componentIdList = new ArrayList<String>();
						processIdList = new ArrayList<String>();
						functionIdList = new ArrayList<String>();

						for (Go myGo : entry.getGoTerms()) { // For each gene ontology
							
							if (myGo.getOntologyType().toString().equalsIgnoreCase("C")) {
								// If it is a cellular component

								componentList.add(myGo.getGoTerm().getValue());
								componentIdList.add(myGo.getGoId().toString());
							}
							if (myGo.getOntologyType().toString().equalsIgnoreCase("P")) {
								// If it is biological processes

								processList.add(myGo.getGoTerm().getValue());
								processIdList.add(myGo.getGoId().toString());
							}
							if (myGo.getOntologyType().toString().equalsIgnoreCase("F")) {
								// If it is a molecular function

								functionList.add(myGo.getGoTerm().getValue());
								functionIdList.add(myGo.getGoId().toString());
							}
						}

						node.setGeneOntologyData(proteinDescription, componentList, processList, functionList, componentIdList, processIdList, functionIdList);
					} 
					else node.setGeneOntologyData(null, null, null, null, null, null, null); 
				}
			}
		} catch(Exception e) {System.out.println("[Error thread gene ontology]");e.printStackTrace();}
		
		System.out.println("GO done!");
		PMBPanelControl.setStatus("Loading GO finished!");
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) { e.printStackTrace();	}
		PMBPanelControl.setStatus("");
	}

}
