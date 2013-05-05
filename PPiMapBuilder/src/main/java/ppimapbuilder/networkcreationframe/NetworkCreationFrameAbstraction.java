package ppimapbuilder.networkcreationframe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;

import cytoscape.Cytoscape;

import ppimapbuilder.ppidb.api.DBConnector;

public class NetworkCreationFrameAbstraction {

	private DBConnector myDBConnector;
	
	public NetworkCreationFrameAbstraction() throws SQLException, IOException {
			myDBConnector = DBConnector.Instance();
	}
	
	public LinkedHashMap<String, Integer> getOrganisms() {
		LinkedHashMap<String, Integer> orga;
		try {
			orga = myDBConnector.getOrganisms();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "Connection error", "Connection to database failed", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return orga;
	}

	public ArrayList<String> getDatabases() {
		ArrayList<String> db;
		try {
			db = myDBConnector.getDatabases();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(),"Connection error", "Connection to database failed", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return db;
	}
	
	
}
