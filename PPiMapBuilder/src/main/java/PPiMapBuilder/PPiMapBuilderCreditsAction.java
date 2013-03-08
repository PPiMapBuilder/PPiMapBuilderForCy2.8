package PPiMapBuilder;

import cytoscape.util.CytoscapeAction;
import java.awt.event.ActionEvent;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PPiMapBuilderCreditsAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;
	
	private PPiMapBuilderCredits myFrame = PPiMapBuilderCredits.Instance(); // Frame for the credits

	/**
	 * Default constructor
	 */
	public PPiMapBuilderCreditsAction() {
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
