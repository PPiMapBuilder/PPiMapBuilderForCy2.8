package ppimapbuilder.menu.presentation;

import cytoscape.util.CytoscapeAction;
import java.awt.event.ActionEvent;
import ppimapbuilder.menu.PMBMenuControl;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class CreditMenuAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor
	 */
	public CreditMenuAction() {
		super();
	}
	
	/**
	 * ActionPerformed which creates the credits frame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		PMBMenuControl.openCreditFrame();
	}
}
