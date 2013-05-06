package ppimapbuilder.network.presentation;

import java.net.UnknownHostException;
import java.util.ArrayList;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.dbx.go.Go;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import giny.model.RootGraph;
import cytoscape.CyNode;
import cytoscape.Cytoscape;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PMBNode extends CyNode {
	
	private String geneName;
	private String uniprotId;
	
	private String proteinDescription;
	private ArrayList<String> componentList;
	private ArrayList<String> processList;
	private ArrayList<String> functionList;
	
	private boolean goLoaded;
	
	/**
	 * Constructor which inherits from the CyNode class
	 * This class is necessary and is used by the other ppimapbuilder node constructor
	 * @param root
	 * @param rootGraphIndex
	 */
	public PMBNode(RootGraph root, int rootGraphIndex) {
		super(root, rootGraphIndex);
		this.goLoaded = false;
	}

	/**
	 * Constructor which creates a ppimapbuilder node directly from a CyNode
	 * @param myNode
	 */
	public PMBNode(CyNode myNode, String uniprotId, String taxid) throws UnknownHostException {
		this(myNode.getRootGraph(), myNode.getRootGraphIndex());
		
		Cytoscape.getNodeAttributes().setUserEditable("Gene name", false);
		Cytoscape.getNodeAttributes().setUserEditable("Uniprot id", false);
		Cytoscape.getNodeAttributes().setUserEditable("Taxonomy id", false);
		Cytoscape.getNodeAttributes().setUserEditable("canonicalName", false);
		
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "Gene name", myNode.getIdentifier()); // Add the gene name as node attribute
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "Uniprot id", uniprotId);
		Cytoscape.getNodeAttributes().setAttribute(myNode.getIdentifier(), "Taxonomy id", taxid);
		
		this.geneName = myNode.getIdentifier(); // The identifier and the gene name are the same information
		this.uniprotId = uniprotId; // Uniprot Id which is used to retrieve the uniprot entry
		
		this.goLoaded = false;
	}

	/**
	 * 
	 * @return gene name
	 */
	public String getGeneName() {
		return Cytoscape.getNodeAttributes().getStringAttribute(this.getIdentifier(), "Gene name");
	}
	
	/**
	 * 
	 * @return uniprot id
	 */
	public String getUniprotId() {
		return Cytoscape.getNodeAttributes().getStringAttribute(this.getIdentifier(), "Uniprot id");
	}
	
	/**
	 * 
	 * @return protein description
	 */
	public String getProteinDescription() {
		return proteinDescription;
	}
	
	/**
	 * 
	 * @return cellular components
	 */
	public ArrayList<String> getComponentList() {
		return componentList;
	}
	
	/**
	 * 
	 * @return biological processes
	 */
	public ArrayList<String> getProcessList() {
		return processList;
	}
	
	/**
	 * 
	 * @return molecular functions
	 */
	public ArrayList<String> getFunctionList() {
		return functionList;
	}

	/**
	 * 
	 * @param geneName
	 */
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	/**
	 * 
	 * @param uniprotId
	 */
	public void setUniprotId(String uniprotId) {
		this.uniprotId = uniprotId;
	}

	/**
	 * 
	 * @param proteinDescription
	 * @param componentList
	 * @param processList
	 * @param functionList
	 */
	public void setGeneOntologyData(String proteinDescription, ArrayList<String> componentList, ArrayList<String> processList, ArrayList<String> functionList) {
		this.proteinDescription = proteinDescription;
		this.componentList = componentList;
		this.processList = processList;
		this.functionList = functionList;
		this.goLoaded = true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isGoLoaded() {
		return goLoaded;
	}
	
	/**
	 * 
	 */
	@Override
	public String getIdentifier() {
		return this.geneName;
	}
	

}
