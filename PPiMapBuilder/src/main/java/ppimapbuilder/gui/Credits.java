package ppimapbuilder.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ppimapbuilder.gui.listener.WindowCloseEscapeListener;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import cytoscape.generated.Desktop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		Dimension size = new Dimension(315, 297);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		setResizable(false);
		setTitle("About PPiMapBuilder\n");
		
		getContentPane().setLayout(null);
		JLabel lblLogo = new JLabel();
		lblLogo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setMaximumSize(new Dimension(105, 136));
		lblLogo.setBounds(89, 6, 136, 134);
		getContentPane().add(lblLogo);
		
		
		JLabel lblPpimapbuilder = new JLabel("PPiMapBuilder");
		lblPpimapbuilder.setHorizontalAlignment(SwingConstants.CENTER);
		lblPpimapbuilder.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblPpimapbuilder.setBounds(99, 143, 117, 28);
		getContentPane().add(lblPpimapbuilder);
		
		try {
			lblLogo.setIcon(new ImageIcon(getClass().getResource("/logo.png")));
			
			JLabel lblAuthors = new JLabel("Authors:");
			lblAuthors.setHorizontalAlignment(SwingConstants.CENTER);
			lblAuthors.setBounds(127, 193, 61, 16);
			getContentPane().add(lblAuthors);
			
			JLabel lblGuillaumeCornutPierre = new JLabel("Guillaume CORNUT");
			lblGuillaumeCornutPierre.setToolTipText("Gcornut");
			lblGuillaumeCornutPierre.setHorizontalAlignment(SwingConstants.CENTER);
			lblGuillaumeCornutPierre.setBounds(20, 220, 127, 16);
			getContentPane().add(lblGuillaumeCornutPierre);
			
			JLabel lblPierreCressant = new JLabel("Pierre CRESSANT");
			lblPierreCressant.setToolTipText("Piotr");
			lblPierreCressant.setHorizontalAlignment(SwingConstants.CENTER);
			lblPierreCressant.setBounds(167, 221, 127, 16);
			getContentPane().add(lblPierreCressant);
			
			JLabel lblPierreDupuis = new JLabel("Pierre DUPUIS");
			lblPierreDupuis.setToolTipText("Boss");
			lblPierreDupuis.setHorizontalAlignment(SwingConstants.CENTER);
			lblPierreDupuis.setBounds(20, 249, 127, 16);
			getContentPane().add(lblPierreDupuis);
			
			JLabel lblKvinGravouil = new JLabel("Kévin GRAVOUIL");
			lblKvinGravouil.setToolTipText("Keuv");
			lblKvinGravouil.setHorizontalAlignment(SwingConstants.CENTER);
			lblKvinGravouil.setBounds(167, 249, 127, 16);
			getContentPane().add(lblKvinGravouil);
			
			JLabel label = new JLabel("<ppimapbuilder@gmail.com>");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBorder(new EmptyBorder(0, 0, 0, 0));
			label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label.setForeground(Color.GRAY);
			label.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			label.setBounds(49, 168, 216, 16);
			label.setOpaque(false);
			getContentPane().add(label);
		} catch(Exception e) {
			lblLogo.setText("LOGO");
			e.printStackTrace();
		}
		this.setLocationRelativeTo(null); // The frame is now at the center
		
		// Close window on escape
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
