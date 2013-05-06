package ppimapbuilder.panel.presentation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import ppimapbuilder.JLinkLabel;
import ppimapbuilder.network.presentation.PMBNode;
import ppimapbuilder.panel.presentation.ScrollablePanel.ScrollableSizeHint;
import cytoscape.CyEdge;
import cytoscape.Cytoscape;

import javax.swing.ScrollPaneConstants;


/** @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL */
public class PMBPanel extends JPanel {

	private static final long serialVersionUID = 1;

	/** Instance of the ppimapbuilder panel to prevent several instances  */
	private static PMBPanel _instance = null;

	private JLabel lblCellularComponent, lblBiologicalProcesses, lblMolecularFunction, lblGeneNameValue, lblDescriptionValue, lblUniprotIdValue;
	private JTextList lblCcList, lblBioList, lblFunList;
	private ScrollablePanel proteinInfoPanel, geneOntologyPanel;

	private JScrollPane proteinInfoScroll, geneOntologyScroll;

	private JPanel nodeInfoPanel;
	private JScrollPane edgeInfoPanel;
	
	private JLabel lblOriginValue;
	private JTextList lblSrcDbValue, lblExpsysValue, lblPublicationValue;
	


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
	 * Default constructor which is private to prevent several instances
	 * Creates a panel and add the different components
	 */
	private PMBPanel() {
		super();
		this.setName("PPiMapBuilder");

		initNodeInfoPanel();
		initEdgeInfoPanel();

		// Main panel
		setLayout(new BorderLayout());		
		setPreferredSize(new Dimension(300, 600));

		update();
	}


	/** Initialize the node info panel */
	public void initNodeInfoPanel() {
		nodeInfoPanel = new JPanel(new MigLayout("inset 5", "[grow]", "[158px:158px,grow 20][growprio 101,grow]"));

		JLabel lblGeneName, lblUniprotId, lblDescription;

		Font title = new Font("Lucida Grande", Font.BOLD, 14);

		// Protein info panel
		proteinInfoPanel = new ScrollablePanel();
		proteinInfoPanel.setScrollableWidth(ScrollableSizeHint.FIT);
		proteinInfoPanel.setScrollableHeight(ScrollableSizeHint.STRETCH);
		proteinInfoPanel.setScrollableBlockIncrement(ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 200);
		proteinInfoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		proteinInfoPanel.setLayout(new BoxLayout(proteinInfoPanel, BoxLayout.Y_AXIS));
		proteinInfoPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		// Protein scroll pane
		proteinInfoScroll = new JScrollPane(proteinInfoPanel);
		proteinInfoScroll.setOpaque(false);
		proteinInfoScroll.setBorder(new TitledBorder(null, "Protein", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		// Protein labels
		lblGeneName = new JLabel("HUGO Gene Name");
		lblGeneName.setFont(title);

		lblGeneNameValue = new JLabel();
		lblGeneNameValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

		lblUniprotId = new JLabel("Uniprot Accesion");
		lblUniprotId.setFont(title);

		lblUniprotIdValue = new JLinkLabel();
		lblUniprotIdValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

		lblDescription = new JLabel("Description");
		lblDescription.setFont(title);

		lblDescriptionValue = new JLabel();
		lblDescriptionValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

		proteinInfoPanel.add(lblGeneName);
		proteinInfoPanel.add(lblGeneNameValue);
		proteinInfoPanel.add(lblUniprotId);
		proteinInfoPanel.add(lblUniprotIdValue);
		proteinInfoPanel.add(lblDescription);
		proteinInfoPanel.add(lblDescriptionValue);
		
		
		// Gene ontology panel
		geneOntologyPanel = new ScrollablePanel();
		geneOntologyPanel.setScrollableWidth(ScrollableSizeHint.FIT);
		geneOntologyPanel.setScrollableHeight(ScrollableSizeHint.STRETCH);
		geneOntologyPanel.setScrollableBlockIncrement(ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 200);
		geneOntologyPanel.setLayout(new MigLayout("inset 0", "[grow]", "[20px][grow][20px][grow][20px][grow][growprio 101, grow]"));
		//geneOntologyPanel.setLayout(new BoxLayout(geneOntologyPanel, BoxLayout.Y_AXIS));
		geneOntologyPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		// Gene ontology scroll pane
		geneOntologyScroll = new JScrollPane(geneOntologyPanel);
		geneOntologyScroll.setOpaque(false);
		geneOntologyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		geneOntologyScroll.setBorder(new TitledBorder(null, "Gene Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		// Gene ontology labels
		lblCellularComponent = new JLabel("Cellular Component");
		lblCellularComponent.setFont(title);
		lblCellularComponent.setAlignmentY(Component.LEFT_ALIGNMENT);
		geneOntologyPanel.add(lblCellularComponent, "cell 0 0, grow");
		
		lblCcList = new JTextList();
		geneOntologyPanel.add(lblCcList, "cell 0 1, grow");

		lblBiologicalProcesses = new JLabel("Biological Processes");
		lblBiologicalProcesses.setFont(title);
		lblBiologicalProcesses.setAlignmentY(Component.LEFT_ALIGNMENT);
		geneOntologyPanel.add(lblBiologicalProcesses, "cell 0 2, grow");
		
		lblBioList = new JTextList();
		geneOntologyPanel.add(lblBioList, "cell 0 3, grow");

		lblMolecularFunction = new JLabel("Molecular Functions");
		lblMolecularFunction.setFont(title);
		lblMolecularFunction.setAlignmentY(Component.LEFT_ALIGNMENT);
		geneOntologyPanel.add(lblMolecularFunction, "cell 0 4, grow");
		
		lblFunList = new JTextList();
		geneOntologyPanel.add(lblFunList, "cell 0 5, grow");

		nodeInfoPanel.add(proteinInfoScroll, "cell 0 0,grow");
		nodeInfoPanel.add(geneOntologyScroll, "cell 0 1,grow");

		//Demo
		//lblGeneNameValue.setText("val");
		//lblUniprotIdValue.setText("val");
		//lblDescriptionValue.setText("<html>val<br/><br/>d</html>");
	}


	/** Initialize the edge info panel */
	public void initEdgeInfoPanel() {
		//Scrollable Panel
		ScrollablePanel generalEdgeInfoPanel = new ScrollablePanel();
		generalEdgeInfoPanel.setScrollableWidth(ScrollableSizeHint.FIT);
		generalEdgeInfoPanel.setScrollableHeight(ScrollableSizeHint.STRETCH);
		generalEdgeInfoPanel.setScrollableBlockIncrement(ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 200);
		generalEdgeInfoPanel.setLayout(new MigLayout("inset 0", "[grow]", "[20px][20px][20px][grow][20px][grow][20px][grow][growprio 101, grow]"));
		
		//JScrollPane
		edgeInfoPanel = new JScrollPane(generalEdgeInfoPanel);
		edgeInfoPanel.setOpaque(false);
		edgeInfoPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		edgeInfoPanel.setBorder(new TitledBorder(null, "Interaction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		Font title = new Font("Lucida Grande", Font.BOLD, 14);
		
		JLabel lblOrigin = new JLabel("Origin");
		lblOrigin.setFont(title);
		
		lblOriginValue = new JLabel();
		lblOriginValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		
		JLabel lblSourceDatabase = new JLabel("Source database(s)");
		lblSourceDatabase.setFont(title);
		
		lblSrcDbValue = new JTextList();
		lblSrcDbValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		
		JLabel lblExperimentalSystem = new JLabel("Experimental system(s)");
		lblExperimentalSystem.setFont(title);
		
		lblExpsysValue = new JTextList();
		lblExpsysValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		
		JLabel lblPublication = new JLabel("Publication(s)");
		lblPublication.setFont(title);
		
		lblPublicationValue = new JTextList();
		lblPublicationValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		
		
		generalEdgeInfoPanel.add(lblOrigin, "cell 0 0");
		generalEdgeInfoPanel.add(lblOriginValue, "cell 0 1");
		generalEdgeInfoPanel.add(lblSourceDatabase, "cell 0 2");
		generalEdgeInfoPanel.add(lblSrcDbValue, "cell 0 3");
		generalEdgeInfoPanel.add(lblExperimentalSystem, "cell 0 4");
		generalEdgeInfoPanel.add(lblExpsysValue, "cell 0 5");
		generalEdgeInfoPanel.add(lblPublication, "cell 0 6");
		generalEdgeInfoPanel.add(lblPublicationValue, "cell 0 7");
	}


	/**
	 * Update the panel with the selected Node
	 * @param selectedNode
	 */
	public void update(PMBNode selectedNode) {
		/* TODO : Retrieve node information from data panel (when the attributes will be done...) */

		//Protein info
		lblGeneNameValue.setText(selectedNode.getGeneName().trim());
		
		((JLinkLabel)lblUniprotIdValue).setUrl("http://uniprot.org/uniprot/"+selectedNode.getUniprotId().trim());
		lblUniprotIdValue.setText("<html><u>"+selectedNode.getUniprotId().trim()+"</u></html>");
		
		if(selectedNode.isGoLoaded()) {
			if(selectedNode.getProteinDescription() != null)
				lblDescriptionValue.setText(selectedNode.getProteinDescription().trim());
			
			//Gene ontology
			ArrayList<String> tmp;
			if (selectedNode.getComponentList() != null && !selectedNode.getComponentList().isEmpty()) {
				tmp = selectedNode.getComponentList();
				lblCcList.setText((String[])tmp.toArray(new String[tmp.size()]));
			} else lblCcList.setText("");

			if (selectedNode.getProcessList() != null && !selectedNode.getProcessList().isEmpty()) {
				tmp = selectedNode.getProcessList();
				lblBioList.setText((String[])tmp.toArray(new String[tmp.size()]));
			} else lblBioList.setText("");

			if (selectedNode.getFunctionList() != null && !selectedNode.getFunctionList().isEmpty()) {
				tmp = selectedNode.getFunctionList();
				lblFunList.setText((String[])tmp.toArray(new String[tmp.size()]));
			} else lblFunList.setText("");
		} else {
			lblDescriptionValue.setText("Gene ontology loading...");
			lblCcList.setText("Gene ontology loading...");
			lblBioList.setText("Gene ontology loading...");
			lblFunList.setText("Gene ontology loading...");
		}

		//Display the node info panel and refresh the interface
		refreshUI(nodeInfoPanel);
	}


	/**
	 * Update the panel with the selected Edge
	 * @param selectedEdge
	 */
	public void update(CyEdge selectedEdge) {
		String origin = Cytoscape.getEdgeAttributes().getStringAttribute(selectedEdge.getIdentifier(), "Origin");
		if(origin != null && !origin.isEmpty()) lblOriginValue.setText(origin);
		
		String sourceDb = Cytoscape.getEdgeAttributes().getStringAttribute(selectedEdge.getIdentifier(), "Source database");
		if(sourceDb != null && !sourceDb.isEmpty()) lblSrcDbValue.setText(sourceDb.split("; "));
		
		String exp = Cytoscape.getEdgeAttributes().getStringAttribute(selectedEdge.getIdentifier(), "Experimental system");
		if(exp != null && !exp.isEmpty()) lblExpsysValue.setText(exp.split("; "));
		
		String pub = Cytoscape.getEdgeAttributes().getStringAttribute(selectedEdge.getIdentifier(), "Pubmed id");
		if(pub != null && !pub.isEmpty()){
			String[] url = pub.split("; ");
			for(int i = 0; i < url.length; i++)
				url[i] = "http://www.ncbi.nlm.nih.gov/pubmed/"+url[i];
			lblPublicationValue.setText(pub.split("; "), url);
		}
		
		//Display the edge info panel and refresh the interface
		refreshUI(edgeInfoPanel);
	}


	/** Update the panel when nothing is selected */
	public void update() {
		JLabel noSelection = new JLabel("<html><center>Please select one node or one edge of a PPiMapBuilder generated network to get information about them.</center></html>");

		try {
			noSelection.setIcon(new ImageIcon(getClass().getResource("/img/info.png")));
		} catch(Exception e) {}

		//Display the default panel and refresh the interface
		refreshUI(noSelection);
	} 
	
	/**
	 * 
	 * @param comp
	 */
	private void refreshUI(JComponent comp) {
		removeAll();
		
		add(comp);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				validate();
				repaint();
			}
		});
	}
}
