package ppimapbuilder.network;

import java.io.IOException;
import java.sql.SQLException;
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
			if(res.isEmpty()) throw new SQLException("empty "+id);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Error SQL : "+e1.getLocalizedMessage());
			return null;
		}
		catch (IOException e2) {
			JOptionPane.showMessageDialog(null, "Error config : "+e2.getLocalizedMessage());
			return null;
		}
		return res;
	}
	
}
