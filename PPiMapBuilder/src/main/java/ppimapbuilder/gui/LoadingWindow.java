package ppimapbuilder.gui;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JWindow;

import uk.ac.ebi.kraken.uuw.services.remoting.SetOperation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

public class LoadingWindow extends JWindow{
	
	private static final long serialVersionUID = 1L;

	public LoadingWindow(String message) {
		super();
		setAlwaysOnTop(true);
		
		JLabel spinner =  new JLabel();
		JLabel text = new JLabel(message);
		
		try {
			spinner.setIcon(new ImageIcon(getClass().getResource("/spinner.gif")));
		} catch (Exception e) {
			spinner.setText("Loading...");
		}
		
		getContentPane().add(spinner, BorderLayout.CENTER);
		getContentPane().add(text, BorderLayout.CENTER);
		
		this.pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width/2 - (getWidth()/2), screenSize.height/2 - (getHeight()/2));
		screenSize = null;
		
		this.setVisible(true);
		repaint();
	}
}
