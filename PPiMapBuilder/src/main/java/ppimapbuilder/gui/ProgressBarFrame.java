package ppimapbuilder.gui;

import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;

public class ProgressBarFrame extends JPopupMenu{

	private JProgressBar myProgressBar;
	
	public ProgressBarFrame() {
		super();
		
		myProgressBar = new JProgressBar();
		myProgressBar.setIndeterminate(true);
		
		this.add(myProgressBar);
		//this.setLocation(null);
	}
}
