package PPiMapBuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;

public class PPiMapBuilderMediator implements PropertyChangeListener {
	
	private static PPiMapBuilderMediator _instance = null; // Instance for singleton pattern
	private ArrayList<CyNetwork> myNetworks = new ArrayList<CyNetwork>();
	
	// Instance for singleton pattern
	public static PPiMapBuilderMediator Instance() {
		if (_instance == null)
			_instance = new PPiMapBuilderMediator();
		return _instance;
	}
	
	// Constructor
	private PPiMapBuilderMediator() {
		Cytoscape.getDesktop().getSwingPropertyChangeSupport().addPropertyChangeListener(CytoscapeDesktop.NETWORK_VIEW_CREATED, this); // We add the property to handle the view creation
	}

	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equalsIgnoreCase(CytoscapeDesktop.NETWORK_VIEW_CREATED)) { // If a view is created for a network
			for (CyNetwork n : myNetworks) { // We look if this network is one of the plugin network
				if (((CyNetworkView)e.getNewValue()).getNetwork() == n) {
					new PPiMapBuilderView(((CyNetworkView)e.getNewValue()).getNetwork()); // If it is the case, we create a particular view for this network
				}
			}
		}
	}
	
	public void addNetwork(CyNetwork myNetwork) {
		this.myNetworks.add(myNetwork); // We stock the network
		new PPiMapBuilderView(myNetwork); // We create a view for this network
	}
}
