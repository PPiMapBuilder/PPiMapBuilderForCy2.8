package PPiMapBuilder;

import giny.model.RootGraph;
import cytoscape.CyNode;

public class PPiMapBuilderNode extends CyNode {

	public PPiMapBuilderNode(CyNode myNode) {
		this(myNode.getRootGraph(), myNode.getRootGraphIndex());
	}
	
	public PPiMapBuilderNode(RootGraph root, int rootGraphIndex) {
		super(root, rootGraphIndex);
	}
	

}
