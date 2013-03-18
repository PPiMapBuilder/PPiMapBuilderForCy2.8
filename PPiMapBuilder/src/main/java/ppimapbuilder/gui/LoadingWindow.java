package ppimapbuilder.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import cytoscape.Cytoscape;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;


public abstract class LoadingWindow implements Runnable{
	
	private JDialog loadingwindow;
	
	private JLabel text;
	
	private JProgressBar progressbar;
	
	/**
	 * Create a new loading window with a displayed message
	 * @param message the message that will be displayed on the loading window
	 */
	public LoadingWindow(String message) {
		initialize(message);
		run();
	}
	
	/**
	 * Creates the JDialog with progress bar and text message
	 * @param message the message that will be displayed on the loading window
	 */
	private void initialize(String message) {
		loadingwindow = new JDialog();
		loadingwindow.setUndecorated(true);
		
		JPanel center_panel = new JPanel(); // New panel
		center_panel.setLayout(new BorderLayout(0, 0));
		center_panel.setBorder(new EmptyBorder(10,10,10,10)); // Border adding a 10px margin
		
		text = new JLabel(message);
		text.setBorder(new EmptyBorder(5,5,5,5));
		
		this.progressbar = new JProgressBar(); // New progress bar
		progressbar.setIndeterminate(true); // Infinite progress bar
		
		center_panel.add(text, BorderLayout.CENTER); // Add the text label to the panel
		center_panel.add(progressbar, BorderLayout.SOUTH); // Add the progress bar to the panel
		
		loadingwindow.getContentPane().add(center_panel, BorderLayout.CENTER); // Add the panel to the JDialog
		
		loadingwindow.pack();
		loadingwindow.setLocationRelativeTo(Cytoscape.getDesktop());
		loadingwindow.toFront();
		
		//Keep the loading window to front when Cytoscape is focused
		Cytoscape.getDesktop().addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				loadingwindow.toFront();
				loadingwindow.setLocationRelativeTo(Cytoscape.getDesktop());
			}
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {}
		});
		
		//Keep the loading window centered when Cytoscape is resized or moved
		Cytoscape.getDesktop().addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				loadingwindow.setLocationRelativeTo(Cytoscape.getDesktop());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				loadingwindow.setLocationRelativeTo(Cytoscape.getDesktop());
			}
			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentShown(ComponentEvent arg0) {}
		});
	}
	
	
	/**
	 * Sets the progress bar's current value to n. (between 0 and 100)
	 * @param n the new value
	 */
	public void setProgress(int n) {
		progressbar.setValue(n);
	}
	
	
	/**
	 * Sets the <i>indeterminate</i> property of the progress bar. 
	 * An indeterminate progress bar continuously displays animation indicating that an operation of unknown length is occurring.
	 * @param state <i>true</i> if the progress bar should change to indeterminate mode; <i>false</i> if it should revert to normal.
	 */
	public void setIndeterminate(boolean state) {
		progressbar.setIndeterminate(state);
	}
	
	
	/**
	 * Sets the displayed message on the loading window
	 * @param message the message that will be displayed on the loading window
	 */
	public void setMessage(String message) {
		this.text.setText(message);
	}

	
	/**
	 * When launch, the loading window will appear, then run the defined process and close
	 */
	@Override
	public void run() {
		loadingwindow.setVisible(true);
		new Thread(new Runnable() { // Create a new thread for the treatment to keep the progress bar running.
			@Override
			public void run(){
				process();
				loadingwindow.setVisible(false);
			}
	    }).start();
	}
	
	
	/**
	 * Process executed at launch. Need to be defined by the user
	 */
	public abstract void process();
}
