package ppimapbuilder.network;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import ppimapbuilder.LoadingWindow;
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
	
	private static NetworkControl _instance = null; // Instance for singleton pattern
	
	private CyNetwork myNetwork; // Result network
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Add the property changes managed by the mediator
	 */
	private NetworkControl() {
		Cytoscape.getDesktop().getSwingPropertyChangeSupport().addPropertyChangeListener(CytoscapeDesktop.NETWORK_VIEW_CREATED, this); // We add the property to handle the view creation
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
			String id = ((CyNetworkView)e.getNewValue()).getNetwork().getIdentifier();
			Boolean isPmbNetwork = Cytoscape.getNetworkAttributes().getBooleanAttribute(id, "PMB");
			if (isPmbNetwork != null) {
				if (isPmbNetwork) {	
					addViewToNetwork(((CyNetworkView)e.getNewValue()).getNetwork()); // If it is the case, we create a particular view for this network
				}
				else {
					Cytoscape.getVisualMappingManager().setVisualStyle("default"); // By default, we put the default visual style
				}
			}
			else {
				Cytoscape.getVisualMappingManager().setVisualStyle("default"); // By default, we put the default visual style
			}
		}
	}
	
	
	public void addViewToNetwork(CyNetwork myNetwork) {
		new PMBView(myNetwork); // We create a view for this network
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
				myNetwork = Cytoscape.createNetwork("network"+System.currentTimeMillis(), false); // Creation of a network

				CyAttributes attrNetwork = Cytoscape.getNetworkAttributes();
				attrNetwork.setUserEditable("PMB", false);
				attrNetwork.setUserVisible("PMB", false);
				attrNetwork.setAttribute(myNetwork.getIdentifier(), "PMB", true);
				
				CyAttributes attrNode = Cytoscape.getNodeAttributes();
				
				attrNode.setUserEditable("Gene name", false);
				attrNode.setUserEditable("Uniprot id", false);
				attrNode.setUserEditable("Taxonomy id", false);
				attrNode.setUserEditable("Protein description", false);
				attrNode.setUserEditable("Biological process", false);
				attrNode.setUserEditable("Cellular component", false);
				attrNode.setUserEditable("Molecular function", false);
				attrNode.setUserEditable("annotation.GO BIOLOGICAL_PROCESS", false);
				attrNode.setUserEditable("annotation.GO CELLULAR_COMPONENT", false);
				attrNode.setUserEditable("annotation.GO MOLECULAR_FUNCTION", false);
				attrNode.setUserEditable("canonicalName", false);
				
				attrNode.setUserVisible("GoLoaded", false);
				attrNode.setUserEditable("GoLoaded", false);
				
				CyAttributes attrEdge = Cytoscape.getEdgeAttributes();
				
				attrEdge.setUserEditable("Source database", false);
				attrEdge.setUserEditable("Origin", false);
				attrEdge.setUserEditable("Experimental system", false);
				attrEdge.setUserEditable("Pubmed id", false);
				attrEdge.setUserEditable("Predicted from", false);
				attrEdge.setUserEditable("canonicalName", false);
				
				// Store final protein IDs 
				ArrayList<Integer> ptnIDs = new ArrayList<Integer>();
				
				// Creation of the network components
				for (String id : poiList) { // For each protein of interest
					
					SQLResult res = NetworkAbstraction.getAllData(id, dbList, orgaList, refOrganism);
					
					if (res != null) {
												
						CyNode A, B;
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

								A = createNode(fields.get("p1_gene_name"), fields.get("p1_uniprot_id"), fields.get("p1_taxid"));
								B = createNode(fields.get("p2_gene_name"), fields.get("p2_uniprot_id"), fields.get("p2_taxid"));
								myNetwork.addNode(A);
								myNetwork.addNode(B);

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

							} catch (Exception e) {
								JOptionPane.showMessageDialog(Cytoscape.getDesktop(),"Error!", "Error when network loading. Please try again later or contact the administrator", JOptionPane.ERROR_MESSAGE);
								Cytoscape.destroyNetwork(myNetwork);
								return;
							}
						}
					}
				}
				
				if (myNetwork.getNodeCount() == 0) {
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
	
	public CyNode createNode(String geneName, String uniprotId, String taxid) {
		CyNode myNode = Cytoscape.getCyNode(geneName, true);
		
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "Gene name", geneName); // Add the gene name as node attribute
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "Uniprot id", uniprotId);
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "Taxonomy id", taxid);
		
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "GoLoaded", false);
		
		return myNode;
	}
	
	/** Method handling clicks on the network */
	public void mousePressed(Point targetedPoint) {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the current network

		DGraphView graph = ((DGraphView)Cytoscape.getCurrentNetworkView());
		
		if (graph.getPickedNodeView(targetedPoint) != null) { // If the click is on a node
			PMBPanelControl.updatePanel((CyNode) current_network.getSelectedNodes().iterator().next());
		}
		else if(graph.getPickedEdgeView(targetedPoint) != null) {
			PMBPanelControl.updatePanel((CyEdge) current_network.getSelectedEdges().iterator().next());
		}
		else PMBPanelControl.updatePanel(); //Clear the panel
	}
	
	/**
	 * 
	 * @param proteinDescription
	 * @param componentList
	 * @param processList
	 * @param functionList
	 */
	public void setGeneOntologyData(CyNode myNode, String proteinDescription, ArrayList<String> componentList, ArrayList<String> processList, ArrayList<String> functionList, ArrayList<String> componentIdList, ArrayList<String> processIdList, ArrayList<String> functionIdList) {
		CyAttributes attr = Cytoscape.getNodeAttributes();
		
		attr.setAttribute(myNode.getIdentifier(), "Protein description", proteinDescription);
		
		attr.setListAttribute(myNode.getIdentifier(), "Biological process", processList);
		attr.setListAttribute(myNode.getIdentifier(), "Cellular component", componentList);
		attr.setListAttribute(myNode.getIdentifier(), "Molecular function", functionList);

		attr.setListAttribute(myNode.getIdentifier(), "annotation.GO BIOLOGICAL_PROCESS", processIdList);
		attr.setListAttribute(myNode.getIdentifier(), "annotation.GO CELLULAR_COMPONENT", componentIdList);
		attr.setListAttribute(myNode.getIdentifier(), "annotation.GO MOLECULAR_FUNCTION", functionIdList);
		
		attr.setAttribute(myNode.getIdentifier(), "GoLoaded", true);
		
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
			
			int[] indices = network.getNodeIndicesArray();
			for(int i : indices) {
				CyNode node = (CyNode) network.getNode(i);
				
				if(!Cytoscape.getNodeAttributes().getBooleanAttribute(node.getIdentifier(), "GoLoaded")) {
					PMBPanelControl.setStatus("Loading GO ("+(indices.length-(java.lang.Math.abs(i)))+"/"+indices.length+")...");//nodes.indexOf(node)+"/"+nodes.size()+")...");
					String uniprotId = Cytoscape.getNodeAttributes().getStringAttribute(node.getIdentifier(), "Uniprot id").trim();
					UniProtEntry entry = (UniProtEntry) entryRetrievalService.getUniProtEntry(uniprotId); //Retrieve UniProt entry by its accession number

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

						setGeneOntologyData(node, proteinDescription, componentList, processList, functionList, componentIdList, processIdList, functionIdList);
					} 
					else Cytoscape.getNodeAttributes().setAttribute(node.getIdentifier(), "GoLoaded", true); //setGeneOntologyData(node, null, null, null, null, null, null, null); 
				}
			}
		} catch(Exception e) {System.out.println("[Error thread gene ontology]");e.printStackTrace();}
		
		//System.out.println("GO done!");
		PMBPanelControl.setStatus("Loading GO finished!");
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) { e.printStackTrace();	}
		PMBPanelControl.setStatus("");
	}

}
