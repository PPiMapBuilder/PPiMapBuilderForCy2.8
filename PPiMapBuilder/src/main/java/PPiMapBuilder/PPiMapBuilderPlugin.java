package PPiMapBuilder;

import cytoscape.Cytoscape;
import cytoscape.plugin.CytoscapePlugin;

/**
 * 
 * @author pidupuis
 *
 */
public class PPiMapBuilderPlugin extends CytoscapePlugin {
	
	/**
	 * Default constructor
	 * 		Add a launcher to the "Plugins" menu
	 */
	public PPiMapBuilderPlugin() {
		Cytoscape.getDesktop().getCyMenus().getMenuBar().getMenu("Plugins").add(PPiMapBuilderMenu.Instance());
	}

}
