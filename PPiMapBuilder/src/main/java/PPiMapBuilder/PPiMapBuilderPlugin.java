package PPiMapBuilder;

import cytoscape.Cytoscape;
import cytoscape.plugin.CytoscapePlugin;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PPiMapBuilderPlugin extends CytoscapePlugin {
	
	private PPiMapBuilderMenu myMenu = PPiMapBuilderMenu.Instance(); // Main menu for the plugin
	
	/**
	 * Default constructor
	 * Add a launcher to the "Plugins" menu
	 */
	public PPiMapBuilderPlugin() {
		Cytoscape.getDesktop().getCyMenus().getMenuBar().getMenu("Plugins").add(myMenu); // Add the menu to the plugins menu
	}

}
