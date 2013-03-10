package ppimapbuilder.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Set;
import javax.swing.*;
import ppimapbuilder.Mediator;
import ppimapbuilder.PMBNode;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import javax.swing.border.TitledBorder;
import java.awt.Component;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PMBPanel extends JPanel {

	private static final long serialVersionUID = 1;
	
	private static PMBPanel _instance = null; // Instance of the ppimapbuilder panel to prevent several instances 
	private Mediator myMediator;
	
	private JLabel lblGeneName, lblGeneNameValue, lblUniprotId, lblUniprotIdValue, lblDescription, lblDescriptionValue, lblCellularComponent, lblCcList, lblBiologicalProcesses, lblBpList, lblMolecularFunction, lblMfList;  
	private JPanel proteinInfoPanel, geneOntologyPanel;
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Creates a panel and add the different components
	 */
	private PMBPanel() {
		super();
		this.setName("PPiMapBuilder");
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Protein label
		
		proteinInfoPanel = new JPanel();
		proteinInfoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		proteinInfoPanel.setLayout(new BoxLayout(proteinInfoPanel, BoxLayout.Y_AXIS));
		proteinInfoPanel.setBorder(new TitledBorder(null, "Protein", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		proteinInfoPanel.setPreferredSize(new Dimension(300, 300));
		
		lblGeneName = new JLabel();
		lblGeneName.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		proteinInfoPanel.add(lblGeneName);
		
		lblGeneNameValue = new JLabel();
		proteinInfoPanel.add(lblGeneNameValue);
		
		lblUniprotId = new JLabel();
		lblUniprotId.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		proteinInfoPanel.add(lblUniprotId);
		
		lblUniprotIdValue = new JLabel();
		proteinInfoPanel.add(lblUniprotIdValue);
		
		lblDescription = new JLabel();
		lblDescription.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		proteinInfoPanel.add(lblDescription);
		
		lblDescriptionValue = new JLabel();
		proteinInfoPanel.add(lblDescriptionValue);
		
		proteinInfoPanel.setVisible(false);
		
		// Gene Ontology panel
		geneOntologyPanel = new JPanel();
		geneOntologyPanel.setLayout(new BoxLayout(geneOntologyPanel, BoxLayout.Y_AXIS));
		geneOntologyPanel.setBorder(new TitledBorder(null, "Gene Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		geneOntologyPanel.setPreferredSize(new Dimension(300, 300));
		
		lblCellularComponent = new JLabel();
		lblCellularComponent.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		geneOntologyPanel.add(lblCellularComponent);
		
		lblCcList = new JLabel();
		geneOntologyPanel.add(lblCcList);
		
		lblBiologicalProcesses = new JLabel();
		lblBiologicalProcesses.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		geneOntologyPanel.add(lblBiologicalProcesses);
		
		lblBpList = new JLabel();
		geneOntologyPanel.add(lblBpList);
		
		lblMolecularFunction = new JLabel();
		lblMolecularFunction.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		geneOntologyPanel.add(lblMolecularFunction);
		
		lblMfList = new JLabel();
		geneOntologyPanel.add(lblMfList);
		
		geneOntologyPanel.setVisible(false);
		
		// Main panel
		this.add(proteinInfoPanel);
		this.add(geneOntologyPanel);
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
			
			proteinInfoPanel.setVisible(true);
			geneOntologyPanel.setVisible(true);

			@SuppressWarnings("unchecked") // Delete the warnings for the getSelectedNodes() method
			Set<CyNode> selectedNodes = current_network.getSelectedNodes(); // Retrieve selected Nodes
			
			if (selectedNodes.size() == 1) { // If one node is selected
				
				// Reinitialization of the values
				lblGeneName.setText("HUGO Gene Name");
				lblGeneNameValue.setText("");
				lblUniprotId.setText("Uniprot Accesion");
				lblUniprotIdValue.setText("");
				lblDescription.setText("Description");
				lblDescriptionValue.setText("");
				lblCellularComponent.setText("");
				lblCcList.setText("");
				lblBiologicalProcesses.setText("");
				lblBpList.setText("");
				lblMolecularFunction.setText("");
				lblMfList.setText("");
								
				//PMBNode myNode = (PMBNode) selectedNodes.iterator().next();
				myMediator = Mediator.Instance();
				
				PMBNode myNode = myMediator.getNode(selectedNodes.iterator().next().getIdentifier());
				
				lblGeneNameValue.setText(myNode.getGeneName());
				lblUniprotIdValue.setText(myNode.getUniprotId());
				lblDescriptionValue.setText(myNode.getProteinDescription());
				
				if (!myNode.getComponentList().isEmpty()) {
					lblCellularComponent.setText("Cellular Component");	
					lblCcList.setText("<html><ul>");
					for (String str : myNode.getComponentList())
						lblCcList.setText(lblCcList.getText()+"<li>"+str+"</li>");
					lblCcList.setText(lblCcList.getText()+"</ul></html>");
				}
				
				if (!myNode.getProcessList().isEmpty()) {
					lblBiologicalProcesses.setText("Biological Processes");	
					lblBpList.setText("<html><ul>");
					for (String str : myNode.getProcessList())
						lblBpList.setText(lblBpList.getText()+"<li>"+str+"</li>");
					lblBpList.setText(lblBpList.getText()+"</ul></html>");
				}
				
				if (!myNode.getFunctionList().isEmpty()) {
					lblMolecularFunction.setText("Molecular Functions");
					lblMfList.setText("<html><ul>");
					for (String str : myNode.getFunctionList())
						lblMfList.setText(lblMfList.getText()+"<li>"+str+"</li>");
					lblMfList.setText(lblMfList.getText()+"</ul></html>");
				}

			}
			else {
				proteinInfoPanel.setVisible(false);
				geneOntologyPanel.setVisible(false);

				lblGeneName.setText("");
				lblGeneNameValue.setText("");
				lblUniprotId.setText("");
				lblUniprotIdValue.setText("");
				lblDescription.setText("");
				lblDescriptionValue.setText("");
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
