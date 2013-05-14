package ppimapbuilder;

import ppimapbuilder.menu.presentation.PMBMenu;
import ppimapbuilder.network.NetworkControl;
import ppimapbuilder.panel.PMBPanelControl;
import cytoscape.Cytoscape;
import cytoscape.plugin.CytoscapePlugin;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PMBPlugin extends CytoscapePlugin {
	
	private PMBMenu myMenu = PMBMenu.Instance(); // Main menu for the plugin
	
	/**
	 * Default constructor
	 * Add a launcher to the "Plugins" menu
	 */
	public PMBPlugin() {
		Cytoscape.getDesktop().getCyMenus().getMenuBar().getMenu("Plugins").add(myMenu); // Add the menu to the plugins menu
		PMBPanelControl.createPanel(); // Creates the PPiMapBuilder panel
	
		restoreInitState();
	}
	
	public void restoreInitState() {
		NetworkControl.Instance(); // Initialisation of the network control
	}

}
