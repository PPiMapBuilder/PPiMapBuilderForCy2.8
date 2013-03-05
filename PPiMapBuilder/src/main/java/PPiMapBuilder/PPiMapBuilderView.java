package PPiMapBuilder;

import giny.model.Edge;
import giny.model.GraphPerspective;
import giny.model.Node;
import giny.model.RootGraph;
import giny.view.EdgeView;
import giny.view.GraphViewChangeListener;
import giny.view.NodeView;

import java.awt.Component;
import java.awt.Paint;
import java.awt.event.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.layout.CyLayoutAlgorithm;
import cytoscape.layout.CyLayouts;
import cytoscape.view.CyEdgeView;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CyNodeView;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualStyle;
import ding.view.DGraphView;
import ding.view.EdgeContextMenuListener;
import ding.view.NodeContextMenuListener;

public class PPiMapBuilderView implements CyNetworkView {

	private CyNetworkView myView;
	
	public PPiMapBuilderView(CyNetwork myNetwork) {
		
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

	// Override of every implementable method
	public boolean addContextMethod(String arg0, String arg1, String arg2, Object[] arg3, ClassLoader arg4) {
		return myView.addContextMethod(arg0, arg1, arg2, arg3, arg4);
	}
	
	public EdgeView addEdgeView(int arg0) {
		return myView.addEdgeView(arg0);
	}
	
	public EdgeView addEdgeView(String arg0, int arg1) {
		return myView.addEdgeView(arg0, arg1);
	}
	
	public void addGraphViewChangeListener(GraphViewChangeListener arg0) {
		myView.addGraphViewChangeListener(arg0);
	}
	
	public NodeView addNodeView(int arg0) {
		return myView.addNodeView(arg0);
	}
	
	public NodeView addNodeView(String arg0, int arg1) {
		return myView.addNodeView(arg0, arg1);
	}
	
	public NodeView addNodeView(int arg0, NodeView arg1) {
		return myView.addNodeView(arg0, arg1);
	}
	
	public void disableEdgeSelection() {
		myView.disableEdgeSelection();
	}
	
	public void disableNodeSelection() {
		myView.disableNodeSelection();
	}
	
	public int edgeCount() {
		return myView.edgeCount();
	}
	
	public boolean edgeSelectionEnabled() {
		return myView.edgeSelectionEnabled();
	}
	
	public void enableEdgeSelection() {
		myView.enableEdgeSelection();
	}
	
	public void enableNodeSelection() {
		myView.enableNodeSelection();
	}
	
	public void fitContent() {
		myView.fitContent();
	}
	
	public Object[] getAllEdgePropertyData(int arg0) {
		return myView.getAllEdgePropertyData(arg0);
	}
	
	public Object[] getAllNodePropertyData(int arg0) {
		return myView.getAllNodePropertyData(arg0);
	}
	
	public Paint getBackgroundPaint() {
		return myView.getBackgroundPaint();
	}
	
	public Component getComponent() {
		return myView.getComponent();
	}
	
	public Object[] getContextMethods(String arg0, boolean arg1) {
		return myView.getContextMethods(arg0, arg1);
	}
	
	public Object[] getContextMethods(String arg0, Object[] arg1) {
		return myView.getContextMethods(arg0, arg1);
	}
	
	public boolean getEdgeBooleanProperty(int arg0, int arg1) {
		return myView.getEdgeBooleanProperty(arg0, arg1);
	}
	
	public double getEdgeDoubleProperty(int arg0, int arg1) {
		return myView.getEdgeDoubleProperty(arg0, arg1);
	}
	
	public float getEdgeFloatProperty(int arg0, int arg1) {
		return myView.getEdgeFloatProperty(arg0, arg1);
	}
	
	public int getEdgeIntProperty(int arg0, int arg1) {
		return myView.getEdgeIntProperty(arg0, arg1);
	}
	
	public Object getEdgeObjectProperty(int arg0, int arg1) {
		return myView.getEdgeObjectProperty(arg0, arg1);
	}
	
	public EdgeView getEdgeView(int arg0) {
		return myView.getEdgeView(arg0);
	}
	
	public EdgeView getEdgeView(Edge arg0) {
		return myView.getEdgeView(arg0);
	}
	
	public int getEdgeViewCount() {
		return myView.getEdgeViewCount();
	}
	
	@SuppressWarnings("rawtypes")
	public Iterator getEdgeViewsIterator() {
		return myView.getEdgeViewsIterator();
	}
	
	@SuppressWarnings("rawtypes")
	public List getEdgeViewsList() {
		return myView.getEdgeViewsList();
	}
	
	@SuppressWarnings("rawtypes")
	public List getEdgeViewsList(Node arg0, Node arg1) {
		return myView.getEdgeViewsList();
	}
	
	@SuppressWarnings("rawtypes")
	public List getEdgeViewsList(int arg0, int arg1, boolean arg2) {
		return myView.getEdgeViewsList(arg0, arg1, arg2);
	}
	
	public GraphPerspective getGraphPerspective() {
		return myView.getGraphPerspective();
	}
	
	public String getIdentifier() {
		return myView.getIdentifier();
	}
	
	public boolean getNodeBooleanProperty(int arg0, int arg1) {
		return myView.getNodeBooleanProperty(arg0, arg1);
	}
	
	public double getNodeDoubleProperty(int arg0, int arg1) {
		return myView.getNodeDoubleProperty(arg0, arg1);
	}
	
	public float getNodeFloatProperty(int arg0, int arg1) {
		return myView.getNodeFloatProperty(arg0, arg1);
	}
	
	public int getNodeIntProperty(int arg0, int arg1) {
		return myView.getNodeIntProperty(arg0, arg1);
	}
	
	public Object getNodeObjectProperty(int arg0, int arg1) {
		return myView.getNodeObjectProperty(arg0, arg1);
	}
	
	public NodeView getNodeView(Node arg0) {
		return myView.getNodeView(arg0);
	}
	
	public NodeView getNodeView(int arg0) {
		return myView.getNodeView(arg0);
	}
	
	public int getNodeViewCount() {
		return myView.getNodeViewCount();
	}
	
	@SuppressWarnings("rawtypes")
	public Iterator getNodeViewsIterator() {
		return myView.getNodeViewsIterator();
	}
	
	public RootGraph getRootGraph() {
		return myView.getRootGraph();
	}
	
	public int[] getSelectedEdgeIndices() {
		return myView.getSelectedEdgeIndices();
	}
	
	@SuppressWarnings("rawtypes")
	public List getSelectedEdges() {
		return myView.getSelectedEdges();
	}
	
	public int[] getSelectedNodeIndices() {
		return myView.getSelectedNodeIndices();
	}
	
	@SuppressWarnings("rawtypes")
	public List getSelectedNodes() {
		return myView.getSelectedNodes();
	}
	
	public double getZoom() {
		return myView.getZoom();
	}
	
	public boolean hideGraphObject(Object arg0) {
		return myView.hideGraphObject(arg0);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean hideGraphObjects(List arg0) {
		return myView.hideGraphObjects(arg0);
	}
	
	public int nodeCount() {
		return myView.nodeCount();
	}
	
	public boolean nodeSelectionEnabled() {
		return myView.nodeSelectionEnabled();
	}
	
	public EdgeView removeEdgeView(EdgeView arg0) {
		return myView.removeEdgeView(arg0);
	}
	
	public EdgeView removeEdgeView(Edge arg0) {
		return myView.removeEdgeView(arg0);
	}
	
	public EdgeView removeEdgeView(int arg0) {
		return myView.removeEdgeView(arg0);
	}
	
	public void removeGraphViewChangeListener(GraphViewChangeListener arg0) {
		myView.removeGraphViewChangeListener(arg0);
	}
	
	public NodeView removeNodeView(NodeView arg0) {
		return myView.removeNodeView(arg0);
	}
	
	public NodeView removeNodeView(Node arg0) {
		return myView.removeNodeView(arg0);
	}
	
	public NodeView removeNodeView(int arg0) {
		return myView.removeNodeView(arg0);
	}
	
	public void setAllEdgePropertyData(int arg0, Object[] arg1) {
		myView.setAllEdgePropertyData(arg0, arg1);
	}
	
	public void setAllNodePropertyData(int arg0, Object[] arg1) {
		myView.setAllNodePropertyData(arg0, arg1);
	}
	
	public void setBackgroundPaint(Paint arg0) {
		myView.setBackgroundPaint(arg0);
	}
	
	public boolean setEdgeBooleanProperty(int arg0, int arg1, boolean arg2) {
		return myView.setEdgeBooleanProperty(arg0, arg1, arg2);
	}
	
	public boolean setEdgeDoubleProperty(int arg0, int arg1, double arg2) {
		return myView.setEdgeDoubleProperty(arg0, arg1, arg2);
	}
	
	public boolean setEdgeFloatProperty(int arg0, int arg1, float arg2) {
		return myView.setEdgeFloatProperty(arg0, arg1, arg2);
	}
	
	public boolean setEdgeIntProperty(int arg0, int arg1, int arg2) {
		return myView.setEdgeIntProperty(arg0, arg1, arg2);
	}
	
	public boolean setEdgeObjectProperty(int arg0, int arg1, Object arg2) {
		return myView.setEdgeObjectProperty(arg0, arg1, arg2);
	}
	
	public void setIdentifier(String arg0) {
		myView.setIdentifier(arg0);
	}
	
	public boolean setNodeBooleanProperty(int arg0, int arg1, boolean arg2) {
		return myView.setNodeBooleanProperty(arg0, arg1, arg2);
	}
	
	public boolean setNodeDoubleProperty(int arg0, int arg1, double arg2) {
		return myView.setNodeDoubleProperty(arg0, arg1, arg2);
	}
	
	public boolean setNodeFloatProperty(int arg0, int arg1, float arg2) {
		return myView.setNodeFloatProperty(arg0, arg1, arg2);
	}
	
	public boolean setNodeIntProperty(int arg0, int arg1, int arg2) {
		return myView.setNodeIntProperty(arg0, arg1, arg2);
	}
	
	public boolean setNodeObjectProperty(int arg0, int arg1, Object arg2) {
		return myView.setNodeObjectProperty(arg0, arg1, arg2);
	}
	
	public void setZoom(double arg0) {
		myView.setZoom(arg0);
	}
	
	public boolean showGraphObject(Object arg0) {
		return myView.showGraphObject(arg0);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean showGraphObjects(List arg0) {
		return myView.showGraphObjects(arg0);
	}
	
	public void updateView() {
		myView.updateView();
	}
	
	public void addEdgeContextMenuListener(EdgeContextMenuListener arg0) {
		myView.addEdgeContextMenuListener(arg0);
	}
	
	public void addNodeContextMenuListener(NodeContextMenuListener arg0) {
		myView.addNodeContextMenuListener(arg0);
	}
	
	public void applyLayout(CyLayoutAlgorithm arg0) {
		myView.applyLayout(arg0);
	}
	
	public void applyLayout(CyLayoutAlgorithm arg0, CyNode[] arg1, CyEdge[] arg2) {
		myView.applyLayout(arg0, arg1, arg2);
	}
	
	public void applyLayout(CyLayoutAlgorithm arg0, CyNodeView[] arg1, CyEdgeView[] arg2) {
		myView.applyLayout(arg0, arg1, arg2);
	}
	
	public void applyLayout(CyLayoutAlgorithm arg0, int[] arg1, int[] arg2) {
		myView.applyLayout(arg0, arg1, arg2);
	}
	
	public void applyLockedLayout(CyLayoutAlgorithm arg0, CyNode[] arg1, CyEdge[] arg2) {
		myView.applyLockedLayout(arg0, arg1, arg2);
	}
	
	public void applyLockedLayout(CyLayoutAlgorithm arg0, CyNodeView[] arg1, CyEdgeView[] arg2) {
		myView.applyLockedLayout(arg0, arg1, arg2);
	}
	
	public void applyLockedLayout(CyLayoutAlgorithm arg0, int[] arg1, int[] arg2) {
		myView.applyLockedLayout(arg0, arg1, arg2);
	}
	
	public boolean applyVizMap(CyEdge arg0) {
		return myView.applyVizMap(arg0);
	}
	
	public boolean applyVizMap(EdgeView arg0) {
		return myView.applyVizMap(arg0);
	}
	
	public boolean applyVizMap(CyNode arg0) {
		return myView.applyVizMap(arg0);
	}
	
	public boolean applyVizMap(NodeView arg0) {
		return myView.applyVizMap(arg0);
	}
	
	public boolean applyVizMap(CyEdge arg0, VisualStyle arg1) {
		return myView.applyVizMap(arg0, arg1);
	}
	
	public boolean applyVizMap(EdgeView arg0, VisualStyle arg1) {
		return myView.applyVizMap(arg0, arg1);
	}
	
	public boolean applyVizMap(CyNode arg0, VisualStyle arg1) {
		return myView.applyVizMap(arg0, arg1);
	}
	
	public boolean applyVizMap(NodeView arg0, VisualStyle arg1) {
		return myView.applyVizMap(arg0, arg1);
	}
	
	public void applyVizmapper(VisualStyle arg0) {
		myView.applyVizmapper(arg0);
	}
	
	public Object getClientData(String arg0) {
		return myView.getClientData(arg0);
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getClientDataNames() {
		return myView.getClientDataNames();
	}
	
	public CyNetwork getNetwork() {
		return myView.getNetwork();
	}
	
	public String getTitle() {
		return myView.getTitle();
	}
	
	public CyNetworkView getView() {
		return myView.getView();
	}
	
	public boolean getVisualMapperEnabled() {
		return myView.getVisualMapperEnabled();
	}
	
	public VisualStyle getVisualStyle() {
		return myView.getVisualStyle();
	}
	
	public VisualMappingManager getVizMapManager() {
		return myView.getVizMapManager();
	}
	
	public void putClientData(String arg0, Object arg1) {
		myView.putClientData(arg0, arg1);
	}
	
	public void redrawGraph(boolean arg0, boolean arg1) {
		myView.redrawGraph(arg0, arg1);
	}
	
	public void removeEdgeContextMenuListener(EdgeContextMenuListener arg0) {
		myView.removeEdgeContextMenuListener(arg0);
	}
	
	public void removeNodeContextMenuListener(NodeContextMenuListener arg0) {
		myView.removeNodeContextMenuListener(arg0);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setSelected(CyNode[] arg0) {
		return myView.setSelected(arg0);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setSelected(NodeView[] arg0) {
		return myView.setSelected(arg0);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setSelected(CyEdge[] arg0) {
		return myView.setSelected(arg0);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setSelected(EdgeView[] arg0) {
		return myView.setSelected(arg0);
	}
	
	public void setTitle(String arg0) {
		myView.setTitle(arg0);
	}
	
	public void setVisualMapperEnabled(boolean arg0) {
		myView.setVisualMapperEnabled(arg0);
	}
	
	public void setVisualStyle(String arg0) {
		myView.setVisualStyle(arg0);
	}
	
	public void toggleVisualMapperEnabled() {
		myView.toggleVisualMapperEnabled();
	}
}
