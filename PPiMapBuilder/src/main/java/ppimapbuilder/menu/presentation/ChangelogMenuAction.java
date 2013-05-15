package ppimapbuilder.menu.presentation;

import cytoscape.util.CytoscapeAction;
import java.awt.event.ActionEvent;
import ppimapbuilder.menu.PMBMenuControl;

/**
 *
 * @author Keuv
 */
class ChangelogMenuAction extends CytoscapeAction {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public ChangelogMenuAction() {
        super();
    }

    /**
     * ActionPerformed which creates the changelog frame
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        PMBMenuControl.openChangelogFrame();
    }
}
