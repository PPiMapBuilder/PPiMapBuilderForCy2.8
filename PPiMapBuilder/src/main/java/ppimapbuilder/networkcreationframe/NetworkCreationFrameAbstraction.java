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
	
	public NetworkCreationFrameAbstraction() {
		try {
			myDBConnector = DBConnector.Instance();
		} catch (SQLException e) {
			showError("Connection to database failed", "Connection error!");
			e.printStackTrace();
		} catch (IOException e) {
			showError("Server config missing!", "Server config");
			e.printStackTrace();
		}
	}
	
	/**
	 * Displays an error message using <i>JOptionPane</i>
	 * @param message the error message
	 * @param title the title of the error window
	 */
	private void showError(String message, String title) {
		JOptionPane.showMessageDialog(Cytoscape.getDesktop(), title, message, JOptionPane.ERROR_MESSAGE);
	}
	

	public LinkedHashMap<String, Integer> getOrganisms() {
		LinkedHashMap<String, Integer> orga;
		try {
			orga = myDBConnector.getOrganisms();
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Connection to database failed", "Connection error");
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
			showError("Connection to database failed", "Connection error");
			return null;
		}
		return db;
	}
	
	
}
