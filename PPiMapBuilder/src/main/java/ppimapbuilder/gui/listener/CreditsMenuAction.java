package ppimapbuilder.gui.listener;

import cytoscape.util.CytoscapeAction;
import java.awt.event.ActionEvent;

import ppimapbuilder.gui.Credits;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class CreditsMenuAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;
	
	private Credits myFrame = Credits.Instance(); // Frame for the credits

	/**
	 * Default constructor
	 */
	public CreditsMenuAction() {
		super();
	}
	
	/**
	 * ActionPerformed which creates the credits frame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		myFrame.setVisible(true); // Set the frame visible
	}
}
