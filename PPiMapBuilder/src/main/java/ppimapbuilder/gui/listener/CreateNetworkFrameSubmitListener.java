package ppimapbuilder.gui.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import ppimapbuilder.Mediator;
import ppimapbuilder.PMBNode;
import ppimapbuilder.gui.CreateNetworkFrame;
import ppimapbuilder.gui.ProgressBarFrame;
import ppimapbuilder.ppidb.api.DBConnector;
import ppimapbuilder.ppidb.api.SQLResult;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class CreateNetworkFrameSubmitListener implements ActionListener{
	
	private CreateNetworkFrame myFrame; // Frame which is contains the submit
	private ArrayList<String> poiList; // List of proteins of interest
	//private ArrayList<String> dbList; // List of selected organisms
	//private ArrayList<String> orgaList; // List of selected databases
	private CyNetwork myNetwork; // Result network
	private PMBNode poiNode, node2; // nodes
	
	private DBConnector myDBConnector;
	private Mediator myMediator = Mediator.Instance();
	
	public CreateNetworkFrameSubmitListener(CreateNetworkFrame myFrame, DBConnector myDBConnector) {
		this.myFrame = myFrame;
		this.myDBConnector = myDBConnector;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			poiList = myFrame.getIdentifiers(); // Retrieve the identifier list
		}
		catch (ArrayStoreException e2) {
			// Empty text area
			JOptionPane.showMessageDialog(myFrame, "The identifier list is empty.", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		myFrame.close();
		
		//ProgressBarFrame myPBF = new ProgressBarFrame();
		//myPBF.setVisible(true);
		
		
		//dbList = myFrame.getDatabaseValues(); // Retrieve the database list
		//orgaList = myFrame.getOrganismValues(); // Retrieve the organism list
		
		// Creation of the network
		myNetwork = Cytoscape.createNetwork("", false); // Creation of a network (with the name of the poi)

		// Creation of the network components
		for (String id : poiList) { // For each protein of interest
			SQLResult res;
			// Get result from 
			try {
				res = myDBConnector.getAllData(id);
				if(res.isEmpty()) throw new SQLException("empty "+id);
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(myFrame, "Error SQL: "+e1.getMessage());
				e1.printStackTrace();
				return;
			}
			
			PMBNode A, B;
			CyEdge interaction;
			LinkedHashMap<String, String> fields;
			
			// For each line
			for(String row: res.keySet()) {
				//Get fields
				fields = res.getData(row);
				System.out.println(fields);
				
				//Create Nodes
				try {
					
					A = new PMBNode(Cytoscape.getCyNode(fields.get("interactor_nameA"), true), fields.get("uniprotA"));
					B = new PMBNode(Cytoscape.getCyNode(fields.get("interactor_nameB"), true), fields.get("uniprotB"));
					myNetwork.addNode(A);
					myNetwork.addNode(B);
					myMediator.addNode(A);
					myMediator.addNode(B);
					
					//Create Edges
					interaction = Cytoscape.getCyEdge(A, B, Semantics.INTERACTION, "pp", true);
					myNetwork.addEdge(interaction);
				
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				
			}
			
		}
		
		// Add the network to the mediator
		Mediator.Instance().addNetwork(myNetwork);
		
		//myPBF.setVisible(false);

	}
		
}
