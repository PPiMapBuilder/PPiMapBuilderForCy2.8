package PPiMapBuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PPiMapBuilderMediator implements PropertyChangeListener {
	
	private static PPiMapBuilderMediator _instance = null; // Instance for singleton pattern
	
	private ArrayList<CyNetwork> myNetworks = new ArrayList<CyNetwork>(); // List of networks created by the plugin
	
	@SuppressWarnings("unused")
	private PPiMapBuilderView myView; // View for a network (for creation or update)
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Add the property changes managed by the mediator
	 */
	private PPiMapBuilderMediator() {
		Cytoscape.getDesktop().getSwingPropertyChangeSupport().addPropertyChangeListener(CytoscapeDesktop.NETWORK_VIEW_CREATED, this); // We add the property to handle the view creation
	}
	
	/**
	 * Method to access the unique instance of PPiMapBuilderMediator
	 * @return _instance
	 */
	public static PPiMapBuilderMediator Instance() {
		if (_instance == null)
			_instance = new PPiMapBuilderMediator();
		return _instance;
	}
	
	/**
	 * PropertyChange which handle the network_view_created event to add the specific PPiMapBuilder view
	 */
	public void propertyChange(PropertyChangeEvent e) {
		
		// If a network view is destroyed and then recreated, we have to add the link between this view and the panel :
		if (e.getPropertyName().equalsIgnoreCase(CytoscapeDesktop.NETWORK_VIEW_CREATED)) { // If a view is created for a network
			for (CyNetwork n : myNetworks) { // We look if this network is one of the plugin network
				if (((CyNetworkView)e.getNewValue()).getNetwork() == n) {
					myView = new PPiMapBuilderView(((CyNetworkView)e.getNewValue()).getNetwork()); // If it is the case, we create a particular view for this network
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
		myView = new PPiMapBuilderView(myNetwork); // We create a view for this network
	}
}
