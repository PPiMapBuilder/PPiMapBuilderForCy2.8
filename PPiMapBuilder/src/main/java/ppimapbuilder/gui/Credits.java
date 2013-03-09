package ppimapbuilder.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class Credits extends JFrame {

	private static final long serialVersionUID = 1L; // Instance of the ppimapbuilder menu to prevent several instances 
	
	private static Credits _instance = null; // Instance of the PPiMapBUilder menu to prevent several instances 
	
	/* Frame components */
	private JLabel copyright; // Label to display the copyright
	// Need to be completed...
	/* ---------------- */
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Create the entire credits frame
	 */
	private Credits() {
		getContentPane().setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][grow]"));
		
		//JLabel lblNewLabel = new JLabel(new ImageIcon(getClass().getResource("logo.jpeg")));
		//getContentPane().add(lblNewLabel, "cell 1 0");
		
		// Frame contents
		copyright = new JLabel("Copyright 2013");
		copyright.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(copyright, "cell 1 1,grow");
		
		// Frame parameters : [!] Do not put the 'exit on close' option !
		this.setTitle("About ppimapbuilder"); // Change the frame title
		this.setPreferredSize(new Dimension(400,250)); // Change the dimensions
		this.setMinimumSize(new Dimension(400,250));
		this.setMaximumSize(new Dimension(400,250));
		this.setResizable(false); // The dimensions are fixed
		this.setLocationRelativeTo(null); // The frame is now at the center
	}
	
	/**
	 * Method to access the unique instance of Credits
	 * @return _instance
	 */
	public static Credits Instance() {
		if (_instance == null)
			_instance = new Credits();
		return _instance;
	}
}
