package ppimapbuilder;

import java.net.UnknownHostException;

import ppimapbuilder.menu.presentation.PMBMenu;
import ppimapbuilder.network.presentation.PMBNode;
import ppimapbuilder.network.presentation.PMBView;
import ppimapbuilder.panel.PMBPanelControl;
import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import cytoscape.plugin.CytoscapePlugin;
import cytoscape.visual.CalculatorCatalog;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualStyle;

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
	
		try {
			Cytoscape.getEdgeAttributes().setUserEditable("Origin", false);

			CyNetwork myNetwork = Cytoscape.createNetwork("network", false); // Creation of a network // TODO : change the name

			PMBNode A = new PMBNode(Cytoscape.getCyNode("PPID", true), "123456", "9606");
			PMBNode B = new PMBNode(Cytoscape.getCyNode("HSP90", true), "567890", "9606");
			myNetwork.addNode(A);
			myNetwork.addNode(B);
			CyEdge interaction = Cytoscape.getCyEdge(A, B, Semantics.INTERACTION, "pp", true);
			myNetwork.addEdge(interaction);
			
			Cytoscape.getEdgeAttributes().setAttribute(interaction.getIdentifier(), "Origin", "Interolog");

			PMBView myView = new PMBView(myNetwork);
			
			VisualMappingManager manager = Cytoscape.getVisualMappingManager();
			CalculatorCatalog catalog = manager.getCalculatorCatalog();
			VisualStyle vs = catalog.getVisualStyle("PPiMapBuilder");
			if (vs == null) {
				vs = myView.createVisualStyle(myNetwork);
				catalog.addVisualStyle(vs);
			}			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	
	}

}
