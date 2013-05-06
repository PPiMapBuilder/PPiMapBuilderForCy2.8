package ppimapbuilder.network;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import ppimapbuilder.ppidb.api.DBConnector;
import ppimapbuilder.ppidb.api.SQLResult;

public class NetworkAbstraction {
	
	public static SQLResult getAllData(String id, ArrayList<String> dbList, ArrayList<Integer> orgaList, int refOrganism) {
		SQLResult res;
		// Get result from 
		try {
			res = DBConnector.Instance().getAllData(id, refOrganism, dbList, orgaList);
			/*if(res.isEmpty()) 
				JOptionPane.showMessageDialog(null, id+" not found");*/
		} catch (Exception e1) {
			e1.printStackTrace();
			//JOptionPane.showMessageDialog(null, "Error SQL : "+e1.getLocalizedMessage());
			return null;
		}
		return res;
	}
	
	public static HashMap<Integer, HashMap<String, String>> getSecondInteractors(ArrayList<Integer> ptnIDs) {
		HashMap<Integer, HashMap<String, String>> res;
		
		try {
			res = DBConnector.Instance().getSecondInteractors(ptnIDs);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return res;
		
	}
			
}
