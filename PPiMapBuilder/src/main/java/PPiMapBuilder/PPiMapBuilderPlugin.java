package PPiMapBuilder;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import cytoscape.Cytoscape;
import cytoscape.plugin.CytoscapePlugin;
import cytoscape.util.CytoscapeAction;

public class PPiMapBuilderPlugin extends CytoscapePlugin {
	
	public PPiMapBuilderPlugin() {
		CytoscapeAction myAction = new CytoscapeAction("test") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "Hello World");
			}
		};
		myAction.setPreferredMenu("Plugins");
		Cytoscape.getDesktop().getCyMenus().addAction(myAction);
	}

}
