package ppimapbuilder.menu;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import cytoscape.Cytoscape;

import ppimapbuilder.LoadingWindow;
import ppimapbuilder.changelog.ChangelogFrameControl;
import ppimapbuilder.creditframe.CreditFrameControl;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameControl;
import ppimapbuilder.panel.PMBPanelControl;

public class PMBMenuControl {

	public static void openNetworkCreationFrame() {
		new LoadingWindow("Connecting to server database...") {
			public void process() {
				try {
					NetworkCreationFrameControl.open();
					PMBPanelControl.focusPanel(); // Focus on the panel	
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(Cytoscape.getDesktop(),"Connection error!", "Connection to database failed", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(Cytoscape.getDesktop(),"Server config","Server config missing!", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
						
			}
		};
	}
	
	
	public static void openCreditFrame() {
		CreditFrameControl.open();
	}
	
        /*
         * Open the changelog frame
         */
  	public static void openChangelogFrame() {
            ChangelogFrameControl.open();
	}      
	
}
