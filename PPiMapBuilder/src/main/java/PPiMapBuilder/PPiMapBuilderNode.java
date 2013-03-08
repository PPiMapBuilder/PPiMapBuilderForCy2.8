package PPiMapBuilder;

import giny.model.RootGraph;
import cytoscape.CyNode;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PPiMapBuilderNode extends CyNode {
	
	/**
	 * Constructor which inherits from the CyNode class
	 * This class is necessary and is used by the other PPiMapBuilder node constructor
	 * @param root
	 * @param rootGraphIndex
	 */
	public PPiMapBuilderNode(RootGraph root, int rootGraphIndex) {
		super(root, rootGraphIndex);
	}

	/**
	 * Constructor which creates a PPiMapBuilder node directly from a CyNode
	 * @param myNode
	 */
	public PPiMapBuilderNode(CyNode myNode) {
		this(myNode.getRootGraph(), myNode.getRootGraphIndex());
	}
		

}
