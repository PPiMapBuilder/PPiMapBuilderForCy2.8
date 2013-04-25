package ppimapbuilder.networkcreationframe.presentation;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import ppimapbuilder.networkcreationframe.NetworkCreationFrameControl;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class SubmitListener implements ActionListener{
	
	public void actionPerformed(ActionEvent e) {
		NetworkCreationFrameControl.createNetwork();
	}
		
}
