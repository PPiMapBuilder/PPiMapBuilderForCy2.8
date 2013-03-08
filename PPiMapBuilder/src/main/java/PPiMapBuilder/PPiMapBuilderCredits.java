package PPiMapBuilder;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PPiMapBuilderCredits extends JFrame {

	private static final long serialVersionUID = 1L; // Instance of the PPiMapBuilder menu to prevent several instances 
	
	private static PPiMapBuilderCredits _instance = null; // Instance of the PPiMapBUilder menu to prevent several instances 
	
	/* Frame components */
	private JLabel copyright; // Label to display the copyright
	// Need to be completed...
	/* ---------------- */
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Create the entire credits frame
	 */
	private PPiMapBuilderCredits() {
		
		// Frame contents
		copyright = new JLabel("Copyright 2013");
		this.getContentPane().add(copyright);
		
		// Frame parameters : [!] Do not put the 'exit on close' option !
		this.setTitle("About PPiMapBuilder"); // Change the frame title
		this.setPreferredSize(new Dimension(400,250)); // Change the dimensions
		this.setMinimumSize(new Dimension(400,250));
		this.setMaximumSize(new Dimension(400,250));
		this.setResizable(false); // The dimensions are fixed
		this.setLocationRelativeTo(null); // The frame is now at the center
	}
	
	/**
	 * Method to access the unique instance of PPiMapBuilderCredits
	 * @return _instance
	 */
	public static PPiMapBuilderCredits Instance() {
		if (_instance == null)
			_instance = new PPiMapBuilderCredits();
		return _instance;
	}
}
