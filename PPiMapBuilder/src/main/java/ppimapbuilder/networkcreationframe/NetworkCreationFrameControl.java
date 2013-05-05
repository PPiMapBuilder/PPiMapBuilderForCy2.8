package ppimapbuilder.networkcreationframe;

import java.rmi.ServerError;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import ppimapbuilder.network.NetworkControl;
import ppimapbuilder.networkcreationframe.presentation.NetworkCreationFrame;

import cytoscape.Cytoscape;

public class NetworkCreationFrameControl {

	private static NetworkCreationFrame myFrame;
	private static NetworkCreationFrameAbstraction myAbstraction;
	
	/**
	 * 
	 * @throws ServerError
	 */
	public static void open() {
		
		myFrame = NetworkCreationFrame.Instance();
		myAbstraction = new NetworkCreationFrameAbstraction();
		
		if(!myFrame.getWindow().isVisible()) {
		updateInterfaceWithDatabase();
		}
		myFrame.getWindow().setLocationRelativeTo(Cytoscape.getDesktop());
		myFrame.getWindow().setVisible(true);
	}
	
	/**
	 * 
	 * @throws ServerError
	 */
	private static void updateInterfaceWithDatabase() {
		myFrame.clearFormFields();
		myFrame.updateLists(myAbstraction.getOrganisms(), myAbstraction.getDatabases());
	}
	
	public static void createNetwork() {
		
		ArrayList<String> poiList; // List of proteins of interest
		ArrayList<String> dbList; // List of selected organisms
		ArrayList<Integer> orgaList; // List of selected databases
		int refOrganism;
		
		// POI list
		try {
			poiList = myFrame.getIdentifiers(); // Retrieve the identifier list
		}
		catch (ArrayStoreException e2) {
			// Empty text area
			JOptionPane.showMessageDialog(null, "The identifier list is empty.", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// DB list
		dbList = myFrame.getSelectedDatabases(); // Retrieve the database list
		
		// Orga list
		orgaList = myFrame.getSelectedOrganisms(); // Retrieve the organism list
		
		// Reference orga
		refOrganism = myFrame.getSelectedReferenceOrganism();
		
		// TODO : distinguish the reference organism !
		NetworkControl.Instance().createNetwork(poiList, dbList, orgaList, refOrganism);
		
		
	}
	
	public static void closeFrame() {
		myFrame.close();
	}

}
