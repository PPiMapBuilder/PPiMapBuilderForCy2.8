package ppimapbuilder.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ppimapbuilder.gui.listener.WindowCloseEscapeListener;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class Credits extends JFrame {

	private static final long serialVersionUID = 1L; // Instance of the ppimapbuilder menu to prevent several instances 
	
	private static Credits _instance = null; // Instance of the PPiMapBUilder menu to prevent several instances 
	// Need to be completed...
	/* ---------------- */
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Create the entire credits frame
	 */
	private Credits() {
		setMaximumSize(new Dimension(266, 250));
		setMinimumSize(new Dimension(266, 250));
		setPreferredSize(new Dimension(266, 250));
		setResizable(false);
		setTitle("About PPiMapBuilder\n");
		
		getContentPane().setLayout(null);
		JLabel lblLogo = new JLabel();
		lblLogo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setMaximumSize(new Dimension(105, 136));
		lblLogo.setBounds(65, 6, 136, 134);
		getContentPane().add(lblLogo);
		
		JLabel lblCopyright = new JLabel("Copyright © 2013");
		lblCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopyright.setBounds(12, 193, 242, 16);
		getContentPane().add(lblCopyright);
		
		JLabel lblPpimapbuilder = new JLabel("PPiMapBuilder");
		lblPpimapbuilder.setHorizontalAlignment(SwingConstants.CENTER);
		lblPpimapbuilder.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblPpimapbuilder.setBounds(74, 152, 117, 28);
		getContentPane().add(lblPpimapbuilder);
		
		try {
			lblLogo.setIcon(new ImageIcon(getClass().getResource("/logo.png")));
		} catch(Exception e) {
			lblLogo.setText("LOGO");
			e.printStackTrace();
		}
		this.setLocationRelativeTo(null); // The frame is now at the center
		
		//this.setF
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new WindowCloseEscapeListener(this));
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
