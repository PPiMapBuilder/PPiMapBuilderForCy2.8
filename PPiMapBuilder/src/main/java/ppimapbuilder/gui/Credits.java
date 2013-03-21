package ppimapbuilder.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

import ppimapbuilder.gui.label.JLinkLabel;
import ppimapbuilder.gui.label.LogoIcon;
import ppimapbuilder.gui.label.PicardLabIcon;
import ppimapbuilder.gui.listener.WindowCloseEscapeListener;

import cytoscape.Cytoscape;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class Credits extends JFrame {

	private static final long serialVersionUID = 1L; // Instance of the ppimapbuilder menu to prevent several instances 
	
	private static Credits _instance = null; // Instance of the PPiMapBuilder menu to prevent several instances 

	private WindowCloseEscapeListener escapeListener;
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Create the entire credits frame
	 */
	private Credits() {
		super("About PPiMapBuilder");
		
		Dimension size = new Dimension(340, 340);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		setResizable(false);
		
		setLayout(new MigLayout("inset 5", "[grow][22px][grow]", "[grow][][][][grow][][][grow][][][][grow]"));
		
		// PPiMapBuilder Logo
		JLabel lblLogo = new LogoIcon();
		getContentPane().add(lblLogo, "cell 0 1 3,alignx center");
		
		// PPiMapBuilder Name
		JLabel lblPpimapbuilder = new JLabel("PPiMapBuilder");
		lblPpimapbuilder.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		getContentPane().add(lblPpimapbuilder, "cell 0 2 3,alignx center");
		
		// PPiMapBuilder email
		JLabel lblEmail = new JLinkLabel("<ppimapbuilder@gmail.com>", "mailto:ppimapbuilder@gmail.com");
		lblEmail.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblEmail.setForeground(Color.GRAY);
		lblEmail.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		getContentPane().add(lblEmail, "cell 0 3 3 1,alignx center");
		
		// Initiated by
		JLabel lblInitiatedBy = new JLabel("Initiated by:");
		lblInitiatedBy.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		getContentPane().add(lblInitiatedBy, "cell 0 5 3 1,alignx center");
			
		// Pablo Echeverría
		JLabel lblPabloEcheverria = new JLabel("Pablo Echeverría");
		getContentPane().add(lblPabloEcheverria, "cell 0 6,alignx center");
		
		// PicardLab logo
		JLabel lblPicardLabLogo = new PicardLabIcon();
		lblPicardLabLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		getContentPane().add(lblPicardLabLogo, "cell 2 6,alignx center");
		
		// Developpers
		JLabel lblAuthors = new JLabel("Developpers:");
		lblAuthors.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		getContentPane().add(lblAuthors, "cell 0 8 3 1,alignx center");
		
		// Guillaume Cornut
		JLabel lblGuillaumeCornutPierre = new JLabel("Guillaume Cornut");
		lblGuillaumeCornutPierre.setToolTipText("Wonderful Gui");
		getContentPane().add(lblGuillaumeCornutPierre, "cell 0 9,alignx center");
		
		// Pierre Cressant
		JLabel lblPierreCressant = new JLabel("Pierre Cressant");
		lblPierreCressant.setToolTipText("Amazing Piotr");
		getContentPane().add(lblPierreCressant, "cell 2 9,alignx center");
			
		// Pierre Dupuis
		JLabel lblPierreDupuis = new JLabel("Pierre Dupuis");
		lblPierreDupuis.setToolTipText("Tremendous Boss");
		getContentPane().add(lblPierreDupuis, "cell 0 10,alignx center");
		
		// Kévin Gravouil
		JLabel lblKvinGravouil = new JLabel("Kévin Gravouil");
		lblKvinGravouil.setToolTipText("Marvelous Keuv");
		getContentPane().add(lblKvinGravouil, "cell 2 10,alignx center");
			
		// The frame is now centered relatively to Cytoscape
		this.setLocationRelativeTo(Cytoscape.getDesktop()); 
		
		// Close window on escape key
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		escapeListener = new WindowCloseEscapeListener(this);
		manager.addKeyEventDispatcher(escapeListener);
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
