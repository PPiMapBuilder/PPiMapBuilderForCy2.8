package ppimapbuilder.menu;

import ppimapbuilder.LoadingWindow;
import ppimapbuilder.creditframe.CreditFrameControl;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameControl;
import ppimapbuilder.panel.PMBPanelControl;

public class PMBMenuControl {

	public static void openNetworkCreationFrame() {
		new LoadingWindow("Connecting to server database...") {
			public void process() {
				NetworkCreationFrameControl.open(); // Creation of the frame
				PMBPanelControl.focusPanel(); // Focus on the panel			
			}
		};
	}
	
	
	public static void openCreditFrame() {
		CreditFrameControl.open();
	}
	
	
}
