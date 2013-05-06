package ppimapbuilder.panel.presentation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import javax.swing.ScrollPaneConstants;


/** @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL */
public class PMBPanel extends JPanel {

	private static final long serialVersionUID = 1;

	/** Instance of the ppimapbuilder panel to prevent several instances  */
	private static PMBPanel _instance = null;

	private JLabel lblCellularComponent, lblBiologicalProcesses, lblMolecularFunction;  
	private JLabel lblGeneNameValue, lblDescriptionValue;

	private JLinkLabel lblUniprotIdValue;
	private ScrollablePanel proteinInfoPanel, geneOntologyPanel;

	private JScrollPane proteinInfoScroll, geneOntologyScroll;

	private JPanel edgeInfoPanel, nodeInfoPanel;


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
		proteinInfoPanel.add(lblGeneName);

		lblGeneNameValue = new JLabel();
		lblGeneNameValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		proteinInfoPanel.add(lblGeneNameValue);

		lblUniprotId = new JLabel("Uniprot Accesion");
		lblUniprotId.setFont(title);
		proteinInfoPanel.add(lblUniprotId);

		lblUniprotIdValue = new JLinkLabel();
		lblUniprotIdValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		proteinInfoPanel.add(lblUniprotIdValue);

		lblDescription = new JLabel("Description");
		lblDescription.setFont(title);
		proteinInfoPanel.add(lblDescription);

		lblDescriptionValue = new JLabel();
		lblDescriptionValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		proteinInfoPanel.add(lblDescriptionValue);


		// Gene ontology panel
		geneOntologyPanel = new ScrollablePanel();
		geneOntologyPanel.setScrollableWidth(ScrollableSizeHint.FIT);
		geneOntologyPanel.setScrollableHeight(ScrollableSizeHint.STRETCH);
		geneOntologyPanel.setScrollableBlockIncrement(ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 200);
		geneOntologyPanel.setLayout(new MigLayout("inset 0", "[grow]", "[20px][grow][20px][grow][20px][grow][growprio 101, grow]"));
		geneOntologyPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		// Gene ontology scroll pane
		geneOntologyScroll = new JScrollPane(geneOntologyPanel);
		geneOntologyScroll.setOpaque(false);
		geneOntologyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		geneOntologyScroll.setBorder(new TitledBorder(null, "Gene Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		// Gene ontology labels
		lblCellularComponent = new JLabel("Cellular Component");
		lblCellularComponent.setFont(title);
		geneOntologyPanel.add(lblCellularComponent, "cell 0 0, grow");

		lblBiologicalProcesses = new JLabel("Biological Processes");
		lblBiologicalProcesses.setFont(title);
		geneOntologyPanel.add(lblBiologicalProcesses, "cell 0 1, grow");

		lblMolecularFunction = new JLabel("Molecular Functions");
		lblMolecularFunction.setFont(title);
		geneOntologyPanel.add(lblMolecularFunction, "cell 0 2, grow");

		nodeInfoPanel.add(proteinInfoScroll, "cell 0 0,grow");
		nodeInfoPanel.add(geneOntologyScroll, "cell 0 1,grow");

		//Demo
		//lblGeneNameValue.setText("val");
		//lblUniprotIdValue.setText("val");
		//lblDescriptionValue.setText("<html>val<br/><br/>d</html>");
	}


	/** Initialize the edge info panel */
	public void initEdgeInfoPanel() {
		edgeInfoPanel = new JPanel();
	}


	/**
	 * Update the panel with the selected Node
	 * @param selectedNode
	 */
	public void update(PMBNode selectedNode) {
		removeAll();
		add(nodeInfoPanel);

		/* TODO : Retrieve node information from data panel (when the attributes will be done...) */

		//Protein info
		lblGeneNameValue.setText(selectedNode.getGeneName().trim());
		lblUniprotIdValue.setUrl("http://uniprot.org/uniprot/"+selectedNode.getUniprotId().trim());
		lblUniprotIdValue.setText("<html><u>"+selectedNode.getUniprotId().trim()+"</u></html>");
		if (selectedNode.getProteinDescription() != null)
			lblDescriptionValue.setText(selectedNode.getProteinDescription().trim());

		//Gene ontology
		geneOntologyPanel.removeAll();
		int row = 0;
		if (!selectedNode.getComponentList().isEmpty()) {
			geneOntologyPanel.add(lblCellularComponent, "cell 0 "+(row++)+"");
			geneOntologyPanel.add(new JTextList(selectedNode.getComponentList()), "cell 0 "+(row++)+"");
		}

		if (!selectedNode.getProcessList().isEmpty()) {
			geneOntologyPanel.add(lblBiologicalProcesses, "cell 0 "+(row++)+"");
			geneOntologyPanel.add(new JTextList(selectedNode.getProcessList()), "cell 0 "+(row++)+"");
		} 

		if (!selectedNode.getFunctionList().isEmpty()) {
			geneOntologyPanel.add(lblMolecularFunction, "cell 0 "+(row++)+"");
			geneOntologyPanel.add(new JTextList(selectedNode.getFunctionList()), "cell 0 "+(row++)+"");
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				validate();
				repaint();
			}
		});
	}


	/**
	 * Update the panel with the selected Edge
	 * @param selectedEdge
	 */
	public void update(CyEdge selectedEdge) {

	}


	/** Update the panel when nothing is selected */
	public void update() {
		removeAll();
		JLabel noSelection = new JLabel("<html><center>Please select one node or one edge of a PPiMapBuilder generated network to get information about them.</center></html>");

		try {
			noSelection.setIcon(new ImageIcon(getClass().getResource("/img/info.png")));
		} catch(Exception e) {}

		add(noSelection);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				validate();
				repaint();
			}
		});
	} 
}
