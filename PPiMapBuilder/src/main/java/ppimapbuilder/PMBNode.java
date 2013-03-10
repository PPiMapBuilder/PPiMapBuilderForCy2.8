package ppimapbuilder;

import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniref.UniRefEntry;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import giny.model.RootGraph;
import cytoscape.CyNode;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PMBNode extends CyNode {
	
	private String geneName;
	private String uniprotId;
	
	/**
	 * Constructor which inherits from the CyNode class
	 * This class is necessary and is used by the other ppimapbuilder node constructor
	 * @param root
	 * @param rootGraphIndex
	 */
	public PMBNode(RootGraph root, int rootGraphIndex) {
		super(root, rootGraphIndex);
	}

	/**
	 * Constructor which creates a ppimapbuilder node directly from a CyNode
	 * @param myNode
	 */
	public PMBNode(CyNode myNode, String geneName, String uniprotId) {
		this(myNode.getRootGraph(), myNode.getRootGraphIndex());
		
		this.geneName = geneName;
		this.uniprotId = uniprotId;
		
		//Create entry retrival service
	    EntryRetrievalService entryRetrievalService = UniProtJAPI.factory.getEntryRetrievalService();
	    
	    //Retrieve UniProt entry by its accession number
	    UniProtEntry entry = (UniProtEntry) entryRetrievalService.getUniProtEntry("Q96AZ6");
	    
	    //If entry with a given accession number is not found, entry will be equal null
	    if (entry != null) {
	        System.out.println("entry = " + entry.getUniProtId().getValue());
	    }
	    
	    //Retrieve UniRef entry by its ID
	    UniRefEntry uniRefEntry = entryRetrievalService.getUniRefEntry("UniRef90_Q12979-2");
	    
	    if (uniRefEntry != null) {
	        System.out.println("Representative Member Organism = " +
	          uniRefEntry.getRepresentativeMember().getSourceOrganism().getValue());
	    }
	}
		

}
