package PPiMapBuilder;

import giny.view.NodeView;
import java.awt.event.*;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.layout.CyLayouts;
import cytoscape.view.CyNetworkView;
import ding.view.DGraphView;

public class PPiMapBuilderMediatorView {

	private CyNetworkView myView;
	
	public PPiMapBuilderMediatorView(CyNetwork myNetwork) {
		
		this.myView = Cytoscape.createNetworkView(myNetwork);
		((DGraphView) this.myView).getCanvas().addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {}

			public void mousePressed(MouseEvent arg0) {}

			public void mouseExited(MouseEvent arg0) {}

			public void mouseEntered(MouseEvent arg0) {}

			public void mouseClicked(MouseEvent e) {
				CyNetworkView view=Cytoscape.getCurrentNetworkView();	
				NodeView nv =((DGraphView) view).getPickedNodeView(e.getPoint ());
				if(nv != null) {
					updatePanel(); // We update the panel
				}
			}
		});
		this.myView.setZoom(3); // Zoom the network view (because there are only two nodes)
		this.myView.updateView(); // Update the view
		this.myView.applyLayout(CyLayouts.getLayout("force-directed")); // Use the "Force directed" layout
	}
	
	public void updatePanel() {
		// For now there is one action, but we can extend this method with different conditions according that we click on node or edge for example
		PPiMapBuilderPanel.Instance().update();
	}

}
