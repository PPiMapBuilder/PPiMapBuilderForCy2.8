package ppimapbuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import ppimapbuilder.gui.PMBPanel;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class Mediator implements PropertyChangeListener {
	
	private static Mediator _instance = null; // Instance for singleton pattern
	
	private ArrayList<CyNetwork> myNetworks = new ArrayList<CyNetwork>(); // List of networks created by the plugin
	private PMBPanel myPanel = PMBPanel.Instance();
	@SuppressWarnings("unused")
	private PMBView myView; // View for a network (for creation or update)
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Add the property changes managed by the mediator
	 */
	private Mediator() {
		Cytoscape.getDesktop().getSwingPropertyChangeSupport().addPropertyChangeListener(CytoscapeDesktop.NETWORK_VIEW_CREATED, this); // We add the property to handle the view creation
	}
	
	/**
	 * Method to access the unique instance of Mediator
	 * @return _instance
	 */
	public static Mediator Instance() {
		if (_instance == null)
			_instance = new Mediator();
		return _instance;
	}
	
	/**
	 * PropertyChange which handle the network_view_created event to add the specific ppimapbuilder view
	 */
	public void propertyChange(PropertyChangeEvent e) {
		
		// If a network view is destroyed and then recreated, we have to add the link between this view and the panel :
		if (e.getPropertyName().equalsIgnoreCase(CytoscapeDesktop.NETWORK_VIEW_CREATED)) { // If a view is created for a network
			for (CyNetwork n : myNetworks) { // We look if this network is one of the plugin network
				if (((CyNetworkView)e.getNewValue()).getNetwork() == n) {
					myView = new PMBView(((CyNetworkView)e.getNewValue()).getNetwork()); // If it is the case, we create a particular view for this network
				}
			}
		}
	}
	
	/**
	 * Method which add a network to the network list
	 * @param myNetwork
	 */
	public void addNetwork(CyNetwork myNetwork) {
		this.myNetworks.add(myNetwork); // We stock the network
		myView = new PMBView(myNetwork); // We create a view for this network
	}
	
	/**
	 * Method which call the update() method from the ppimapbuilder panel
	 */
	public void updatePanel() {
		// For now there is one action, but we can extend this method with different conditions according that we click on node or edge for example
		myPanel.update();
	}
	

}
