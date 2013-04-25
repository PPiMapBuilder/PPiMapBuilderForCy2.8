package ppimapbuilder.panel;

import javax.swing.SwingConstants;

import ppimapbuilder.panel.presentation.PMBPanel;

import cytoscape.Cytoscape;
import cytoscape.view.cytopanels.CytoPanelImp;

public class PMBPanelControl {

	/**
	 * Method which creates the panel
	 */
	public static void createPanel() {
		CytoPanelImp ctrlPanel = (CytoPanelImp) Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST); // Retrieve the Cytoscape control panel
		ctrlPanel.add(PMBPanel.Instance(), 1); // Add the new panel at the index 1 (so at the second position in the control panel)	
	}
	
	/**
	 * Method which focus on the panel
	 */
	public static void focusPanel() {
		CytoPanelImp ctrlPanel = (CytoPanelImp) Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST); // Retrieve the Cytoscape control panel
		ctrlPanel.setSelectedIndex(ctrlPanel.indexOfComponent(PMBPanel.Instance())); // Specify that the panel selected by default is our panel
	}
	
	/**
	 * Method which update the panel
	 */
	public static void updatePanel() {
		PMBPanel.Instance().update();
	}

}
