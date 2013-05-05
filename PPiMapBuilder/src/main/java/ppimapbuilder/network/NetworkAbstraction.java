package ppimapbuilder.network;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ppimapbuilder.ppidb.api.DBConnector;
import ppimapbuilder.ppidb.api.SQLResult;

public class NetworkAbstraction {
	
	public static SQLResult getAllData(String id, ArrayList<String> dbList, ArrayList<Integer> orgaList, int refOrganism) {
		SQLResult res;
		// Get result from 
		try {
			res = DBConnector.Instance().getAllData(id, refOrganism, dbList, orgaList);
			if(res.isEmpty()) 
				JOptionPane.showMessageDialog(null, id+" not found");
		} catch (Exception e1) {
			e1.printStackTrace();
			//JOptionPane.showMessageDialog(null, "Error SQL : "+e1.getLocalizedMessage());
			return null;
		}
		return res;
	}
	
}
