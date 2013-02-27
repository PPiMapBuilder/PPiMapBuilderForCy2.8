package PPiMapBuilder;

import cytoscape.util.CytoscapeAction;
import java.awt.event.ActionEvent;

/**
 * 
 * @author pidupuis
 *
 */
public class PPiMapBuilderCreditsAction extends CytoscapeAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public PPiMapBuilderCreditsAction() {
		super();
	}
	
	/**
	 * ActionPerformed which contains the actions which are triggered when you click on your launcher
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		PPiMapBuilderCredits myFrame = new PPiMapBuilderCredits();
		myFrame.setVisible(true);
	}
}
