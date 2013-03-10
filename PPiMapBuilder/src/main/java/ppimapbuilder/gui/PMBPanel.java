package ppimapbuilder.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import ppimapbuilder.Mediator;
import ppimapbuilder.PMBNode;

import com.sun.corba.se.impl.orbutil.graph.Node;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PMBPanel extends JPanel {

	private static final long serialVersionUID = 1;
	
	private static PMBPanel _instance = null; // Instance of the ppimapbuilder panel to prevent several instances 
	private Mediator myMediator;
	
	private JLabel lblProtein, lblGeneName, lblUniprotId, lblDescription, lblGeneOntology, lblCellularComponent, lblCcList, lblBiologicalProcesses, lblBpList, lblMolecularFunction, lblMfList;  
	
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Creates a panel and add the different components
	 */
	private PMBPanel() {
		super();
		this.setName("PPiMapBuilder");
		
		JPanel proteinInfoPanel = new JPanel();
		
		setLayout(new MigLayout("", "[1px][][][][]", "[1px][][][][][][][][][][][][][]"));
		
		lblProtein = new JLabel("");
		lblProtein.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		add(lblProtein, "cell 2 2");
		
		lblGeneName = new JLabel("");
		add(lblGeneName, "cell 2 3");
		
		lblUniprotId = new JLabel("");
		add(lblUniprotId, "cell 2 4");
		
		lblDescription = new JLabel("");
		add(lblDescription, "cell 2 5");
		
		lblGeneOntology = new JLabel("");
		lblGeneOntology.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		add(lblGeneOntology, "cell 2 7");
		
		lblCellularComponent = new JLabel("");
		add(lblCellularComponent, "cell 2 8");
		
		lblCcList = new JLabel("");
		add(lblCcList, "cell 2 9");
		
		lblBiologicalProcesses = new JLabel();
		add(lblBiologicalProcesses, "cell 2 10");
		
		lblBpList = new JLabel("");
		add(lblBpList, "cell 2 11");
		
		lblMolecularFunction = new JLabel("");
		add(lblMolecularFunction, "cell 2 12");
		
		lblMfList = new JLabel("");
		add(lblMfList, "cell 2 13");
		
		this.setPreferredSize(new Dimension(300, 600));
		this.setMinimumSize(new Dimension(300, 600));
		
	}
	
	/**
	 * Method to access the unique instance of PMBPanel
	 * @return _instance
	 */
	public static PMBPanel Instance() {
		if (_instance == null)
			_instance = new PMBPanel();
		return _instance;
	}
	
	/**
	 * Method which modify the panel display according to the selected nodes
	 */
	public void update() {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the current network

		if (current_network != null) { // If this network exits
			
			@SuppressWarnings("unchecked") // Delete the warnings for the getSelectedNodes() method
			Set<CyNode> selectedNodes = current_network.getSelectedNodes(); // Retrieve selected Nodes
			
			if (selectedNodes.size() == 1) { // If one node is selected
								
				//PMBNode myNode = (PMBNode) selectedNodes.iterator().next();
				myMediator = Mediator.Instance();
				
				PMBNode myNode = myMediator.getNode(selectedNodes.iterator().next().getIdentifier());
				
				lblProtein.setText("Protein information");
				lblGeneName.setText(" - "+myNode.getGeneName());
				lblUniprotId.setText(myNode.getUniprotId());
				lblDescription.setText(myNode.getProteinDescription());
				lblGeneOntology.setText("Gene Ontology information");
				
				lblCellularComponent.setText("Cellular Component");				
				for (String str : myNode.getComponentList())
					lblCcList.setText(lblCcList.getText()+"\n"+str);
				
				lblBiologicalProcesses.setText("Biological Processes");				
				for (String str : myNode.getProcessList())
					lblBpList.setText(lblBpList.getText()+"\n"+str);
				
				lblMolecularFunction.setText("Molecular Functions");				
				for (String str : myNode.getFunctionList())
					lblMfList.setText(lblMfList.getText()+"\n"+str);

			}
			else {
				
				lblProtein.setText("");
				lblGeneName.setText("");
				lblUniprotId.setText("");
				lblDescription.setText("");
				lblGeneOntology.setText("");
				lblCellularComponent.setText("");
				lblCcList.setText("");
				lblBiologicalProcesses.setText("");
				lblBpList.setText("");
				lblMolecularFunction.setText("");
				lblMfList.setText("");
			}
				
		}
	}
}
