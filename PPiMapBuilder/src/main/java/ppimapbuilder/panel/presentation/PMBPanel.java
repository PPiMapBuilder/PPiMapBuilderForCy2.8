package ppimapbuilder.panel.presentation;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Set;
import javax.swing.*;
import ppimapbuilder.network.NetworkControl;
import ppimapbuilder.network.presentation.PMBNode;
import ppimapbuilder.panel.PMBPanelControl;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Component;
import net.miginfocom.swing.MigLayout;

/** @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL */
public class PMBPanel extends JPanel {

	private static final long serialVersionUID = 1;
	
	private static PMBPanel _instance = null; // Instance of the ppimapbuilder panel to prevent several instances 

	private JLabel lblGeneName, lblGeneNameValue, lblUniprotId, lblUniprotIdValue, lblDescription, lblDescriptionValue, lblCellularComponent, lblCcList, lblBiologicalProcesses, lblBpList, lblMolecularFunction, lblMfList;  
	private JPanel proteinInfoPanel, geneOntologyPanel;
	private JScrollPane proteinInfoScroll, geneOntologyScroll;
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Creates a panel and add the different components
	 */
	private PMBPanel() {
		super();
		this.setName("PPiMapBuilder");
		
		Font title = new Font("Lucida Grande", Font.BOLD, 14);

		// Protein info panel
		proteinInfoPanel = new JPanel();
		proteinInfoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		proteinInfoPanel.setLayout(new BoxLayout(proteinInfoPanel, BoxLayout.Y_AXIS));
		proteinInfoPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		// Protein scroll pane
		proteinInfoScroll = new JScrollPane(proteinInfoPanel);
		proteinInfoScroll.setBorder(new TitledBorder(null, "Protein", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		proteinInfoScroll.setOpaque(false);
		
		// Protein labels
		lblGeneName = new JLabel();
		lblGeneName.setFont(title);
		proteinInfoPanel.add(lblGeneName);
		
		lblGeneNameValue = new JLabel();
		proteinInfoPanel.add(lblGeneNameValue);
		
		lblUniprotId = new JLabel();
		lblUniprotId.setFont(title);
		proteinInfoPanel.add(lblUniprotId);
		
		lblUniprotIdValue = new JLabel();
		proteinInfoPanel.add(lblUniprotIdValue);
		
		lblDescription = new JLabel();
		lblDescription.setFont(title);
		proteinInfoPanel.add(lblDescription);
		
		lblDescriptionValue = new JLabel();
		proteinInfoPanel.add(lblDescriptionValue);
		
		proteinInfoScroll.setVisible(false);
		
		
		// Gene ontology panel
		geneOntologyPanel = new JPanel();
		geneOntologyPanel.setLayout(new BoxLayout(geneOntologyPanel, BoxLayout.Y_AXIS));
		geneOntologyPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		// Gene ontology scroll pane
		geneOntologyScroll = new JScrollPane(geneOntologyPanel);
		geneOntologyScroll.setBorder(new TitledBorder(null, "Gene Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		geneOntologyScroll.setOpaque(false);
		
		// Gene ontology labels
		lblCellularComponent = new JLabel();
		lblCellularComponent.setFont(title);
		geneOntologyPanel.add(lblCellularComponent);
		
		lblCcList = new JLabel();
		geneOntologyPanel.add(lblCcList);
		
		lblBiologicalProcesses = new JLabel();
		lblBiologicalProcesses.setFont(title);
		geneOntologyPanel.add(lblBiologicalProcesses);
		
		lblBpList = new JLabel();
		geneOntologyPanel.add(lblBpList);
		
		lblMolecularFunction = new JLabel();
		lblMolecularFunction.setFont(title);
		geneOntologyPanel.add(lblMolecularFunction);
		
		lblMfList = new JLabel();
		geneOntologyPanel.add(lblMfList);
		
		geneOntologyScroll.setVisible(false);
		
		
		// Main panel
		setLayout(new MigLayout("inset 5", "[grow]", "[125px:n,grow][grow]"));
		
		add(proteinInfoScroll, "cell 0 0,grow");
		add(geneOntologyScroll, "cell 0 1,grow");
		
		setPreferredSize(new Dimension(300, 600));
		setOpaque(false);

		
		//Demo
		lblGeneName.setText("Name");
		lblGeneNameValue.setText("val");
		lblUniprotId.setText("Uniprot");
		lblUniprotIdValue.setText("val");
		lblDescription.setText("Description");
		lblDescriptionValue.setText("<html>val<br/><br/>d</html>");
		
		lblCellularComponent.setText("Cellular Component");
		lblCcList.setText("val");
		lblBiologicalProcesses.setText("Biological Process");
		lblBpList.setText("val");
		lblMolecularFunction.setText("Molecular function");
		lblMfList.setText("<html>val<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>m</html>");
		
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
	
	/** Method which modify the panel display according to the selected nodes */
	public void update() {
		CyNetwork current_network = Cytoscape.getCurrentNetwork(); // Retrieve the current network

		if (current_network != null) { // If this network exits
			
			proteinInfoScroll.setVisible(true);
			geneOntologyScroll.setVisible(true);

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
								
				/* TODO : Retrieve node information from data panel (when the attributes will be done...) */
				PMBNode myNode = PMBPanelControl.getNode(current_network, selectedNodes.iterator().next().getIdentifier());
				
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
				proteinInfoScroll.setVisible(false);
				geneOntologyScroll.setVisible(false);

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
